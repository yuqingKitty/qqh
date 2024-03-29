package com.zdjf.qqh.utils.updateapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.UploadBean;
import com.zdjf.qqh.utils.ColorUtil;
import com.zdjf.qqh.utils.DrawableUtil;
import com.zdjf.qqh.utils.ToastCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.zdjf.qqh.utils.updateapp.AppUpdateUtil.getApkName;

public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    private Context mContext;
    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static boolean isShow = false;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private LinearLayout mLlClose;
    private UploadBean mUpdateApp;
    //默认色
    private int mDefaultColor = 0xff39c1e9;
    private int mDefaultPicResId = R.mipmap.lib_update_app_top_bg;
    private ImageView mTopIv;
    private TextView mIgnore;
    public static String INTENT_KEY = "INTENT_KEY";
    File apkFile;

    public static UpdateDialogFragment newInstance(Bundle args) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
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
        //是否可以取消,会影响上面那条属性
//        setCancelable(false);
//        //window外可以点击,不拦截窗口外的事件
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (mUpdateApp != null && mUpdateApp.getAppEnForce() == 1) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            if (mContext != null) {
                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                lp.height = (int) (displayMetrics.heightPixels * 0.8f);
            }
            dialogWindow.setAttributes(lp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_update_app_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);
        //忽略
        mIgnore = view.findViewById(R.id.tv_ignore);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        if (getArguments() != null) {
            mUpdateApp = (UploadBean) getArguments().getSerializable(INTENT_KEY);
        }
        //设置主题色
        initTheme();
        if (mUpdateApp != null) {
            //弹出对话框
            final String dialogTitle = mUpdateApp.getTitle();
            final String newVersion = "";
            final String targetSize = "";
            final String updateLog = mUpdateApp.getName();

            String msg = "";

            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：" + targetSize + "\n\n";
            }

            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog;
            }

            //更新内容
            mContentTextView.setText(msg);
            //标题
            mTitleTextView.setText(TextUtils.isEmpty(dialogTitle) ? String.format("是否升级到%s版本？", newVersion) : dialogTitle);
            //强制更新
            if (mUpdateApp.getAppEnForce() == 1) {
                mLlClose.setVisibility(View.GONE);
            } else {
                //不是强制更新时，才生效
                mIgnore.setVisibility(View.GONE);
            }

            initEvents();
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {
        setDialogTheme(mDefaultColor, mDefaultPicResId);
    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackground(DrawableUtil.getDrawable(dip2px(4, mContext), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mIgnore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                    ToastCompat.makeText(mContext, TIPS, Toast.LENGTH_SHORT).show();
                } else {
                    // 申请授权。
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                installApp();
            }
        } else if (i == R.id.iv_close) {
            // 这里是否要对UpdateAppBean的强制更新做处理？不会重合，当强制更新时，就不会显示这个按钮，也不会调这个方法。
//            if (mNumberProgressBar.getVisibility() == View.VISIBLE) {
//                ToastCompat.makeText(getApplicationContext(), "后台更新app", Toast.LENGTH_LONG).show();
//            }
            cancelDownloadService();
//            if (mUpdateDialogFragmentListener != null) {
//                // 通知用户
//                mUpdateDialogFragmentListener.onUpdateNotifyDialogCancel(mUpdateApp);
//            }
            dismiss();
        } else if (i == R.id.tv_ignore) {
//            AppUpdateUtil.saveIgnoreVersion(getActivity(), mUpdateApp.getNewVersion());
            dismiss();
        }
    }

    public void cancelDownloadService() {
//        if (mDownloadBinder != null) {
//            // 标识用户已经点击了更新，之后点击取消
//            mDownloadBinder.stop("取消下载");
//        }
    }

    private void installApp() {
        mUpdateOkButton.setEnabled(false);
        File root = new File(Constants.ROOT_PATH);
        if (!root.exists()) {
            root.mkdir();
        }
        File file = new File(Constants.FILE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        apkFile = new File(file, getApkName(mUpdateApp));
        try {
            if (apkFile.exists()) {
                apkFile.delete();
            }
            apkFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadStart();
        ProgressManager.getInstance().addResponseListener(mUpdateApp.getSrcURL(), new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                mUpdateOkButton.setVisibility(View.GONE);
                mNumberProgressBar.setVisibility(View.VISIBLE);
                mNumberProgressBar.setMax((int) progressInfo.getContentLength());
                mNumberProgressBar.setProgress((int) progressInfo.getCurrentbytes());
                if (progressInfo.isFinish()) {
                    //下载完成
                    if (AppUpdateUtil.installApp(mContext, apkFile)) {
                        //成功跳转安装
                        if (isShow) {
                            System.exit(0);
                        }
                    } else {
                        //跳转失败
                        if (isShow) {
                            System.exit(0);
                        }
                    }
                }
            }

            @Override
            public void onError(long id, Exception e) {
                e.printStackTrace();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateOkButton.setVisibility(View.VISIBLE);
                        mUpdateOkButton.setEnabled(true);
                        mNumberProgressBar.setVisibility(View.GONE);
                        mNumberProgressBar.setProgress(0);
                        ToastCompat.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void downloadStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(mUpdateApp.getSrcURL())
                            .build();

                    Response response = ProgressManager.getInstance().with(new OkHttpClient.Builder()).build().newCall(request).execute();
                    InputStream is = response.body().byteStream();
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    //当外部发生错误时,使用此方法可以通知所有监听器的 onError 方法
                    ProgressManager.getInstance().notifyOnErorr(mUpdateApp.getSrcURL(), e);
                }
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //升级
                installApp();
            } else {
                //提示，并且关闭
                ToastCompat.makeText(mContext, TIPS, Toast.LENGTH_SHORT).show();
                dismiss();
                if (mUpdateApp.getAppEnForce() == 1) {
                    //强制更新 关闭程序
                    System.exit(0);
                }
            }
        }
    }

    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        //使用ApplicationContext延长他的生命周期
//        DownloadService.bindService(getActivity().getApplicationContext(), conn);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("", "对话框 requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
//        switch (resultCode) {
//            case Activity.RESULT_CANCELED:
//                switch (requestCode){
//                    // 得到通过UpdateDialogFragment默认dialog方式安装，用户取消安装的回调通知，以便用户自己去判断，比如这个更新如果是强制的，但是用户下载之后取消了，在这里发起相应的操作
//                    case AppUpdateUtil.REQ_CODE_INSTALL_APP:
//                        if (mUpdateApp.isConstraint()) {
//                            if (AppUpdateUtil.appIsDownloaded(mUpdateApp)) {
//                                AppUpdateUtil.installApp(UpdateDialogFragment.this, AppUpdateUtil.getAppFile(mUpdateApp));
//                            }
//                        }
//                        break;
//                }
//                break;
//
//            default:
//        }
//    }

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

