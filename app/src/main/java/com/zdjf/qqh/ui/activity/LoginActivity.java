package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.presenter.LoginPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.AddSpaceTextWatcher;
import com.zdjf.qqh.utils.EditTextUtil;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.view.ILoginView;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_LOGIN_SUCCESS_KEY;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {
    @BindView(R.id.phone_et)
    EditText mPhoneEt;
    @BindView(R.id.pwd_et)
    EditText mPwdEt;
    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new LoginPresenter(this, this);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.mainTextColor).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        //手机号添加空格
        AddSpaceTextWatcher asEditTexts = new AddSpaceTextWatcher(mPhoneEt, 13, true);
        asEditTexts.setSpaceType(AddSpaceTextWatcher.SpaceType.mobilePhoneNumberType);
        asEditTexts.setButtonListen(mLoginBtn, mPwdEt);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick({R.id.login_btn, R.id.to_register, R.id.forget_pwd})
    void click(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String phone = mPhoneEt.getText().toString().replace(" ", "");
                String pwd = mPwdEt.getText().toString().trim();
                mPresenter.login(phone, pwd);
                break;
            case R.id.to_register:
                IntentUtil.toRegisterActivity(this);
                break;
            case R.id.forget_pwd:
                IntentUtil.toForgetPwdActivity(this);
                break;
        }
    }

    @OnCheckedChanged(R.id.show_pwd)
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        EditTextUtil.moveEtCursor2Last(mPwdEt);
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

    /**
     * 关闭界面
     */
    @OnClick(R.id.login_close)
    void closeLogin() {
        finish();
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
}
