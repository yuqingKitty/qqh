package com.zdjf.qqh.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.utils.EditTextUtil;

import static com.zdjf.qqh.utils.IntentUtil.getWechatApi;

/**
 * 微信公众号
 */
public class WxAccountsFragment extends DialogFragment implements View.OnClickListener {
    private Context mContext;
    private ImageView mCloseIv;
    private Button mToWxBtn;
    public static boolean isShow = false;
    public static String INTENT_KEY = "INTENT_KEY";

    public static WxAccountsFragment newInstance(Bundle args) {
        WxAccountsFragment fragment = new WxAccountsFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            if (mContext != null) {
                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                lp.height = (int) (displayMetrics.heightPixels * 0.8f);
                lp.width = (int) (displayMetrics.widthPixels * 0.8f);
            }
            dialogWindow.setAttributes(lp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_wx_accounts_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        if (view == null) {
            return;
        }
        mToWxBtn = view.findViewById(R.id.to_wx_btn);
        mCloseIv = view.findViewById(R.id.close_iv);
        EditTextUtil.setTextBold((TextView) view.findViewById(R.id.account_text));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        initEvents();
    }

    private void initEvents() {
        mToWxBtn.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_wx_btn:
                //跳转微信
                getWechatApi((Activity) mContext);
                break;
            case R.id.close_iv:
                //关闭
                getDialog().cancel();
                break;

        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager == null || manager.isDestroyed()) {
            return;
        }

        try {
            if (!isShow) {
                super.show(manager, tag);
            }
        } catch (Exception e) {
//            ExceptionHandler exceptionHandler = ExceptionHandlerHelper.getInstance();
//            if (exceptionHandler != null) {
//                exceptionHandler.onException(e);
//            }
        }
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }

    public static int dip2px(int dip, Context context) {
        return (int) (dip * getDensity(context) + 0.5f);
    }

    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}

