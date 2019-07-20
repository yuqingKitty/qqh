package com.zdjf.qqh.ui.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 动态加载框
 */
public class AnimLoadingDialog extends Dialog {

    private Context mContext;
    private TextView mLoadingText;

    public AnimLoadingDialog(@NonNull Context context) {
        super(context, R.style.toast_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading_dialog);
        AutoUtils.auto(findViewById(R.id.dialog_layout));

        mLoadingText = findViewById(R.id.loading_text);
//        GlideImageLoader.setImg(mContext, R.drawable.loading, (ImageView) findViewById(R.id.loading_iv));
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        if (this.isShowing() || mContext == null || ((Activity) mContext).isDestroyed()) {
            return;
        }
        super.show();
    }

    public AnimLoadingDialog setText(String text) {
        if (mLoadingText != null) {
            mLoadingText.setText(text);
        }
        return this;
    }

    public AnimLoadingDialog showLoading(Context context, String text) {
        return this;
    }

    /**
     * 关闭
     */
    public void dismissLoading() {
        if (isShowing() && !((Activity) mContext).isDestroyed()) {
            //正在显示
            cancel();
        }
    }

}
