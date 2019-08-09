package com.leuters.qqh.presenter;


import android.app.Activity;

import com.leuters.qqh.data.entity.CompleteBean;
import com.leuters.qqh.data.entity.StatisticsBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.ICompleteView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * 贷款大全
 */
public class CompletePresenter extends BasePresenter<ICompleteView> {
    private int pageNumber = 1;
    private int pageSize = 10;

    public CompletePresenter(Activity context, ICompleteView view) {
        super(context, view);
    }

    public void initData(int sortRule){
        getLoanSortLabelList();
        initListData(sortRule);
    }

    // 初始化产品列表
    public void initListData(int sortRule) {
        pageNumber = 1;
        getData(pageNumber, pageSize, sortRule);
    }

    // 贷款大全排序标签
    private void getLoanSortLabelList() {
        mModel.getLoanSortLabelList(new HashMap<String, Object>(), new DisposableObserver<CompleteBean>() {
            @Override
            public void onNext(CompleteBean completeBean) {
                if (parse(mContext, completeBean)) {
                    List<CompleteBean.ProductSortLabel> listBean = completeBean.getProdSortLabelList();
                    if (listBean != null && listBean.size() > 0) {
                        obtainView().getProductSortLabelList(listBean);
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

    private void getData(final int number, final int pageSize, int sortRule) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", number);
        params.put("pageSize", pageSize);
        params.put("sortRule", sortRule);
        mModel.getLoanProductList(params, new DisposableObserver<CompleteBean>() {
            @Override
            public void onNext(CompleteBean o) {
                if (parse(mContext, o)) {
                    List<CompleteBean.ProductBean> listBean = o.getProductList();
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
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
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
    public void loadMoreData(int sortRule) {
        getData(pageNumber, pageSize, sortRule);
    }


    /**
     * 产品点击统计
     */
    public void recordProduct(String productId, String moduleName, final String link) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("moduleName", moduleName);

        mModel.toStatisticClickProduct(params, new DisposableObserver<StatisticsBean>() {


            @Override
            public void onNext(StatisticsBean statisticsBean) {
                if (parse(mContext, statisticsBean)) {
                    obtainView().onRecordSuccess(link, statisticsBean.id);
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
