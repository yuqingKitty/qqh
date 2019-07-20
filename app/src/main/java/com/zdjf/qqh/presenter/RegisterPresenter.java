package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IRegisterView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * 注册界面
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {
    private final String SMSTYPE = "REGISTER";

    public RegisterPresenter(Activity context, IRegisterView view) {
        super(context, view);
    }

    public void register(String phone, String password, String captcha) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("captcha", captcha);

        mModel.userRegister(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean o) {
                if (parse(mContext, o)) {
                    obtainView().registerSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().portError("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }

    public void getSendSms(String phone) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("smsPhone", phone);
        params.put("smsType", SMSTYPE);
        mModel.sendSms(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean o) {
                if (parse(mContext, o)) {
                    obtainView().getSmsSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {LogUtil.e(e.toString());
                obtainView().portError("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }
}
