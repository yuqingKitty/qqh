package com.zdjf.qqh.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 调整可视布局
 */

public class AndroidBug54971Workaround {

    private Context mContext;

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
//    private static boolean isKeyBordVisiable;

    public static void assistActivity(View content, Context context) {
        new AndroidBug54971Workaround(content, context);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidBug54971Workaround(View content, Context context) {
        mContext = context;
        mChildOfContent = content;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致


//            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                // keyboard probably just became visible
//                isKeyBordVisiable=true;
//            } else {
//                // keyboard probably just became hidden
//                isKeyBordVisiable=false;
//            }
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = computeUsableHeight();
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom;
    }
}
