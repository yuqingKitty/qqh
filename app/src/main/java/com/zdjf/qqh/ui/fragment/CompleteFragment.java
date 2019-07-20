package com.zdjf.qqh.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.CompleteProductBean;
import com.zdjf.qqh.presenter.CompletePresenter;
import com.zdjf.qqh.ui.adapter.CompleteAdapter;
import com.zdjf.qqh.ui.base.BaseFragment;
import com.zdjf.qqh.ui.customview.CompleteDecoration;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.view.ICompleteView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 贷款大全fenzhi
 */
public class CompleteFragment extends BaseFragment<CompletePresenter> implements ICompleteView, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.product_list)
    RecyclerView mRecycleView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CompleteAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new CompletePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mAdapter = new CompleteAdapter(mActivity, new ArrayList<CompleteProductBean.ProductBean>());
        mRecycleView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecycleView.addItemDecoration(new CompleteDecoration(2, 14, 20));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycleView);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(mActivity.getResources().getColor(R.color.colorPrimary));
        mPresenter.initListData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete;
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //点击某一项
        CompleteProductBean.ProductBean bean = (CompleteProductBean.ProductBean) adapter.getData().get(position);
        mPresenter.recordProduct(bean.getId(), bean.getLink(), Constants.moduleName.Complete.getName(), "");
    }

    @Override
    public void loadDataSuccess(List<CompleteProductBean.ProductBean> data) {
        mAdapter.addData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshDataSuccess(List<CompleteProductBean.ProductBean> data) {
        mAdapter.setNewData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noMordData() {
        mAdapter.loadMoreEnd();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMordFail() {
        mAdapter.loadMoreFail();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clearData() {
        mAdapter.loadMoreComplete();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRecordSuccess(String url, String id) {
        IntentUtil.toAppWebView(mActivity, url, id);
    }

    @Override
    public void onRefresh() {
        mPresenter.initListData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMoreData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mActivity != null) {
            mActivity.initStatusBar(true);
        }
    }
}
