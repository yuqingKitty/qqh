package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.view.IWebView;

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
        params.put("prodAccessId", id);

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
