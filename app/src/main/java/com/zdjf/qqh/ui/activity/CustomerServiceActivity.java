package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.ServiceBean;
import com.zdjf.qqh.presenter.ServicePresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.CustomDialog;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.zdjf.qqh.utils.RegexUtil;
import com.zdjf.qqh.view.IServiceView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.zdjf.qqh.utils.EditTextUtil.copyString;

/**
 * 联系客服
 */
public class CustomerServiceActivity extends BaseActivity<ServicePresenter> implements IServiceView<ServiceBean> {
    @BindView(R.id.phone_num)
    TextView mPhoneNumTv;
    /**
     * 公众号
     */
    @BindView(R.id.account_tv)
    TextView mAccountTv;
    /**
     * 二维码
     */
    @BindView(R.id.qr_code_iv)
    ImageView mQRCode;
    /**
     * QQ客服布局
     */
    @BindView(R.id.qq_service_layout)
    LinearLayout mQQService;
    /**
     * QQ号
     */
    @BindView(R.id.qq_accounts)
    TextView mQqAccounts;
    /**
     * 工作时间
     */
    @BindView(R.id.work_time)
    TextView mWorkTime;
    /**
     * 客服电话
     */
    @BindView(R.id.phone_layout)
    LinearLayout phone_layout;

    @BindView(R.id.content_layout)
    AutoLinearLayout mContentLayout;
    @BindView(R.id.view_error)
    AutoLinearLayout mNotNetLayout;

    private CustomDialog mDialog;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ServicePresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView() {
        mPresenter.getData();
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
    public void getDataSuccess(ServiceBean bean) {
        mPhoneNumTv.setText(bean.getPhone());
        mAccountTv.setText(bean.getWeixin());
        mQqAccounts.setText(bean.getQq());
        mWorkTime.setText(bean.getWork());
        GlideImageLoader.setImg(this, bean.getQrCode(), mQRCode, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
        mContentLayout.setVisibility(View.VISIBLE);
        mNotNetLayout.setVisibility(View.GONE);

    }

    /**
     * 点击复制
     */
    @OnClick(R.id.copy_btn)
    void click() {
        String account = mAccountTv.getText().toString();
        if (!TextUtils.isEmpty(account)) {
            copyString(this, account);
        }
    }

    /**
     * 长按qq复制
     */
    @OnLongClick(R.id.qq_service_layout)
    boolean longQqClick() {
        String qqAccount = mQqAccounts.getText().toString();
        if (!TextUtils.isEmpty(qqAccount)) {
            copyString(this, qqAccount);
        }
        return true;
    }

    /**
     * 点击电话
     */
    @OnClick(R.id.phone_layout)
    void phoneClick() {
        final String phoneNum = mPhoneNumTv.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || (!RegexUtil.isMobileSimple(phoneNum) && !RegexUtil.isTel(phoneNum))) {
            return;
        }

        AndPermission.with(this).runtime().permission(Permission.CALL_PHONE).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (mDialog == null) {
                    mDialog = new CustomDialog(CustomerServiceActivity.this);
                }
                mDialog.builder().setTitle("拨打：" + phoneNum + "?")
                        .setNegativeButton("", null)
                        .setPositiveButton("", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNum));
                                CustomerServiceActivity.this.startActivity(intent);
                            }
                        }).show();
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                showToast("权限获取失败");
            }
        }).start();
    }

    @Override

    public void getDataFailed() {
        mContentLayout.setVisibility(View.GONE);
        mNotNetLayout.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.view_error)
    void noNetworkClick() {
        mPresenter.getData();
    }

    private String pageTitle = "联系客服";

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
