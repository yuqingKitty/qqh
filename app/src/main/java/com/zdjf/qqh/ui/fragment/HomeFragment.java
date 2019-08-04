package com.zdjf.qqh.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zdjf.qqh.R;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.data.entity.UploadBean;
import com.zdjf.qqh.presenter.HomePresenter;
import com.zdjf.qqh.ui.adapter.HomeProductListAdapter;
import com.zdjf.qqh.ui.base.BaseFragment;
import com.zdjf.qqh.ui.customview.HomeHeaderView;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.utils.updateapp.UpdateDialogFragment;
import com.zdjf.qqh.view.IHomeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_TO_COMPLETE_KEY;
import static com.zdjf.qqh.utils.updateapp.UpdateDialogFragment.INTENT_KEY;

/**
 * @Description: 首页fragment
 * @Author: Young
 * @Time: 2018/6/27 14:31
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView<HomeBean>, BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, HomeHeaderView.ClickHomeHeadListener, OnBannerListener {
    @BindView(R.id.top_view)
    TopBarView mTopView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.home_rv)
    RecyclerView mHomeRecycleView;

    private HomeHeaderView mHeaderView;
    private HomeProductListAdapter homeLoanProductListAdapter;
    private List<HomeBean.BannerBean> adList;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTopView.setTitleBold();
        mPresenter.loadHeadData();
        mPresenter.initProductListData();
        mPresenter.getSysUpdated();

        //设置刷新监听
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(mActivity.getResources().getColor(R.color.colorPrimary));

        mHeaderView = new HomeHeaderView(mActivity);
        mHeaderView.setListener( this, this);

        homeLoanProductListAdapter = new HomeProductListAdapter(mActivity, new ArrayList<HomeBean.ProductBean>());
        homeLoanProductListAdapter.addHeaderView(mHeaderView);
        mHomeRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mHomeRecycleView.setAdapter(homeLoanProductListAdapter);
        homeLoanProductListAdapter.setOnLoadMoreListener(this, mHomeRecycleView);
        mHomeRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onRecommendProductClick(((HomeBean.ProductBean) adapter.getData().get(position)).id
                        , ((HomeBean.ProductBean) adapter.getData().get(position)).link, Constants.moduleName.TypeProduct.getName(), -1);
            }
        });
        homeLoanProductListAdapter.disableLoadMoreIfNotFullPage();
        View errorView = getLayoutInflater().inflate(R.layout.view_error, (ViewGroup) mHomeRecycleView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        homeLoanProductListAdapter.setEmptyView(errorView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden && mActivity != null) {
//            mActivity.initStatusBar(true);
//            if (mHeaderView != null) {
//                mHeaderView.startScroll();
//            }
//        } else if (hidden && mHeaderView != null) {
//            mHeaderView.stopScroll();
//        }
    }

    @Override
    public void fillHeadData(HomeBean data) {
        adList = data.getAdvertisementList();
        mHeaderView.setData(data.getAdvertisementList(), data.getSystemNotifyList(), data.getProdTypeList(), data.getProdRecommendList());
    }

    @Override
    public void loadProductDataSuccess(List<HomeBean.ProductBean> data) {
        homeLoanProductListAdapter.addData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshProductDataSuccess(List<HomeBean.ProductBean> data) {
        homeLoanProductListAdapter.setNewData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noMoreProductData() {
        homeLoanProductListAdapter.loadMoreEnd();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clearProductData() {
        homeLoanProductListAdapter.loadMoreComplete();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMordProductFail() {
        homeLoanProductListAdapter.loadMoreFail();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onHeadRefreshFinish() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getHomeProductListFinish() {
        if (mLoading != null) {
            mLoading.dismissLoading();
        }
    }

    @Override
    public void onRecordSuccess(String url, String id) {
        IntentUtil.toAppWebView(mActivity, url, id);
    }

    @Override
    public void checkUpdate(String srcURL, int appEnForce, String title, String name) {
        if (TextUtils.isEmpty(srcURL)) {
            return;
        }
        UploadBean uploadBean = new UploadBean(srcURL, appEnForce, title, name);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_KEY, uploadBean);
        UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(bundle);
        updateDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoading != null) {
            mLoading.dismissLoading();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadProductMoreData();
    }

    @Override
    public void showErrorView(String throwable) {
        homeLoanProductListAdapter.loadMoreFail();
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mPresenter.loadHeadData();
        mPresenter.initProductListData();
    }

    @Override
    public void onTypeClicked(String title, int typeId) {
        if (BaseApplication.isLogin(mActivity, true, false)) {
            IntentUtil.toHomeTypeProductActivity(getActivity(), title, typeId);
        }
    }

    @Override
    public void onLoanAllClicked() {
        // 点击了热门贷款全部
        RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_TO_COMPLETE_KEY));
    }

    @Override
    public void onRecommendProductClick(String productId, String url, String moduleName, int moduleOrder) {
        //记录点击的产品
        if (BaseApplication.isLogin(mActivity, true, true)) {
            mPresenter.recordProduct(productId, url, moduleName, moduleOrder + "");
        }
    }

    private String mPageName = "推荐";

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }

    /**
     * 点击banner
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        if (BaseApplication.isLogin(mActivity, true, true)) {
            if (adList != null && adList.size() > position) {
                if (!TextUtils.isEmpty(adList.get(position).srcURL)) {
                    IntentUtil.toAppWebView(mActivity, adList.get(position).srcURL, "");
                    mPresenter.simpleRecord("", Constants.moduleName.Banner.getName(), "");
                }
            }
        }
    }
}
