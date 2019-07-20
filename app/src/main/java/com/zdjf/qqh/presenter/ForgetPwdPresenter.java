package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IForgetPwdView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * 忘记密码
 */
public class ForgetPwdPresenter extends BasePresenter<IForgetPwdView> {
    private final String SMSTYPE = "RESET";

    public ForgetPwdPresenter(Activity context, IForgetPwdView view) {
        super(context, view);
    }

    /**
     * 重置密码
     *
     * @param phone
     * @param password
     * @param captcha
     */
    public void resetPassword(String phone, String password, String captcha) {
        obtainView().showLoading();
        final Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("captcha", captcha);
        mModel.resetPassword(params, new DisposableObserver<BaseBean>() {

            @Override
            public void onNext(BaseBean baseBean) {
                if (parse(mContext, baseBean)) {
                    obtainView().resetSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
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
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }
}
