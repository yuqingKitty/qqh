package com.zdjf.qqh.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.zdjf.qqh.R;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.UploadBean;
import com.zdjf.qqh.presenter.HomePresenter;
import com.zdjf.qqh.ui.activity.MainActivity;
import com.zdjf.qqh.ui.adapter.HomeLoanProductListAdapter;
import com.zdjf.qqh.ui.base.BaseFragment;
import com.zdjf.qqh.ui.customview.HomeRvHeaderView;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.updateapp.UpdateDialogFragment;
import com.zdjf.qqh.view.IHomeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zdjf.qqh.utils.updateapp.UpdateDialogFragment.INTENT_KEY;

/**
 * @Description: 首页fragment
 * @Author: Young
 * @Time: 2018/6/27 14:31
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView<HomeBean>, BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, HomeRvHeaderView.ClickHomeHeadListener, OnBannerListener {
    @BindView(R.id.top_view)
    TopBarView mTopView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.home_rv)
    RecyclerView mHomeRecycleView;

    private HomeRvHeaderView mHeaderView;
    private HomeLoanProductListAdapter homeLoanProductListAdapter;

    private int[] pageList;
    private ArrayMap<String, List<HomeBean.ProductBean>> mDataMap;
    private int tabPosition = 0;
    private int pageSize = 10;

    private HomeBean mHomeBean;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTopView.setTitleBold();
        mPresenter.loadData();
        mPresenter.getSysUpdated();
        mHeaderView = new HomeRvHeaderView(mActivity);
        mDataMap = new ArrayMap<>();
        mHeaderView.setListener( this, this);
        //设置刷新监听
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(mActivity.getResources().getColor(R.color.colorPrimary));

        homeLoanProductListAdapter = new HomeLoanProductListAdapter(mActivity, new ArrayList<HomeBean.ProductBean>());
        homeLoanProductListAdapter.addHeaderView(mHeaderView);
        mHomeRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mHomeRecycleView.setAdapter(homeLoanProductListAdapter);

        homeLoanProductListAdapter.setOnLoadMoreListener(this, mHomeRecycleView);
        mHomeRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onRecommendProductClick(((HomeBean.ProductBean) adapter.getData().get(position)).getId()
                        , ((HomeBean.ProductBean) adapter.getData().get(position)).getLink(), Constants.moduleName.TypeProduct.getName(), -1);
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
        if (!hidden && mActivity != null) {
            mActivity.initStatusBar(true);
            if (mHeaderView != null) {
                mHeaderView.startScroll();
            }
        } else if (hidden && mHeaderView != null) {
            mHeaderView.stopScroll();
        }
    }

    @Override
    public void fillData(HomeBean data) {
        mHomeBean = data;
        mHeaderView.setData(data.getAdList(), data.getNoticeList(), data.getTypePOs(), data.getProRecommendVOs());
        if (data.getTypePOs() != null && data.getTypePOs().size() > tabPosition) {
            pageList = new int[data.getTypePOs().size()];
            mPresenter.loadList(data.getTypePOs().get(tabPosition).getId(), pageList[tabPosition], pageSize);
        }
    }

    @Override
    public void fillList(HomeBean data) {
        mDataMap.put(mHomeBean.getTypePOs().get(tabPosition).getId(), data.getProductVOs());
        homeLoanProductListAdapter.setNewData(mDataMap.get(mHomeBean.getTypePOs().get(tabPosition).getId()));
        pageList[tabPosition] += 1;
        if (data.getProductVOs().size() < pageSize) {
            homeLoanProductListAdapter.loadMoreEnd();
            mPresenter.simpleRecord("", Constants.moduleName.ScrollBottom.getName(), "");
        } else {
            homeLoanProductListAdapter.loadMoreComplete();
        }
    }


    @Override
    public void getHeadFinish() {

    }

    @Override
    public void getListFinish() {
        if (mLoading != null) {
            mLoading.dismissLoading();
        }
    }

    @Override
    public void onRefreshFinish() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getListFailed() {
        List<HomeBean.ProductBean> produces = mDataMap.get(mHomeBean.getTypePOs().get(tabPosition).getId());
        if (produces == null) {
            produces = new ArrayList<>();
            homeLoanProductListAdapter.setNewData(produces);
        } else {
            homeLoanProductListAdapter.setNewData(produces);
        }
    }

    @Override
    public void onRefreshData(HomeBean data) {
        mDataMap.clear();
        fillData(data);
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
        if (mHomeBean != null && mHomeBean.getTypePOs() != null) {
            mPresenter.loadMore(mHomeBean.getTypePOs().get(tabPosition).getId(), pageList[tabPosition], pageSize);
        }
    }

    @Override
    public void appendMoreDataToView(HomeBean data) {
        homeLoanProductListAdapter.addData(data.getProductVOs());
        pageList[tabPosition] += 1;
        if (data.getProductVOs().size() < pageSize) {
            homeLoanProductListAdapter.loadMoreEnd();
            mPresenter.simpleRecord("", Constants.moduleName.ScrollBottom.getName(), "");
        } else {
            homeLoanProductListAdapter.loadMoreComplete();
        }
    }

    @Override
    public void hasNoMoreData() {
        homeLoanProductListAdapter.loadMoreEnd();
        mPresenter.simpleRecord("", Constants.moduleName.ScrollBottom.getName(), "");
    }

    @Override
    public void noListData() {
        homeLoanProductListAdapter.loadMoreEnd();
    }

    @Override
    public void showErrorView(String throwable) {
        homeLoanProductListAdapter.loadMoreFail();
    }

    @OnClick(R.id.top_view)
    void topClick() {
        mHomeRecycleView.smoothScrollToPosition(0);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @Override
    public void onTypeClicked(String title, int type) {
        IntentUtil.toHomeTypeProductActivity(getActivity(), title, type);
    }

    @Override
    public void onLoanAllClicked() {
        // 点击了热门贷款全部
        ((MainActivity)getActivity()).setFragment(1);
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
            if (mHomeBean != null && mHomeBean.getAdList() != null
                    && mHomeBean.getAdList().size() > position) {
                if (!TextUtils.isEmpty(mHomeBean.getAdList().get(position).getSrcURL())) {
                    IntentUtil.toAppWebView(mActivity, mHomeBean.getAdList().get(position).getSrcURL(), "");
                    mPresenter.simpleRecord("", Constants.moduleName.Banner.getName(), "");
                }
            }
        }
    }
}
