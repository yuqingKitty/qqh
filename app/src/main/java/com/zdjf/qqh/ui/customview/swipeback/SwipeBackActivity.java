package com.zdjf.qqh.ui.customview.swipeback;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 侧滑返回
 */
public abstract class SwipeBackActivity extends RxAppCompatActivity implements SwipeBackHelper.SlideBackManager {

    private static final String TAG = "SwipeBackActivity";

    private SwipeBackHelper mSwipeBackHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = new SwipeBackHelper(this);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //点击其他区域，收起键盘
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return mSwipeBackHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public Activity getSlideActivity() {
        return this;
    }

    @Override
    public boolean supportSlideBack() {
        return true;
    }

    @Override
    public boolean canBeSlideBack() {
        return true;
    }

    @Override
    public void finish() {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper.finishSwipeImmediately();
            mSwipeBackHelper = null;
        }
        super.finish();
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
