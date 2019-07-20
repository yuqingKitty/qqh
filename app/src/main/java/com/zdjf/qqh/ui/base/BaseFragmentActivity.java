package com.zdjf.qqh.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zdjf.qqh.R;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends RxAppCompatActivity {
    private ImmersionBar mImmersionBar;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        initStatusBar(true);
//        }
        ButterKnife.bind(this);
        initView();
    }

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
//                    .statusBarColorTransform(R.color.mainColor)
                    .transparentStatusBar()
                    .statusBarDarkFont(darkFont)
                    .flymeOSStatusBarFontColor(darkFont ? R.color.mainTextColor : R.color.white)
//                    .barAlpha(0.2f)
                    .init();
        }
    }

    public void selectTab(int num) {
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            for (int i = 0; i < fragments.size(); i++) {
                fragmentTransaction.hide(fragments.get(i));
            }
            fragmentTransaction.show(fragments.get(num));
        } catch (ArrayIndexOutOfBoundsException ae) {
            ae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();
    }

    protected abstract void initView();

    //设置布局
    protected abstract int getLayout();

}
