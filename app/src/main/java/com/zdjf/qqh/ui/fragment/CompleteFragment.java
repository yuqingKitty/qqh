package com.zdjf.qqh.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.CompleteProductBean;
import com.zdjf.qqh.presenter.CompletePresenter;
import com.zdjf.qqh.ui.adapter.CompleteAdapter;
import com.zdjf.qqh.ui.base.BaseFragment;
import com.zdjf.qqh.ui.customview.CompleteDecoration;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.view.ICompleteView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * 贷款大全
 */
public class CompleteFragment extends BaseFragment<CompletePresenter> implements ICompleteView, BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.top_view)
    TopBarView mTopView;
    @BindView(R.id.rb_comprehensive_sort)
    RadioButton rb_comprehensive_sort;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.product_list)
    RecyclerView mRecycleView;

    private CompleteAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new CompletePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTopView.setTitleBold();
        mAdapter = new CompleteAdapter(mActivity, new ArrayList<CompleteProductBean.ProductBean>());
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycleView);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(mActivity.getResources().getColor(R.color.colorPrimary));
        mPresenter.initListData();

        rb_comprehensive_sort.setChecked(true);
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
        mPresenter.recordProduct(bean.id, bean.link, Constants.moduleName.Complete.getName(), "");
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

    @OnCheckedChanged({R.id.rb_comprehensive_sort, R.id.rb_interest_sort, R.id.rb_amount_sort})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_comprehensive_sort:
                    mPresenter.initListData();
                    break;
                case R.id.rb_interest_sort:
                    mPresenter.initListData();
                    break;
                case R.id.rb_amount_sort:
                    mPresenter.initListData();
                    break;
            }
        }
    }

}
