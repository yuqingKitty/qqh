package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.data.entity.HomeTypeProductBean;
import com.zdjf.qqh.data.entity.StatisticsBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IHomeTypeProductView;

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

    public void initData(int type){
        initTypeProductListData(type);
        getHomeTypeAdList(type);
    }

    /**
     * 初始化产品列表数据
     */
    public void initTypeProductListData(int type) {
        pageNumber = 1;
        getHomeTypeProductList(type, pageNumber, pageSize);
    }

    /**
     * 获取借款记录数据
     */
    public void getHomeTypeProductList(int type, final int number, final int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("typeId", type);
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
    public void loadMoreData(int type) {
        getHomeTypeProductList(type, pageNumber, pageSize);
    }

    /**
     * 获取广告位
     */
    public void getHomeTypeAdList(int typeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("adType", 5);
        params.put("adKind", 1);
        params.put("typeId", typeId);
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
