package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.LoginBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.ILoginNewView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class LoginNewPresenter extends BasePresenter<ILoginNewView> {
    private final String SMS_TYPE = "PASSWORD";

    public LoginNewPresenter(Activity context, ILoginNewView view) {
        super(context, view);
    }

    public void getLoginSms(String phone) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", SMS_TYPE);
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
        params.put("captcha", code);
        params.put("loginType", 1);
        mModel.userLogin(params, new DisposableObserver<LoginBean>() {
            @Override
            public void onNext(LoginBean bean) {
                if (parse(mContext, bean)) {
                    BaseApplication.setToken(mContext, bean.getToken());
                    BaseApplication.setUserId(mContext, String.valueOf(bean.getUid()));
                    BaseApplication.setUserInfo(mContext, bean.getUserVo());
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
