package com.leuters.qqh.utils;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * 计时器
 */
public class TimeCount extends CountDownTimer {
    private Button mButton;
    private TextView mText;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mButton = button;
    }

    public TimeCount(TextView text, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mText = text;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mButton != null) {
            mButton.setClickable(false);
            mButton.setText(String.format("%ss", String.valueOf(millisUntilFinished / 1000)));
        } else if (mText != null) {
            mText.setClickable(false);
            mText.setText(String.format("%ss", String.valueOf(millisUntilFinished / 1000)));
        }

    }

    @Override
    public void onFinish() {
        if (mButton != null) {
            mButton.setText("发送验证码");
            mButton.setClickable(true);
        } else if (mText != null) {
            mText.setText("发送验证码");
            mText.setClickable(true);
        }
    }
}



