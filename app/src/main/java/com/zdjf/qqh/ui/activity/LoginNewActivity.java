package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.presenter.LoginNewPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.LoginInputView;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.view.ILoginNewView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_LOGIN_SUCCESS_KEY;

public class LoginNewActivity extends BaseActivity<LoginNewPresenter> implements ILoginNewView, LoginInputView.LoginInputCallback {
    @BindView(R.id.liv_login_phone)
    LoginInputView liv_login_phone;
    @BindView(R.id.liv_login_code)
    LoginInputView liv_login_code;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.btn_login)
    Button btn_login;

    private boolean isPhoneEmpty = true, isCodeEmpty = true;//手机号，验证码是否为空
    private String mPhone, mCode;//手机号码，密码，验证码

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new LoginNewPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView() {
        liv_login_phone.getEdtRegisterPhone().setInputType(InputType.TYPE_CLASS_NUMBER);
        liv_login_phone.getEdtRegisterPhone().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        liv_login_code.getEdtRegisterPhone().setInputType(InputType.TYPE_CLASS_NUMBER);
        liv_login_code.getEdtRegisterPhone().setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});

        liv_login_phone.setOnLoginInputListener(this);
        liv_login_code.setOnLoginInputListener(this);
        liv_login_phone.setMaxLength(13);
        liv_login_code.setMaxLength(7);

        btn_login.setEnabled(false);
    }

    @OnClick({R.id.tv_get_code, R.id.btn_login})
    void click(View view) {
        mPhone = liv_login_phone.getEditRegisterPhone().replace(" ", "");
        mCode = liv_login_code.getEditRegisterPhone().replace(" ", "");
        switch (view.getId()) {
            case R.id.tv_get_code:
                // 请求验证码
                mPresenter.getLoginSms(mPhone);
                break;
            case R.id.btn_login:
                mPresenter.login(mPhone, mCode);
                break;
        }
    }

    @Override
    public void getLoginSmsSuccess() {
        showToast("验证码已发送到您的手机，请注意查收！");
        new CountDowner(60000, 1000).start();
        tv_get_code.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGIN_SUCCESS_KEY));
        this.finish();
    }

    @Override
    public void loginFailed(String dec) {
        showToast(dec);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoading != null) {
            mLoading.dismissLoading();
        }
    }

    private String pageTitle = "登陆界面";

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
    public void isEditTextEmpty(LoginInputView view, boolean isContentEmpty) {
        switch (view.getId()) {
            case R.id.liv_login_phone:
                isPhoneEmpty = isContentEmpty;
                break;
            case R.id.liv_login_code:
                isCodeEmpty = isContentEmpty;
                break;

        }
        if (!isPhoneEmpty && !isCodeEmpty) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
    }

    @Override
    public void isClearEditRegisterPhone(LoginInputView view) {
        switch (view.getId()) {
            case R.id.liv_login_phone:
                liv_login_phone.clearText();
                break;
            case R.id.liv_login_code:
                liv_login_code.clearText();
                break;
        }
    }


    class CountDowner extends CountDownTimer {
        public CountDowner(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_get_code.setEnabled(true);
            tv_get_code.setBackgroundResource(R.drawable.message_official_bg);
            tv_get_code.setText("获取验证码");
            tv_get_code.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_get_code.setBackgroundResource(0);
            tv_get_code.setText("剩余" + String.valueOf(millisUntilFinished / 1000) + "S");
            tv_get_code.setTextColor(getResources().getColor(R.color.color_FFC71D));
        }
    }


}
