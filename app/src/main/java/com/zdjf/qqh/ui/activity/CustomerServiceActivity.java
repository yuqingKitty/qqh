package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.ServiceBean;
import com.zdjf.qqh.presenter.ServicePresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.zdjf.qqh.view.IServiceView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 专属客服
 */
public class CustomerServiceActivity extends BaseActivity<ServicePresenter> implements IServiceView<ServiceBean> {
    @BindView(R.id.content_layout)
    LinearLayout mContentLayout;
    @BindView(R.id.tv_service_name)
    TextView tv_service_name;
    @BindView(R.id.tv_service_number)
    TextView tv_service_number;
    @BindView(R.id.tv_service_level)
    TextView tv_service_level;
    @BindView(R.id.iv_qr_code)
    ImageView iv_qr_code;
    @BindView(R.id.tv_weixin)
    TextView tv_weixin;
    @BindView(R.id.view_error)
    AutoLinearLayout mNotNetLayout;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ServicePresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_custom_service;
    }

    @Override
    protected void initView() {
        ((TopBarView)findViewById(R.id.top_view)).setTitleBold();
        mPresenter.getData();
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
    public void getDataSuccess(ServiceBean bean) {
        tv_service_name.setText(bean.name);
        tv_service_number.setText("工号：" + bean.workNumber);
        tv_service_level.setText("职位：" + bean.level);
        tv_weixin.setText("微信号：" + bean.weixin);
        GlideImageLoader.setImg(this, bean.qrCode, iv_qr_code, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
        mContentLayout.setVisibility(View.VISIBLE);
        mNotNetLayout.setVisibility(View.GONE);

    }

    @Override

    public void getDataFailed() {
        mContentLayout.setVisibility(View.GONE);
        mNotNetLayout.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.view_error)
    void noNetworkClick() {
        mPresenter.getData();
    }

    private String pageTitle = "专属客服";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(pageTitle);
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(pageTitle);
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }
}
