package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.StatisticsBean;
import com.zdjf.qqh.data.entity.UploadBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.APKVersionCodeUtil;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IHomeView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class HomePresenter extends BasePresenter<IHomeView> {

    public HomePresenter(Activity context, IHomeView view) {
        super(context, view);
    }

    /**
     * 加载数据
     */
    public void loadData() {
        obtainView().showLoading();
        getHeadData(false);
    }

    /**
     * 加载列表
     *
     * @param typeId   产品id
     * @param pageNo   页数
     * @param pageSize 大小
     */
    public void loadList(String typeId, int pageNo, int pageSize) {
        obtainView().showLoading();
        HashMap<String, Object> params = new HashMap<>();
        params.put("typeId", typeId);
        params.put("pageNo", pageNo + 1);
        params.put("pageSize", pageSize);
        mModel.getProductList(params, new DisposableObserver<HomeBean>() {

            @Override
            public void onNext(HomeBean homeBean) {
                if (parse(mContext, homeBean)) {
                    if (homeBean.getProductVOs().size() == 0) {
                        obtainView().noListData();
                    } else {
                        obtainView().fillList(homeBean);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().getListFailed();
                obtainView().showErrorView("");
            }

            @Override
            public void onComplete() {
                obtainView().getListFinish();
            }
        });
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getHeadData(true);
    }

    /**
     * 获取头部信息
     *
     * @param isRefresh
     */
    private void getHeadData(final boolean isRefresh) {
        mModel.getHomeData(new HashMap<String, Object>(), new DisposableObserver<HomeBean>() {
            @Override
            public void onNext(HomeBean homeBean) {
                if (parse(mContext, homeBean)) {
                    if (isRefresh) {
                        obtainView().onRefreshData(homeBean);
                    } else {
                        obtainView().fillData(homeBean);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().getHeadFinish();
                obtainView().onRefreshFinish();
                obtainView().showErrorView("");
            }

            @Override
            public void onComplete() {
                obtainView().getHeadFinish();
                obtainView().onRefreshFinish();
                obtainView().hideLoading();
            }
        });
    }

    public void loadMore(final String typeId, final int pageNo, final int pageSize) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("typeId", typeId);
        params.put("pageNo", pageNo + 1);
        params.put("pageSize", pageSize);
        mModel.getProductList(params, new DisposableObserver<HomeBean>() {

            @Override
            public void onNext(HomeBean homeBean) {
                if (parse(mContext, homeBean)) {
                    if (homeBean.getProductVOs() == null || homeBean.getProductVOs().size() == 0) {
                        obtainView().hasNoMoreData();
                    } else {
                        obtainView().appendMoreDataToView(homeBean);
                    }
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
                obtainView().getListFinish();
            }
        });
    }

    /**
     * 产品点击统计
     */
    public void recordProduct(String productId, final String url, String module_name, String module_order) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", BaseApplication.getUserId(mContext));
        params.put("token", BaseApplication.getToken(mContext));
        params.put("productId", productId);
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

    /**
     * 版本更新
     */
    public void getSysUpdated() {
        final Map<String, Object> params = new HashMap<>();
        params.put("pubSysType", "ANDROID");
        params.put("pubVersion", APKVersionCodeUtil.getVerName(mContext));

        mModel.sysUpdated(params, new DisposableObserver<UploadBean>() {

            @Override
            public void onNext(UploadBean updateBean) {
                if (parse(mContext, updateBean)) {
                    if (updateBean.getSysNoticeVO() != null) {
                        obtainView().checkUpdate(updateBean.getSysNoticeVO().getSrcURL(), updateBean.getAppEnForce(), updateBean.getSysNoticeVO().getTitle(), updateBean.getSysNoticeVO().getName());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
