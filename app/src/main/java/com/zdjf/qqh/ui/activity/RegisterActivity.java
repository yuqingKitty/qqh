package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.RegisterPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.AddSpaceTextWatcher;
import com.zdjf.qqh.utils.EditTextUtil;
import com.zdjf.qqh.utils.RegexUtil;
import com.zdjf.qqh.utils.TimeCount;
import com.zdjf.qqh.view.IRegisterView;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.REGIST_CMSTYPE;
import static com.zdjf.qqh.utils.IntentUtil.toProtocolActivity;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterView {
    @BindView(R.id.phone_et)
    EditText mPhoneEt;
    /**
     * 验证码
     */
    @BindView(R.id.verification_et)
    EditText mVerificationEt;
    @BindView(R.id.pwd_et)
    EditText mPwdEt;
    @BindView(R.id.get_verification)
    TextView mVerificationBtn;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;
    @BindView(R.id.agree_cb)
    CheckBox mCheckBox;
    AddSpaceTextWatcher asEditTexts;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RegisterPresenter(this, this);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        //手机号添加空格
        asEditTexts = new AddSpaceTextWatcher(mPhoneEt, 13, true);
        asEditTexts.setSpaceType(AddSpaceTextWatcher.SpaceType.mobilePhoneNumberType);
        asEditTexts.setButtonListen(mRegisterBtn, mCheckBox, mPwdEt, mVerificationEt);

    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.get_verification)
    void getVerification() {
        String phone = mPhoneEt.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone) || !RegexUtil.isMobileSimple(phone)) {
            showToast("请输正确的手机号");
            return;
        }
        mPresenter.getSendSms(phone);
    }

    @OnCheckedChanged({R.id.agree_cb, R.id.show_pwd})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.agree_cb:
                mRegisterBtn.setEnabled(asEditTexts.checkAllEdit() && isChecked);
                break;
            case R.id.show_pwd:
                mPwdEt.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                EditTextUtil.moveEtCursor2Last(mPwdEt);
                break;
        }
    }

    /**
     * 注册
     */
    @OnClick(R.id.register_btn)
    void register() {
        String phone = mPhoneEt.getText().toString().trim().replaceAll(" ", "");
        String pwd = mPwdEt.getText().toString().trim();
        String captcha = mVerificationEt.getText().toString().trim();

        mPresenter.register(phone, pwd, captcha);
    }

    /**
     * 点击协议
     */
    @OnClick({R.id.register_protocol_tv})
    void readProtocol(View view) {
        switch (view.getId()) {
            case R.id.register_protocol_tv:
                //注册协议
                toProtocolActivity(this, REGIST_CMSTYPE, "用户注册协议");
                break;
        }
    }

    /**
     * 跳转登陆
     */
    @OnClick(R.id.to_login)
    void toLogin() {
        finish();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        mLoading.dismissLoading();
    }

    @Override
    public void registerSuccess() {
        showToast("注册成功");
        finish();
    }

    @Override
    public void portError(String des) {
        showToast(des);
    }

    @Override
    public void getSmsSuccess() {
        TimeCount timeCount = new TimeCount(mVerificationBtn, 60000, 1000);
        timeCount.start();
        showToast("已发送");
    }

    private String pageTitle = "注册";

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
