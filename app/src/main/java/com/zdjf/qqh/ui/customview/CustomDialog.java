package com.zdjf.qqh.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.utils.GlideImageLoader;

/**
 * 自定义提示对话框
 */

public class CustomDialog implements View.OnClickListener {

    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView icon_iv;
    private ImageView close_iv;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private DisplayMetrics metrics;

    public CustomDialog(Context context) {
        this.context = context;
        metrics = context.getResources().getDisplayMetrics();
    }

    public CustomDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        RelativeLayout lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        icon_iv = view.findViewById(R.id.dialog_icon);
        icon_iv.setVisibility(View.GONE);
        close_iv = view.findViewById(R.id.close_iv);
        close_iv.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (metrics.widthPixels * 0.8), LayoutParams.WRAP_CONTENT));

        return this;
    }

    /**
     * 设置是否显示"取消"按钮
     *
     * @param showNegBtn 是否显示
     * @return
     */
    public CustomDialog showNegativeButton(boolean showNegBtn) {
        if (showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
        } else {
            btn_neg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置是否显示"确认"按钮
     *
     * @param showPosBtn
     * @return
     */
    public CustomDialog showPositiveButton(boolean showPosBtn) {
        btn_pos.setVisibility(showPosBtn ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置是否显示关闭按钮
     *
     * @param showClose
     * @return
     */
    public CustomDialog showCloseIcon(boolean showClose) {
        close_iv.setVisibility(showClose ? View.VISIBLE : View.GONE);
        if (showClose) {
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return this;
    }

    public CustomDialog setIcon(int drawable) {
        GlideImageLoader.setImg(context, drawable, icon_iv);
        icon_iv.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomDialog setTitle(String title) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (TextUtils.isEmpty(title)) {
//            txt_title.setVisibility(View.GONE);
//            if (mShowClose) {
//                lp.setMargins(10, 5, 10, 20);
//            } else {
//                lp.setMargins(10, 40, 10, 30);
//            }
//            mLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//            txt_msg.setLayoutParams(lp);
        } else {
//            lp.setMargins(10, 10, 10, 20);
//            mLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//            txt_msg.setLayoutParams(lp);
            showTitle = true;
            txt_title.setText(title);
        }
        return this;
    }

    public CustomDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public CustomDialog setHtmlMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(Html.fromHtml(msg));
        }
        return this;
    }

    public CustomDialog setMsgLeft() {
        txt_msg.setGravity(Gravity.START);
        return this;
    }

    public CustomDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CustomDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public CustomDialog setNegativeButton(String text,
                                          final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
//            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_neg.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
        }
    }

    public void show() {
        setLayout();
        if (!isShowing()) {
            dialog.show();
        }
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public CustomDialog setLeftTextColor(Context context, int i) {
        btn_neg.setTextColor(context.getResources().getColor(i));
        return this;
    }
}
