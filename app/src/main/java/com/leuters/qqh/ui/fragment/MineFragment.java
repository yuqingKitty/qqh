package com.leuters.qqh.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leuters.qqh.R;
import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.data.entity.UserBean;
import com.leuters.qqh.presenter.MinePresenter;
import com.leuters.qqh.ui.base.BaseFragment;
import com.leuters.qqh.utils.GlideImageLoader;
import com.leuters.qqh.utils.rxbus.RxBus;
import com.leuters.qqh.view.IMineView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGIN_SUCCESS_KEY;
import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;
import static com.leuters.qqh.data.commons.Constants.RXBUS_NICKNAME_SUCCESS_KEY;
import static com.leuters.qqh.data.commons.Constants.RXBUS_UPLOAAD_SUCCESS_KEY;
import static com.leuters.qqh.utils.IntentUtil.toMessageCenterActivity;
import static com.leuters.qqh.utils.IntentUtil.toMyLoanRecordActivity;
import static com.leuters.qqh.utils.IntentUtil.toServiceActivity;
import static com.leuters.qqh.utils.IntentUtil.toSettingActivity;

public class MineFragment extends BaseFragment<MinePresenter> implements IMineView {
    private RxBus rxBus;
    /**
     * 用户名
     */
    @BindView(R.id.user_name)
    TextView mUserName;
    /**
     * 用户头像
     */
    @BindView(R.id.mine_head)
    ImageView mUserHead;
    private UserBean mUserBean;

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter(mActivity, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initRxBus();
        mUserBean = BaseApplication.getUserBean(mActivity);
        fillData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mActivity != null) {
            mActivity.initStatusBar(false);
        }
    }

    @OnClick({R.id.user_name, R.id.iv_setting, R.id.tv_loan_record, R.id.tv_message_center, R.id.tv_my_service})
    void click(View view) {
        switch (view.getId()) {
            case R.id.user_name:
                BaseApplication.isLogin(mActivity, true, false);
                break;
            case R.id.iv_setting:
                //设置
                if (BaseApplication.isLogin(mActivity, true, true)) {
                    toSettingActivity(mActivity);
//                    mPresenter.simpleRecord("", Constants.moduleName.Setting.getName(), "");
                }
                break;
            case R.id.tv_loan_record:
                //申请记录
                if (BaseApplication.isLogin(mActivity, true, true)) {
                    toMyLoanRecordActivity(mActivity);
//                    mPresenter.simpleRecord("", Constants.moduleName.MyLoanRecord.getName(), "");
                }
                break;
            case R.id.tv_message_center:
                // 消息中心
                if (BaseApplication.isLogin(mActivity, true, true)) {
                    toMessageCenterActivity(mActivity);
//                    mPresenter.simpleRecord("", Constants.moduleName.MessageCenter.getName(), "");
                }
                break;
            case R.id.tv_my_service:
                if (BaseApplication.isLogin(mActivity, true, true)) {
                    //客服
                    toServiceActivity(mActivity);
//                    mPresenter.simpleRecord("", Constants.moduleName.Service.getName(), "");
                }
                break;
            default:
                break;
        }
    }

    private void fillData() {
        if (mUserBean != null) {
            String userName = mUserBean.getNickName();
            mUserName.setText(TextUtils.isEmpty(userName) ? mUserBean.getPhoneMask() : userName);
            GlideImageLoader.setCircleImg(mActivity, mUserBean.getImageIcon(), mUserHead, R.mipmap.icon_img_default, R.mipmap.icon_img_default);
        } else {
            mUserName.setText("立即登录");
            GlideImageLoader.setCircleImg(mActivity, R.mipmap.icon_img_default, mUserHead);
        }
    }

    private void initRxBus() {
        rxBus = RxBus.getInstanceBus();
        registerRxBus(RxBusMessage.class, new Consumer<RxBusMessage>() {
            @Override
            public void accept(@NonNull RxBusMessage rxBusMessage) {
                //根据事件类型进行处理
                switch (rxBusMessage.getCode()) {
                    case RXBUS_LOGIN_SUCCESS_KEY://登陆成功
                        mUserBean = BaseApplication.getUserBean(mActivity);
                        fillData();
                        break;
                    case RXBUS_LOGOUT_SUCCESS_KEY://退出成功
                        mUserBean = null;
                        BaseApplication.ClearUser(mActivity);
                        fillData();
                        break;
                    case RXBUS_UPLOAAD_SUCCESS_KEY://上传头像成功
                        BaseApplication.setUserHead(mActivity, rxBusMessage.getMsg());
                        if (mUserBean != null) {
                            mUserBean.setImageIcon(rxBusMessage.getMsg());
                        }
                        GlideImageLoader.setCircleImg(mActivity, mUserBean.getImageIcon(), mUserHead, R.mipmap.icon_img_default, R.mipmap.icon_img_default);
                        break;
                    case RXBUS_NICKNAME_SUCCESS_KEY://昵称设置成功
                        mUserBean = BaseApplication.getUserBean(mActivity);
                        String userName = mUserBean.getNickName();
                        mUserName.setText(TextUtils.isEmpty(userName) ? mUserBean.getPhoneMask() : userName);
                        break;
                }
            }
        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
        rxBus.addSubscription(this, disposable);
    }

    @Override
    public void showLoading() {
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoading != null)
            mLoading.dismissLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String mPageName = "我的";

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
}
