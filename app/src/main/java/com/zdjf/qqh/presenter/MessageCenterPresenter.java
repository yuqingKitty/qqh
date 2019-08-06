package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.MessageCenterBean;
import com.zdjf.qqh.data.entity.StatisticsBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IMessageCenterView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class MessageCenterPresenter extends BasePresenter<IMessageCenterView> {
    private int pageNumber = 1;
    private int pageSize = 10;

    public MessageCenterPresenter(Activity context, IMessageCenterView view) {
        super(context, view);
    }

    /**
     * 初始化数据
     */
    public void initMessageListData() {
        pageNumber = 1;
        getData(pageNumber, pageSize);
    }

    /**
     * 获取数据
     */
    public void getData(final int number, final int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", number);
        params.put("pageSize", pageSize);
        mModel.getMessageCenterList(params, new DisposableObserver<MessageCenterBean>() {
            @Override
            public void onNext(MessageCenterBean messageCenterBean) {
                if (parse(mContext, messageCenterBean)) {
                    List<MessageCenterBean.MessageBean> listBean = messageCenterBean.messageList;
                    if (listBean != null && listBean.size() > 0) {
                        if (pageNumber == 1) {
                            //刷新完成
                            obtainView().refreshDataSuccess(listBean);
                        } else {
                            obtainView().loadDataSuccess(listBean);
                        }
                        if (listBean.size() < pageSize) {
                            obtainView().noMordData();
                        }
                        pageNumber += 1;
                    } else if (pageNumber == 1) {
                        obtainView().clearData();
                    } else {
                        obtainView().noMordData();
                    }
                } else {
                    obtainView().loadMordFail();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(throwable.toString());
                obtainView().ShowToast("网络异常");
                obtainView().loadMordFail();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMoreData() {
        getData(pageNumber, pageSize);
    }


    /**
     * 产品点击统计
     */
    public void recordMessage(String MessageId, final String url, String module_name, String module_order) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("MessageId", MessageId);
        params.put("module_name", module_name);
        params.put("module_order", module_order);

        mModel.toStatistics(params, new DisposableObserver<StatisticsBean>() {


            @Override
            public void onNext(StatisticsBean statisticsBean) {
                if (parse(mContext, statisticsBean)) {
                    obtainView().onRecordSuccess(url, statisticsBean.getStatisticsDetailId());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }



}
