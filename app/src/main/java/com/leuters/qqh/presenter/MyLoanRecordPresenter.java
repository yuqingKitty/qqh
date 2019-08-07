package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.data.entity.MyLoanRecordBean;
import com.leuters.qqh.data.entity.StatisticsBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.IMyLoanRecordView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class MyLoanRecordPresenter extends BasePresenter<IMyLoanRecordView> {
    private int pageNumber = 1;
    private int pageSize = 10;

    public MyLoanRecordPresenter(Activity context, IMyLoanRecordView view) {
        super(context, view);
    }

    public void initData(){
        initLoanRecordListData();
        getRecommendProductData();
    }

    /**
     * 初始化借款记录数据
     */
    public void initLoanRecordListData() {
        pageNumber = 1;
        getLoanRecordData(pageNumber, pageSize);
    }

    /**
     * 获取借款记录数据
     */
    public void getLoanRecordData(final int number, final int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", number);
        params.put("pageSize", pageSize);
        mModel.getMyLoanRecordList(params, new DisposableObserver<MyLoanRecordBean>() {
            @Override
            public void onNext(MyLoanRecordBean myLoanRecordBean) {
                if (parse(mContext, myLoanRecordBean)) {
                    List<MyLoanRecordBean.MyLoanBean> listBean = myLoanRecordBean.productAccessList;
                    if (listBean != null && listBean.size() > 0) {
                        if (pageNumber == 1) {
                            //刷新完成
                            obtainView().refreshLoanRecordDataSuccess(listBean);
                        } else {
                            obtainView().loadLoanRecordDataSuccess(listBean);
                        }
                        if (listBean.size() < pageSize) {
                            obtainView().noLoanRecordMordData();
                        }
                        pageNumber += 1;
                    } else if (pageNumber == 1) {
                        obtainView().clearLoanRecordData();
                    } else {
                        obtainView().noLoanRecordMordData();
                    }
                } else {
                    obtainView().loadLoanRecordMordFail();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(throwable.toString());
                obtainView().ShowToast("网络异常");
                obtainView().loadLoanRecordMordFail();
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
        getLoanRecordData(pageNumber, pageSize);
    }

    /**
     * 获取推荐列表数据
     */
    public void getRecommendProductData() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", 1);
        params.put("pageSize", 5);
        mModel.getMyRecommendProductList(params, new DisposableObserver<MyLoanRecordBean>() {
            @Override
            public void onNext(MyLoanRecordBean myLoanRecordBean) {
                if (parse(mContext, myLoanRecordBean)) {
                    List<MyLoanRecordBean.MyRecommendProductBean> listBean = myLoanRecordBean.productList;
                    if (listBean != null && listBean.size() > 0) {
                        obtainView().loadRecommendProductDataSuccess(listBean);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(throwable.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 产品点击统计
     */
    public void recordProduct(String MessageId, final String url, String module_name, String module_order) {
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
