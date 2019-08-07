package com.leuters.qqh.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.leuters.qqh.R;
import com.leuters.qqh.ui.customview.AnimLoadingDialog;
import com.leuters.qqh.ui.customview.swipeback.SwipeBackActivity;
import com.leuters.qqh.utils.ToastCompat;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends SwipeBackActivity {
    //未指定类型的Presenter
    protected P mPresenter;

    private ImmersionBar mImmersionBar;

    protected abstract void initPresenter(Intent intent);

    //设置布局
    protected abstract int getLayout();

    //初始化布局
    protected abstract void initView();

    protected AnimLoadingDialog mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(getLayout());
        initStatusBar(true);
        //初始化ButterKnife
        ButterKnife.bind(this);
        initPresenter(getIntent());
        checkPresenterIsNull();
        mLoading = new AnimLoadingDialog(this);
        initView();
    }

    /**
     * 初始化状态栏
     *
     * @param darkFont
     */
    public void initStatusBar(boolean darkFont) {
        mImmersionBar = ImmersionBar.with(this);
        transparencyStatus(mImmersionBar, darkFont);
    }

    /**
     * 设置透明 深色字体
     *
     * @param immersionBar
     * @param darkFont     字体是否深色
     */
    public static void transparencyStatus(ImmersionBar immersionBar, boolean darkFont) {
        if (immersionBar != null) {
//            if (BaseApplication.isMeizu) {
//                immersionBar
//                        .statusBarColorTransform(R.color.colorPrimary)
//                        .statusBarDarkFont(darkFont)
//                        .flymeOSStatusBarFontColor(darkFont ? R.color.mainTextColor : R.color.white)
//                        .barAlpha(0.2f)
//                        .init();
//                return;
//            }
            immersionBar
                    .transparentStatusBar()
                    .statusBarDarkFont(darkFont)
                    .flymeOSStatusBarFontColor(darkFont ? R.color.mainTextColor : R.color.white)
                    .init();
        }
    }

    //设置打印方法
    public void showToast(String text) {
        ToastCompat.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }
}
