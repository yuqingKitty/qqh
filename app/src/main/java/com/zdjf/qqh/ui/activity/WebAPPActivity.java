package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.WebViewPresenter;
import com.zdjf.qqh.ui.base.BaseAgentWebActivity;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.AndroidBug54971Workaround;
import com.zdjf.qqh.view.IWebView;
import com.umeng.analytics.MobclickAgent;

import static com.zdjf.qqh.utils.IntentUtil.INTENT_KEY;
import static com.zdjf.qqh.utils.IntentUtil.STATISTICS_ID;


/**
 * H5界面
 * Created by Administrator on 2016/11/30.
 */

public class WebAPPActivity extends BaseAgentWebActivity implements IWebView {
    TopBarView mTopView;
    View mRootView;
    private String StatisticsId = "";

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new WebViewPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web_app;
    }

    @Override
    protected void initView() {
        mTopView = findViewById(R.id.top_view);
        StatisticsId = getIntent().getStringExtra(STATISTICS_ID);
        AndroidBug54971Workaround.assistActivity(findViewById(R.id.web_main), this);
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) findViewById(R.id.web_main);
    }

    @Override
    protected void setTitle(WebView view, String title) {
        super.setTitle(view, title);
        mTopView.setTitleContent(title);
    }

    @Override
    protected int getIndicatorColor() {
        return Color.parseColor("#3886F8");
    }

    @Override
    protected int getIndicatorHeight() {
        return 3;
    }

    @Nullable
    @Override
    public String getUrl() {
        return getIntent().getStringExtra(INTENT_KEY);
    }


    private String pageTitle = "产品Web";

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

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(StatisticsId)) {
            mPresenter.stayTime(StatisticsId);
        }
        super.onDestroy();
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
}
