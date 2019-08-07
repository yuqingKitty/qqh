package com.leuters.qqh.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.leuters.qqh.R;
import com.leuters.qqh.presenter.WebViewPresenter;
import com.leuters.qqh.ui.base.BaseActivity;
import com.leuters.qqh.ui.customview.TopBarView;
import com.leuters.qqh.utils.AndroidBug54971Workaround;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.IWebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * H5界面
 * Created by Administrator on 2016/11/30.
 */

public class WebActivity extends BaseActivity<WebViewPresenter> implements IWebView {
    private Context mContext;
    @BindView(R.id.top_view)
    TopBarView mTopView;
    @BindView(R.id.web_view)
    WebView mWebView;
    private String url = "";
    private String StatisticsId = "";

    /**
     * 加载进度条
     */
    private ProgressBar mLoadBar;
    private boolean isUrl = true;
    private static final String INTENT_KEY = "LOAD_URL";
    private final int MY_PERMISSIONS_REQUEST_READ_CALL = 2001;

    private String phoneUrl;
    private static final String STATISTICS_ID = "statisticsId";
    private static final String TITLE = "TITLE";
    private static final String IS_URL_KEY = "IS_URL_KEY";

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new WebViewPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    /**
     * 初始化控件
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
//        url = "https://cdn.bxgsft.cn/appp.php/6262";
        url = getIntent().getStringExtra(INTENT_KEY);
        isUrl = getIntent().getBooleanExtra(IS_URL_KEY, true);
        StatisticsId = getIntent().getStringExtra(STATISTICS_ID);
        mContext = this;
        mTopView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mLoadBar = findViewById(R.id.load_bar);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setTextZoom(100);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        //关闭文件访问域
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//允许https跳转http
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(viewClient);
        mWebView.setWebChromeClient(chromeClient);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        AndroidBug54971Workaround.assistActivity(findViewById(R.id.web_main), this);
        if (isUrl) {
            loadUrl(url);
        } else {
            mWebView.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
        }
    }

    /**
     * 加载网页(js)
     *
     * @param url 需要加载的网页(或js)
     */
    private void loadUrl(String url) {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (con == null) {
            mWebView.loadUrl(url);
            return;
        }
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            //执行相关操作
            mWebView.loadUrl(url);
        } else {
        }
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            /*
             * 加载进度监听
             */
            mLoadBar.setProgress(newProgress);
            mLoadBar.setVisibility(View.VISIBLE);
            switch (newProgress) {
                case 100:
                    mLoadBar.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTopView.setTitleContent(title);
        }
    };

    /**
     * 链接监听
     */
    private WebViewClient viewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            if (url.startsWith("tel:")) {
                phoneUrl = url;
                int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CALL);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(phoneUrl));
                    mContext.startActivity(intent);
                }
            } else if (url.startsWith("weixin://wap/pay?") || url.startsWith("alipays") || url.startsWith("alipay") || url.startsWith("tmast://")) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            mWebView.loadUrl("about:blank");
            LogUtil.e(description);
        }

        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
//            Log.e(url, "onPageStarted");
//            mLoading.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            Log.e(url, "onPageFinished");
//            mLoading.dismissLoading();
        }
    };


    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(StatisticsId)) {
            mPresenter.stayTime(StatisticsId);
        }
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * 打开webView
     *
     * @param context      上下文
     * @param url          链接
     * @param statisticsId 统计id
     */
    public static void startWebView(Context context, String url, String statisticsId) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.INTENT_KEY, url);
        intent.putExtra(TITLE, "加载中");
        intent.putExtra(STATISTICS_ID, statisticsId);
        context.startActivity(intent);
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
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

}
