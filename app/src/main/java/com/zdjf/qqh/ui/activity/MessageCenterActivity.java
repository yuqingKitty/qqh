package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.MessageCenterBean;
import com.zdjf.qqh.presenter.MessageCenterPresenter;
import com.zdjf.qqh.ui.adapter.MessageCenterAdapter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.view.IMessageCenterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity<MessageCenterPresenter> implements IMessageCenterView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.ll_message_empty)
    LinearLayout ll_message_empty;
    @BindView(R.id.refresh_message_layout)
    SwipeRefreshLayout refresh_message_layout;
    @BindView(R.id.rv_message_list)
    RecyclerView rv_message_list;

    private MessageCenterAdapter messageCenterAdapter;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new MessageCenterPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        ((TopBarView) findViewById(R.id.top_view)).setTitleBold();
        messageCenterAdapter = new MessageCenterAdapter(this, new ArrayList<MessageCenterBean.MessageBean>());
        rv_message_list.setLayoutManager(new LinearLayoutManager(this));
        rv_message_list.setAdapter(messageCenterAdapter);
        messageCenterAdapter.setOnLoadMoreListener(this, rv_message_list);
        refresh_message_layout.setOnRefreshListener(this);
        refresh_message_layout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mPresenter.initMessageListData();
    }

    @Override
    public void loadDataSuccess(List<MessageCenterBean.MessageBean> data) {
        messageCenterAdapter.addData(data);
        refresh_message_layout.setRefreshing(false);
    }

    @Override
    public void refreshDataSuccess(List<MessageCenterBean.MessageBean> data) {
        messageCenterAdapter.setNewData(data);
        refresh_message_layout.setRefreshing(false);
    }

    @Override
    public void noMordData() {
        messageCenterAdapter.loadMoreEnd();
        refresh_message_layout.setRefreshing(false);
    }

    @Override
    public void loadMordFail() {
        messageCenterAdapter.loadMoreFail();
        refresh_message_layout.setRefreshing(false);
    }

    @Override
    public void clearData() {
        messageCenterAdapter.loadMoreComplete();
        refresh_message_layout.setRefreshing(false);
        ll_message_empty.setVisibility(View.VISIBLE);
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
        mPresenter.initMessageListData();
    }

}
