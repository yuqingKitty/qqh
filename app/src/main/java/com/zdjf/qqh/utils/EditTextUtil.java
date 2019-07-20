package com.zdjf.qqh.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * EditText工具类
 * Created by Administrator on 2017/4/25.
 */

public class EditTextUtil {
    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                if (!RegexUtil.isChzLetter(source.toString())) {
                    return "";
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 设置昵称输入规则
     *
     * @param editText 文本框
     * @param length   限制长度
     */
    public static void setEditTextUserName(EditText editText, final int length) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                if (dstart >= length || dest.length() >= length) {// 字符数限制
                    return "";
                }
                if (!RegexUtil.isUsername(source.toString())) {
                    return "";
                }
                /*
                 * 判断是否超出长度
                 */
                if (dstart + source.length() > length) {
                    return source.toString().substring(0, length - dstart);
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 设置editText光标为末尾
     *
     * @param edit
     */
    public static void moveEtCursor2Last(EditText edit) {
        if (edit == null) {
            return;
        }
        edit.setSelection(edit.getText().length());
    }

    /**
     * 设置TextView字体加粗
     *
     * @param textView
     */
    public static void setTextBold(TextView textView) {
        textView.getPaint().setFakeBoldText(true);
    }

    /**
     * 设置Button字体加粗
     *
     * @param buttonView
     */
    public static void setButtonBold(Button buttonView) {
        buttonView.getPaint().setFakeBoldText(true);

    }

    /**
     * 复制字符串到粘贴板
     *
     * @param context
     * @param text
     */
    public static void copyString(Context context, String text) {
        ClipboardManager cmWx = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipDataWx = ClipData.newPlainText("Label", text);
        if (cmWx != null) {
            cmWx.setPrimaryClip(mClipDataWx);
            ToastCompat.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
        } else {
            ToastCompat.makeText(context, "复制失败，请重试。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 复制字符串到粘贴板(不显示toast)
     *
     * @param context
     * @param text
     */
    public static void copyStringUnshowToast(Context context, String text) {
        ClipboardManager cmWx = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipDataWx = ClipData.newPlainText("Label", text);
        if (cmWx != null) {
            cmWx.setPrimaryClip(mClipDataWx);
        }
    }
}
