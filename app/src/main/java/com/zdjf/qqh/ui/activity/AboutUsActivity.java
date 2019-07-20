package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.AboutUsPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.APKVersionCodeUtil;
import com.zdjf.qqh.view.IBaseView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.SYSTEM_CMSTYPE;
import static com.zdjf.qqh.utils.IntentUtil.toProtocolActivity;

public class AboutUsActivity extends BaseActivity implements IBaseView {
    @BindView(R.id.app_icon)
    ImageView mAppIconView;
    @BindView(R.id.version_tv)
    TextView mVersionTv;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new AboutUsPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        mVersionTv.setText(new StringBuilder().append("版本号：V ").append(APKVersionCodeUtil.getVerName(this)));
    }

    @OnClick(R.id.company)
    void viewClick(View v) {
        switch (v.getId()) {
            case R.id.company:
                toProtocolActivity(this, SYSTEM_CMSTYPE, "公司简介");
                break;
        }
    }

    @Override
    public void ShowToast(String t) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private String pageTitle = "关于我们";

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
