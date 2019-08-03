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
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class HomePresenter extends BasePresenter<IHomeView> {
    private int pageNumber = 1;
    private int pageSize = 10;

    public HomePresenter(Activity context, IHomeView view) {
        super(context, view);
    }

    /**
     * 加载头部数据
     */
    public void loadHeadData() {
        obtainView().showLoading();
        getHeadData();
    }

    /**
     * 获取头部信息
     */
    private void getHeadData() {
        mModel.getHomeData(new HashMap<String, Object>(), new DisposableObserver<HomeBean>() {
            @Override
            public void onNext(HomeBean homeBean) {
                if (parse(mContext, homeBean)) {
                    obtainView().fillHeadData(homeBean);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().onHeadRefreshFinish();
                obtainView().showErrorView("");
            }

            @Override
            public void onComplete() {
                obtainView().onHeadRefreshFinish();
                obtainView().hideLoading();
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initProductListData() {
        pageNumber = 1;
        getHomeProductList(pageNumber, pageSize);
    }

    /**
     * 加载更多
     */
    public void loadProductMoreData() {
        getHomeProductList(pageNumber, pageSize);
    }

    /**
     * 加载列表
     */
    private void getHomeProductList(final int number, final int pageSize) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", number);
        params.put("pageSize", pageSize);
        mModel.getHomeProductList(params, new DisposableObserver<HomeBean>() {

            @Override
            public void onNext(HomeBean homeBean) {
                if (parse(mContext, homeBean)) {
                    List<HomeBean.ProductBean> listBean = homeBean.getProductList();
                    if (listBean != null && listBean.size() > 0) {
                        if (pageNumber == 1) {
                            //刷新完成
                            obtainView().refreshProductDataSuccess(listBean);
                        } else {
                            obtainView().loadProductDataSuccess(listBean);
                        }
                        if (listBean.size() < pageSize) {
                            obtainView().noMoreProductData();
                        }
                        pageNumber += 1;
                    } else if (pageNumber == 1) {
                        obtainView().clearProductData();
                    } else {
                        obtainView().noMoreProductData();
                    }
                } else {
                    obtainView().loadMordProductFail();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().loadMordProductFail();
                obtainView().showErrorView("");
            }

            @Override
            public void onComplete() {
                obtainView().getHomeProductListFinish();
            }
        });
    }

    /**
     * 产品点击统计
     */
    public void recordProduct(String productId, final String url, String module_name, String module_order) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
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
