package com.zdjf.qqh.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.CompleteBean;
import com.zdjf.qqh.presenter.CompletePresenter;
import com.zdjf.qqh.ui.adapter.CompleteAdapter;
import com.zdjf.qqh.ui.base.BaseFragment;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.ScreenUtil;
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
    @BindView(R.id.ll_sort_label)
    LinearLayout ll_sort_label;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.product_list)
    RecyclerView mRecycleView;

    private CompleteAdapter mAdapter;
    private int sortRule = 1;
    private List<TextView> sortLabelList;

    @Override
    protected void initPresenter() {
        mPresenter = new CompletePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTopView.setTitleBold();
        mAdapter = new CompleteAdapter(mActivity, new ArrayList<CompleteBean.ProductBean>());
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycleView);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(mActivity.getResources().getColor(R.color.colorPrimary));
        mPresenter.initData(sortRule);
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
        CompleteBean.ProductBean bean = (CompleteBean.ProductBean) adapter.getData().get(position);
        mPresenter.recordProduct(bean.id, bean.link, Constants.moduleName.Complete.getName(), "");
    }

    @Override
    public void getProductSortLabelList(List<CompleteBean.ProductSortLabel> data) {
        sortRule = 1;
        sortLabelList = new ArrayList<>();
        ll_sort_label.removeAllViews();
        for (int i = 0; i < data.size(); i++){
            final CompleteBean.ProductSortLabel productSortLabel = data.get(i);
            View sortLabelView = LayoutInflater.from(getContext()).inflate(R.layout.view_complete_sort_label, null);
            TextView tv_loan_sort_label = sortLabelView.findViewById(R.id.tv_loan_sort_label);
            tv_loan_sort_label.setWidth(ScreenUtil.dp2px(getContext(), 90));
            tv_loan_sort_label.setText(productSortLabel.name);
            if (i == 0) {
                tv_loan_sort_label.setBackgroundResource(R.drawable.loan_tag_selected_bg);
                tv_loan_sort_label.setTextColor(getContext().getResources().getColor(R.color.color_FFFFFF));
            } else {
                tv_loan_sort_label.setBackgroundResource(R.drawable.loan_tag_unselected_bg);
                tv_loan_sort_label.setTextColor(getContext().getResources().getColor(R.color.color_FFC71D));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,ScreenUtil.dp2px(getContext(), 20),0);
            sortLabelView.setLayoutParams(layoutParams);
            ll_sort_label.addView(sortLabelView);
            tv_loan_sort_label.setTag(i);
            sortLabelList.add(tv_loan_sort_label);

            tv_loan_sort_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSortLabel((int)v.getTag(), productSortLabel);
                }
            });
        }
    }

    private void resetSortLabel(int pos, CompleteBean.ProductSortLabel productSortLabel) {
        Log.e("yuq", "pos"+pos);
        for (int i = 0; i < sortLabelList.size(); i++){
            TextView textView = sortLabelList.get(i);
            if (i == pos){
                textView.setBackgroundResource(R.drawable.loan_tag_selected_bg);
                textView.setTextColor(getContext().getResources().getColor(R.color.color_FFFFFF));
            } else {
                textView.setBackgroundResource(R.drawable.loan_tag_unselected_bg);
                textView.setTextColor(getContext().getResources().getColor(R.color.color_FFC71D));
            }
        }
        sortRule = productSortLabel.sortRule;
        mPresenter.initListData(sortRule);
    }


    @Override
    public void loadDataSuccess(List<CompleteBean.ProductBean> data) {
        mAdapter.addData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshDataSuccess(List<CompleteBean.ProductBean> data) {
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
        mPresenter.initListData(sortRule);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMoreData(sortRule);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mActivity != null) {
            mActivity.initStatusBar(true);
        }
    }

}
