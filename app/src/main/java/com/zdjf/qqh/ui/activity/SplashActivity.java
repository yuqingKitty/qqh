package com.zdjf.qqh.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zdjf.qqh.R;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.utils.APKVersionCodeUtil;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import static com.zdjf.qqh.utils.IntentUtil.toMainActivity;

/**
 * @Description: 闪屏界面
 * @Author: Young
 * @Time: 2018/5/4 10:17
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private SplashHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
        //获取渠道
        BaseApplication.CHANNEL = APKVersionCodeUtil.getAppMetaData(this, "UMENG_CHANNEL");
        // 延迟3秒给handler发送信息
        handler = new SplashHandler(SplashActivity.this);
        SplashActivity.this.handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View v) {
    }

    static class SplashHandler extends Handler {
        private WeakReference<SplashActivity> mActivity;

        SplashHandler(SplashActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            toMainActivity(mActivity.get());
            mActivity.get().finish();
        }
    }

    private String pageTitle = "启动页";

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
