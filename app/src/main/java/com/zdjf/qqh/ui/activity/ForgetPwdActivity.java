package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.ForgetPwdPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.AddSpaceTextWatcher;
import com.zdjf.qqh.utils.EditTextUtil;
import com.zdjf.qqh.utils.RegexUtil;
import com.zdjf.qqh.utils.TimeCount;
import com.zdjf.qqh.view.IForgetPwdView;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity<ForgetPwdPresenter> implements IForgetPwdView {
    /**
     * 手机号
     */
    @BindView(R.id.phone_et)
    EditText mPhoneEt;
    /**
     * 验证码输入
     */
    @BindView(R.id.verification_et)
    EditText mVerificationEt;
    @BindView(R.id.get_verification_tv)
    TextView mGetVerification;
    @BindView(R.id.new_pwd_et)
    EditText mNewPwdEt;
    @BindView(R.id.confirm_pwd_et)
    EditText mConfirmPwdEt;
    @BindView(R.id.submit_reset_pwd)
    Button mSubmitPwd;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ForgetPwdPresenter(this, this);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        //手机号添加空格
        AddSpaceTextWatcher asEditTexts = new AddSpaceTextWatcher(mPhoneEt, 13, true);
        asEditTexts.setSpaceType(AddSpaceTextWatcher.SpaceType.mobilePhoneNumberType);
        asEditTexts.setButtonListen(mSubmitPwd, mNewPwdEt, mConfirmPwdEt, mVerificationEt);
    }

    @OnCheckedChanged({R.id.show_pwd, R.id.confirm_show_pwd})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.show_pwd:
                if (isChecked) {
                    mNewPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mNewPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                EditTextUtil.moveEtCursor2Last(mNewPwdEt);
                break;
            case R.id.confirm_show_pwd:
                if (isChecked) {
                    mConfirmPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mConfirmPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                EditTextUtil.moveEtCursor2Last(mConfirmPwdEt);
                break;
        }
    }

    @OnClick(R.id.submit_reset_pwd)
    void submit_reset() {
        String phone = mPhoneEt.getText().toString().trim().replaceAll(" ", "");
        String pwd = mNewPwdEt.getText().toString().trim();
        String confirmPwd = mConfirmPwdEt.getText().toString().trim();
        String captcha = mVerificationEt.getText().toString().trim();
        if (!pwd.equals(confirmPwd)) {
            showToast("两次密码不一致");
            return;
        }
        mPresenter.resetPassword(phone, pwd, captcha);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.get_verification_tv)
    void getVerification() {
        String phone = mPhoneEt.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone) || !RegexUtil.isMobileSimple(phone)) {
            showToast("请输正确的手机号");
            return;
        }
        mPresenter.getSendSms(phone);
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

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void resetSuccess() {
        showToast("密码已重置");
        finish();
    }

    @Override
    public void getSmsSuccess() {
        TimeCount timeCount = new TimeCount(mGetVerification, 60000, 1000);
        timeCount.start();
        showToast("已发送");
    }

    private String pageTitle = "忘记密码";

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
