package com.leuters.qqh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leuters.qqh.R;
import com.leuters.qqh.presenter.WebViewPresenter;
import com.leuters.qqh.ui.base.BaseActivity;
import com.leuters.qqh.ui.customview.TopBarView;
import com.leuters.qqh.view.IWebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

import static com.leuters.qqh.utils.IntentUtil.INTENT_KEY;
import static com.leuters.qqh.utils.IntentUtil.STATISTICS_ID;

public class WebViewActivity extends BaseActivity<WebViewPresenter> implements IWebView {
    @BindView(R.id.top_view)
    TopBarView top_view;
    @BindView(R.id.webview)
    WebView mWebView;

    private String loadUrl = "";
    private String statisticsId = "";

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new WebViewPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        loadUrl = getIntent().getStringExtra(INTENT_KEY);
        statisticsId = getIntent().getStringExtra(STATISTICS_ID);

        initWebView();
        mWebView.loadUrl(loadUrl);
    }

    private void initWebView() {
        mWebView.getSettings().setDefaultFontSize(16);
        mWebView.getSettings().setTextZoom(100);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置可以使用localStorage
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString());
        mWebView.getSettings().setDatabaseEnabled(true);  // 应用可以有数据库
        mWebView.getSettings().setAppCacheEnabled(true);  // 应用可以有缓存
        mWebView.setDownloadListener(new MyDownLoadListener(this));  // 下载响应

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                top_view.setTitleContent(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            /**
             * 通知主机应用程序加载资源时发生SSL错误。它要执行proceed函数
             * @param view
             * @param handler
             * @param error
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
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
        if (!TextUtils.isEmpty(statisticsId)) {
            mPresenter.stayTime(statisticsId);
        }
        MobclickAgent.onPageEnd(pageTitle);
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onDestroy() {
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


    class MyDownLoadListener implements DownloadListener {
        private Context context;

        public MyDownLoadListener(Context context) {
            this.context = context;
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

}
