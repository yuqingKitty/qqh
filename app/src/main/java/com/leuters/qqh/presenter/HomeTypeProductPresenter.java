package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.data.entity.HomeTypeProductBean;
import com.leuters.qqh.data.entity.StatisticsBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.IHomeTypeProductView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class HomeTypeProductPresenter extends BasePresenter<IHomeTypeProductView> {
    private int pageNumber = 1;
    private int pageSize = 10;

    public HomeTypeProductPresenter(Activity context, IHomeTypeProductView view) {
        super(context, view);
    }

    public void initData(String typeId){
        initTypeProductListData(typeId);
        getHomeTypeAdList();
    }

    /**
     * 初始化产品列表数据
     */
    public void initTypeProductListData(String typeId) {
        pageNumber = 1;
        getHomeTypeProductList(typeId, pageNumber, pageSize);
    }

    /**
     * 获取借款记录数据
     */
    public void getHomeTypeProductList(String typeId, final int number, final int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("typeId", typeId);
        params.put("pageNo", number);
        params.put("pageSize", pageSize);
        mModel.getHomeTypeProductList(params, new DisposableObserver<HomeTypeProductBean>() {
            @Override
            public void onNext(HomeTypeProductBean homeTypeProductBean) {
                if (parse(mContext, homeTypeProductBean)) {
                    List<HomeTypeProductBean.TypeProductBean> listBean = homeTypeProductBean.productList;
                    if (listBean != null && listBean.size() > 0) {
                        if (pageNumber == 1) {
                            //刷新完成
                            obtainView().refreshTypeProductDataSuccess(listBean);
                        } else {
                            obtainView().loadTypeProductDataSuccess(listBean);
                        }
                        if (listBean.size() < pageSize) {
                            obtainView().noTypeProductMordData();
                        }
                        pageNumber += 1;
                    } else if (pageNumber == 1) {
                        obtainView().clearTypeProductData();
                    } else {
                        obtainView().noTypeProductMordData();
                    }
                } else {
                    obtainView().loadTypeProductMordFail();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(throwable.toString());
                obtainView().ShowToast("网络异常");
                obtainView().loadTypeProductMordFail();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMoreData(String typeId) {
        getHomeTypeProductList(typeId, pageNumber, pageSize);
    }

    /**
     * 获取广告位
     */
    public void getHomeTypeAdList() {
        Map<String, Object> params = new HashMap<>();
        params.put("adType", 6);
        mModel.getHomeTypeAdList(params, new DisposableObserver<HomeTypeProductBean>() {
            @Override
            public void onNext(HomeTypeProductBean homeTypeProductBean) {
                if (parse(mContext, homeTypeProductBean)) {
                    List<HomeTypeProductBean.BannerBean> listBean = homeTypeProductBean.advertisementList;
                    if (listBean != null && listBean.size() > 0) {
                        obtainView().loadTypeAdDataSuccess(listBean);
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
