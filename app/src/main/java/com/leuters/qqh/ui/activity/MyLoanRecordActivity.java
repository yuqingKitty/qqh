package com.leuters.qqh.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.MyLoanRecordBean;
import com.leuters.qqh.presenter.MyLoanRecordPresenter;
import com.leuters.qqh.ui.adapter.MyLoanRecommendProductAdapter;
import com.leuters.qqh.ui.adapter.MyLoanRecordListAdapter;
import com.leuters.qqh.ui.base.BaseActivity;
import com.leuters.qqh.ui.customview.TopBarView;
import com.leuters.qqh.view.IMyLoanRecordView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyLoanRecordActivity extends BaseActivity<MyLoanRecordPresenter> implements IMyLoanRecordView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.ll_loan_empty)
    LinearLayout ll_loan_empty;
    @BindView(R.id.refresh_loan_record)
    SwipeRefreshLayout refresh_loan_record;
    @BindView(R.id.rv_loan_record)
    RecyclerView rv_loan_record;
    @BindView(R.id.rv_recommend_product)
    RecyclerView rv_recommend_product;

    private MyLoanRecordListAdapter myLoanRecordListAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new MyLoanRecordPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_loan_record;
    }

    @Override
    protected void initView() {
        ((TopBarView) findViewById(R.id.top_view)).setTitleBold();
        myLoanRecordListAdapter = new MyLoanRecordListAdapter(this, new ArrayList<MyLoanRecordBean.MyLoanBean>());
        rv_loan_record.setLayoutManager(new LinearLayoutManager(this));
        rv_loan_record.setAdapter(myLoanRecordListAdapter);

        myLoanRecordListAdapter.setOnLoadMoreListener(this, rv_loan_record);
        refresh_loan_record.setOnRefreshListener(this);
        refresh_loan_record.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mPresenter.initData();
    }

    @Override
    public void loadLoanRecordDataSuccess(List<MyLoanRecordBean.MyLoanBean> data) {
        myLoanRecordListAdapter.addData(data);
        refresh_loan_record.setRefreshing(false);
    }

    @Override
    public void refreshLoanRecordDataSuccess(List<MyLoanRecordBean.MyLoanBean> data) {
        myLoanRecordListAdapter.setNewData(data);
        refresh_loan_record.setRefreshing(false);
    }

    @Override
    public void noLoanRecordMordData() {
        myLoanRecordListAdapter.loadMoreEnd();
        refresh_loan_record.setRefreshing(false);
    }

    @Override
    public void loadLoanRecordMordFail() {
        myLoanRecordListAdapter.loadMoreFail();
        refresh_loan_record.setRefreshing(false);
    }

    @Override
    public void clearLoanRecordData() {
        myLoanRecordListAdapter.loadMoreComplete();
        refresh_loan_record.setRefreshing(false);
        ll_loan_empty.setVisibility(View.VISIBLE);
        refresh_loan_record.setVisibility(View.GONE);
    }

    @Override
    public void loadRecommendProductDataSuccess(List<MyLoanRecordBean.MyRecommendProductBean> data) {
        List<MyLoanRecordBean.MyRecommendProductBean> myRecommendProductBeanList = new ArrayList<>();
        myRecommendProductBeanList.add(data.get(0));
        myRecommendProductBeanList.add(data.get(1));
        MyLoanRecommendProductAdapter myLoanRecommendProductAdapter = new MyLoanRecommendProductAdapter(this, myRecommendProductBeanList);
        rv_recommend_product.setLayoutManager(new LinearLayoutManager(this));
        rv_recommend_product.setAdapter(myLoanRecommendProductAdapter);
    }

    @Override
    public void onRecordSuccess(String url, String id) {

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
        mPresenter.loadMoreData();
    }

    @Override
    public void onRefresh() {
        mPresenter.initLoanRecordListData();
    }

}