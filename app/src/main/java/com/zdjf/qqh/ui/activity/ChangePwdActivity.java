package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.ChangePwdPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.AddSpaceTextWatcher;
import com.zdjf.qqh.utils.EditTextUtil;
import com.zdjf.qqh.view.IChangePwdView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ChangePwdActivity extends BaseActivity<ChangePwdPresenter> implements IChangePwdView {
    @BindView(R.id.old_pwd_et)
    EditText mOldPwdEt;
    @BindView(R.id.new_pwd_et)
    EditText mNewPwdEt;
    @BindView(R.id.confirm_pwd_et)
    EditText mConfirmPwdEt;
    @BindView(R.id.edit_pwd)
    Button mEditPwdBtn;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ChangePwdPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
        AddSpaceTextWatcher asEditTexts = new AddSpaceTextWatcher(mOldPwdEt, 20, false);
        asEditTexts.setButtonListen(mEditPwdBtn, mNewPwdEt, mConfirmPwdEt);
    }

    @OnClick(R.id.edit_pwd)
    void click(View view) {
        String oldPwd = mOldPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            showToast("请输入原密码");
            return;
        }
        String newPwd = mNewPwdEt.getText().toString().trim();
        String confirmPwd = mConfirmPwdEt.getText().toString().trim();
        if (!newPwd.equals(confirmPwd)) {
            showToast("两次密码不一致");
            return;
        }
        mPresenter.changePwd(oldPwd, newPwd);
    }

    @OnCheckedChanged({R.id.show_old_pwd, R.id.show_new_pwd, R.id.show_confirm_pwd})
    void checkChanged(CheckBox checkBox, boolean isChecked) {
        switch (checkBox.getId()) {
            case R.id.show_old_pwd:
                if (isChecked) {
                    mOldPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mOldPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                EditTextUtil.moveEtCursor2Last(mOldPwdEt);
                break;
            case R.id.show_new_pwd:
                if (isChecked) {
                    mNewPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mNewPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                EditTextUtil.moveEtCursor2Last(mNewPwdEt);
                break;
            case R.id.show_confirm_pwd:
                if (isChecked) {
                    mConfirmPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mConfirmPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                EditTextUtil.moveEtCursor2Last(mConfirmPwdEt);
                break;
        }
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

    @Override
    public void changeSuccess() {
        showToast("修改成功");
        finish();
    }

    private String pageTitle = "修改密码";

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
