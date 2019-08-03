package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zdjf.qqh.R;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.data.entity.UserBean;
import com.zdjf.qqh.presenter.SetNicknamePresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.view.ISetNicknameView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_NICKNAME_SUCCESS_KEY;

/**
 * 设置昵称
 */
public class SetNicknameActivity extends BaseActivity<SetNicknamePresenter> implements ISetNicknameView {
    @BindView(R.id.nickname)
    EditText mNicknameEt;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SetNicknamePresenter(this, this);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_nickname;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void changeSuccess(String nickname) {
        UserBean user = BaseApplication.getUserBean(this);
        user.setNickName(nickname);
        BaseApplication.setUserInfo(this, user);
        RxBusMessage message = new RxBusMessage<>(RXBUS_NICKNAME_SUCCESS_KEY);
        message.setMsg(nickname);
        RxBus.getInstanceBus().post(message);
        showToast("修改成功");
        finish();
    }

    @OnClick(R.id.submit_nickname)
    void clickView(View v) {
        String nicknameString = mNicknameEt.getText().toString().trim();
        if (TextUtils.isEmpty(nicknameString) || nicknameString.length() < 2) {
            showToast("请输入至少2个字符");
            return;
        }
        if (Character.isDigit(nicknameString.charAt(0))) {
            showToast("用户名不能以数字开头");
            return;
        }
        mPresenter.editNickname(nicknameString);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.dismissLoading();
    }
}
