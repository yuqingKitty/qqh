package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.ProtocolBean;
import com.zdjf.qqh.presenter.ProtocolPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.utils.RegexUtil;
import com.zdjf.qqh.view.IProtocolView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

import static com.zdjf.qqh.data.commons.Constants.CMSTYPE_INTENT_KEY;
import static com.zdjf.qqh.data.commons.Constants.TITLE_INTENT_KEY;

/**
 * 协议
 */
public class ProtocolActivity extends BaseActivity<ProtocolPresenter> implements IProtocolView {
    private String type;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.top_view)
    TopBarView mTopBarView;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ProtocolPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        mWebView.setWebViewClient(viewClient);
        mWebView.setWebChromeClient(chromeClient);
        type = getIntent().getStringExtra(CMSTYPE_INTENT_KEY);
        mTitle = getIntent().getStringExtra(TITLE_INTENT_KEY);
        mPresenter.getProtocol(type);
    }

    private WebViewClient viewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
            LogUtil.e(url, "onPageStarted");
            mLoading.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.e(url, "onPageFinished");
            mLoading.dismissLoading();
        }
    };

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title) && !RegexUtil.isURL(title)) {
                mTopBarView.setTitleContent(title);
            } else {
                mTopBarView.setTitleContent(mTitle);
            }
        }
    };

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
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void cmsSuccess(ProtocolBean bean) {
        String link = bean.getBody();
        if (TextUtils.isEmpty(link)) {
            return;
        } else if (RegexUtil.isURL(link)) {
            mWebView.loadUrl(link);
        } else {
            mWebView.loadDataWithBaseURL(null, link, "text/html", "UTF-8", null);
        }
    }

    @Override
    public void cmsError() {
        finish();
    }

    private String pageTitle = "协议";

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
