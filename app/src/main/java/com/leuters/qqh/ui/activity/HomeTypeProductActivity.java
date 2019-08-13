package com.leuters.qqh.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.listener.OnBannerListener;
import com.leuters.qqh.R;
import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.commons.Constants;
import com.leuters.qqh.data.entity.HomeTypeProductBean;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.presenter.HomeTypeProductPresenter;
import com.leuters.qqh.ui.adapter.HomeTypeProductListAdapter;
import com.leuters.qqh.ui.base.BaseActivity;
import com.leuters.qqh.ui.customview.HomeTypeAdHeader;
import com.leuters.qqh.ui.customview.TopBarView;
import com.leuters.qqh.utils.IntentUtil;
import com.leuters.qqh.utils.rxbus.RxBus;
import com.leuters.qqh.view.IHomeTypeProductView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.leuters.qqh.data.commons.Constants.EXTRA_TYPE_ID;
import static com.leuters.qqh.data.commons.Constants.RXBUS_TO_COMPLETE_KEY;
import static com.leuters.qqh.data.commons.Constants.TITLE_INTENT_KEY;

public class HomeTypeProductActivity  extends BaseActivity<HomeTypeProductPresenter> implements IHomeTypeProductView, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, HomeTypeAdHeader.ClickTypeAllListener, OnBannerListener {
    @BindView(R.id.refresh_home_type_product)
    SwipeRefreshLayout refresh_home_type_product;
    @BindView(R.id.rv_home_type_product)
    RecyclerView rv_home_type_product;

    private HomeTypeAdHeader homeTypeAdHeader;
    private HomeTypeProductListAdapter homeTypeProductListAdapter;
    private List<HomeTypeProductBean.BannerBean> bannerBeanList = new ArrayList<>();
    private String typeId;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new HomeTypeProductPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_home_type_product;
    }

    @Override
    protected void initView() {
        if (getIntent() != null){
            String title = getIntent().getStringExtra(TITLE_INTENT_KEY);
            typeId = getIntent().getStringExtra(EXTRA_TYPE_ID);
            ((TopBarView) findViewById(R.id.top_view)).setTitleContent(title);
        }
        ((TopBarView) findViewById(R.id.top_view)).setTitleBold();
        homeTypeAdHeader = new HomeTypeAdHeader(this);

        homeTypeProductListAdapter = new HomeTypeProductListAdapter(this, new ArrayList<HomeTypeProductBean.TypeProductBean>());
        homeTypeProductListAdapter.addHeaderView(homeTypeAdHeader);
        rv_home_type_product.setLayoutManager(new LinearLayoutManager(this));
        rv_home_type_product.setAdapter(homeTypeProductListAdapter);
        homeTypeProductListAdapter.setOnItemClickListener(this);

        homeTypeProductListAdapter.setOnLoadMoreListener(this, rv_home_type_product);
        refresh_home_type_product.setOnRefreshListener(this);
        refresh_home_type_product.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        homeTypeAdHeader.setListener(this, this);
        mPresenter.initData(typeId);
        if (homeTypeAdHeader != null) {
            homeTypeAdHeader.startScroll();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //点击某一项
        HomeTypeProductBean.TypeProductBean bean = (HomeTypeProductBean.TypeProductBean) adapter.getData().get(position);
        mPresenter.recordProduct(bean.id, Constants.moduleName.PROD_TYPE.getName(), bean.link);
    }

    @Override
    public void loadTypeProductDataSuccess(List<HomeTypeProductBean.TypeProductBean> data) {
        homeTypeProductListAdapter.addData(data);
        refresh_home_type_product.setRefreshing(false);
    }

    @Override
    public void refreshTypeProductDataSuccess(List<HomeTypeProductBean.TypeProductBean> data) {
        homeTypeProductListAdapter.setNewData(data);
        refresh_home_type_product.setRefreshing(false);
    }

    @Override
    public void noTypeProductMordData() {
        homeTypeProductListAdapter.loadMoreEnd();
        refresh_home_type_product.setRefreshing(false);
    }

    @Override
    public void loadTypeProductMordFail() {
        homeTypeProductListAdapter.loadMoreFail();
        refresh_home_type_product.setRefreshing(false);
    }

    @Override
    public void clearTypeProductData() {
        homeTypeProductListAdapter.loadMoreComplete();
        refresh_home_type_product.setRefreshing(false);
    }

    @Override
    public void loadTypeAdDataSuccess(List<HomeTypeProductBean.BannerBean> data) {
        bannerBeanList.clear();
        bannerBeanList.addAll(data);
        homeTypeAdHeader.setHeadData(bannerBeanList);
    }

    @Override
    public void onRecordSuccess(String url, String id) {
        IntentUtil.toAppWebView(this, url, id);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.dismissLoading();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMoreData(typeId);
    }

    @Override
    public void onRefresh() {
        mPresenter.initTypeProductListData(typeId);
    }

    @Override
    public void OnBannerClick(int position) {
        if (BaseApplication.isLogin(this, true, false)) {
            if (bannerBeanList!= null && bannerBeanList.size() > position) {
                if (!TextUtils.isEmpty(bannerBeanList.get(position).srcURL)) {
                    mPresenter.recordProduct(bannerBeanList.get(position).productId, Constants.moduleName.PROD_TYPE_AD.getName(), bannerBeanList.get(position).srcURL);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeTypeAdHeader.stopScroll();
    }

    @Override
    public void onTypeAllClicked() {
        RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_TO_COMPLETE_KEY));
        finish();
    }

}
