package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.view.IWebView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class WebViewPresenter extends BasePresenter<IWebView> {
    public WebViewPresenter(Activity context, IWebView view) {
        super(context, view);
    }

    /**
     * 统计时间
     *
     * @param id
     */
    public void stayTime(String id) {
        Map<String, Object> params = new HashMap<>();
        String userId = BaseApplication.getUserId(mContext);
        String token = BaseApplication.getToken(mContext);
        params.put("userId", userId);
        params.put("token", token);
        params.put("statisticsDetailId", id);

        mModel.toStayTime(params, new DisposableObserver() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
