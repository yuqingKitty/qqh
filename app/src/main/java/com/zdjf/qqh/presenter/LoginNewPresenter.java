package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.data.entity.LoginBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.ILoginNewView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class LoginNewPresenter extends BasePresenter<ILoginNewView> {
    private final String SMS_TYPE = "LOGIN";

    public LoginNewPresenter(Activity context, ILoginNewView view) {
        super(context, view);
    }

    public void getLoginSms(String phone) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("smsPhone", phone);
        params.put("smsType", SMS_TYPE);
        mModel.sendSms(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean o) {
                if (parse(mContext, o)) {
                    obtainView().getLoginSmsSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {LogUtil.e(e.toString());
                obtainView().loginFailed("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }

    public void login(String phone, String code) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        mModel.userLogin(params, new DisposableObserver<LoginBean>() {
            @Override
            public void onNext(LoginBean bean) {
                if (parse(mContext, bean)) {
                    BaseApplication.setToken(mContext, bean.getToken());
                    BaseApplication.setUserId(mContext, String.valueOf(bean.getUserInfo().getUserId()));
                    BaseApplication.setUserInfo(mContext, bean.getUserInfo());
                    obtainView().loginSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().loginFailed("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }

}
