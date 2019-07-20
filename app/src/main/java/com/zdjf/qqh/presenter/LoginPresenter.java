package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.LoginBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.ILoginView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(Activity context, ILoginView view) {
        super(context, view);
    }

    public void login(String phone, String pwd) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", pwd);
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
