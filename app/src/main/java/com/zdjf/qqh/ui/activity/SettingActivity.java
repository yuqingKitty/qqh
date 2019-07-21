package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.LoginBean;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.data.entity.UserBean;
import com.zdjf.qqh.presenter.SettingPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.ActionSheetDialog;
import com.zdjf.qqh.ui.customview.CustomDialog;
import com.zdjf.qqh.ui.customview.TopBarView;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.zdjf.qqh.utils.ImageUtil;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.JFileUtil;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.view.ISettingView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.zdjf.qqh.data.commons.Constants.CHOOSE_PICTURE;
import static com.zdjf.qqh.data.commons.Constants.INTENT_REQUESTCODE_CAMERA_SYSTEM;
import static com.zdjf.qqh.data.commons.Constants.PHOTO_REQUEST_RESULT;
import static com.zdjf.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;
import static com.zdjf.qqh.data.commons.Constants.RXBUS_NICKNAME_SUCCESS_KEY;
import static com.zdjf.qqh.data.commons.Constants.RXBUS_UPLOAAD_SUCCESS_KEY;
import static com.zdjf.qqh.utils.ImageUtil.getBitmapFormUri;
import static com.zdjf.qqh.utils.IntentUtil.openFile;
import static com.zdjf.qqh.utils.IntentUtil.startPhotoZoom;
import static com.gyf.barlibrary.OSUtils.isMIUI;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity<SettingPresenter> implements ISettingView<LoginBean> {
    private RxBus rxBus;
    /**
     * 用户头像
     */
    @BindView(R.id.head_view)
    ImageView mHeadView;
    /**
     * 用户手机号
     */
    @BindView(R.id.phone_num)
    TextView mPhoneNum;

    @BindView(R.id.nickname_tv)
    TextView mUserName;
    private ActionSheetDialog dialog;

    private static File photoPath;//图片保存目录
    private File hearFile;

    private Intent cameraIntent;  //相机
    private Uri photoUri = null;   //拍照
    private Uri imageUri;   //裁剪的图片uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TopBarView)findViewById(R.id.top_view)).setTitleBold();
        if (savedInstanceState != null) {
            String hearFilePath = savedInstanceState.getString("hearFile", "");
            if (!TextUtils.isEmpty(hearFilePath)) {
                hearFile = new File(savedInstanceState.getString("hearFile", ""));
            }
            imageUri = savedInstanceState.getParcelable("imageUri");
            if (hearFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoUri = FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", hearFile);
                } else {
                    photoUri = Uri.fromFile(hearFile);
                }
            }
        }
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SettingPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        photoPath = getExternalCacheDir();
        mPresenter.getUserInfo();
        initRxBus();
    }

    private void initRxBus() {
        rxBus = RxBus.getInstanceBus();
        registerRxBus(RxBusMessage.class, new Consumer<RxBusMessage>() {
            @Override
            public void accept(@NonNull RxBusMessage rxBusMessage) throws Exception {
                //根据事件类型进行处理
                switch (rxBusMessage.getCode()) {
                    case RXBUS_NICKNAME_SUCCESS_KEY://昵称设置成功
                        mUserName.setText(rxBusMessage.getMsg());
                        break;
                }
            }
        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
        rxBus.addSubscription(this, disposable);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick({R.id.head_view_layout, R.id.edit_pwd, R.id.logout_view, R.id.set_nickname_layout})
    void click(View view) {
        switch (view.getId()) {
            case R.id.head_view_layout:
                //点击更换头像
                if (dialog == null) {
                    dialog = new ActionSheetDialog(this).builder().addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Default, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            //调起拍照
                            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            if (TextUtils.isEmpty(basePath)) {
                                showToast("暂无外部存储，请检查外部存储连接");
                                return;
                            }
                            AndPermission.with(SettingActivity.this).runtime()
                                    .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                                    .onGranted(new Action<List<String>>() {
                                        @Override
                                        public void onAction(List<String> permissions) {
                                            hearFile = new File(photoPath, ImageUtil.getPicName(SettingActivity.this));
                                            try {
                                                if (hearFile.exists()) {
                                                    hearFile.delete();
                                                }
                                                hearFile.createNewFile();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                photoUri = FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", hearFile);
                                            } else {
                                                photoUri = Uri.fromFile(hearFile);
                                            }
                                            cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                            SettingActivity.this.startActivityForResult(cameraIntent, INTENT_REQUESTCODE_CAMERA_SYSTEM);
                                        }
                                    }).onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    showToast("权限获取失败");
                                }
                            }).start();
                        }
                    }).addSheetItem("相册选择", ActionSheetDialog.SheetItemColor.Default, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            AndPermission.with(SettingActivity.this).runtime()
                                    .permission(Permission.Group.STORAGE)
                                    .onGranted(new Action<List<String>>() {
                                        @Override

                                        public void onAction(List<String> permissions) {
                                            openFile(SettingActivity.this);
                                        }
                                    }).onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    showToast("权限获取失败");
                                }
                            }).start();
                        }
                    });
                    dialog.addView();
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                break;
            case R.id.edit_pwd:
                //点击修改密码
                IntentUtil.toChangePwdActivity(this);
                break;
            case R.id.logout_view:
                //退出登陆
                new CustomDialog(SettingActivity.this).builder().setTitle(getResources().getString(R.string.default_dialog_title)).setMsg(getResources().getString(R.string.logout_msg)).setNegativeButton("", null)
                        .setPositiveButton("", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.logout();
                            }
                        }).show();
                break;
            case R.id.set_nickname_layout:
                //设置用户名
                IntentUtil.toSetNickname(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (data != null && resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = JFileUtil.getPathByUri4kitkat(this, uri);
                    File file = new File(path);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isMIUI()) {
                        startPhotoZoomMIUI(FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", file));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        startPhotoZoom(this, FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", file));
                    } else {
                        startPhotoZoom(this, Uri.fromFile(file));
                    }
                }
                break;
            case INTENT_REQUESTCODE_CAMERA_SYSTEM:
                if (resultCode == RESULT_OK && hearFile != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isMIUI()) {
                        startPhotoZoomMIUI(FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", hearFile));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        startPhotoZoom(this, FileProvider.getUriForFile(SettingActivity.this, getApplicationContext().getPackageName() + ".provider", hearFile));
                    } else {
                        startPhotoZoom(this, Uri.fromFile(hearFile));
                    }
                }
                break;
            case PHOTO_REQUEST_RESULT:
                if (data != null) { // 上传文件
                    createTempHeadIconFile(data);
                }
                break;
        }
    }

    @Override
    public void showLoading() {
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void hideLoading() {
        mLoading.dismissLoading();
    }

    /**
     * 图片剪裁缩放（小米）
     *
     * @param
     */
    private void startPhotoZoomMIUI(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File CropPhoto = new File(photoPath, "headcache.jpg");
            intent.putExtra("return-data", false);
            try {
                if (CropPhoto.exists()) {
                    CropPhoto.delete();
                }
                CropPhoto.createNewFile();
                imageUri = Uri.fromFile(CropPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        } else {
            intent.putExtra("return-data", true);
        }
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_RESULT);

    }

    /**
     * 对剪裁缩放后的图片压缩并生成临时头像文件
     */
    private void createTempHeadIconFile(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        String filePath = photoPath + ImageUtil.getPicName(SettingActivity.this);
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo != null) {
                ByteArrayOutputStream baos = null; // 压缩图片
                try {
                    baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                    byte[] photodata = baos.toByteArray();

                    if (JFileUtil.write(filePath, photodata)) {// 生成临时文件
                        // 上传文件
                        File file = new File(filePath);
                        mPresenter.upLoadPic(file);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {    //手动压缩上传图片
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = getBitmapFormUri(SettingActivity.this, imageUri);
                        File file = new File(filePath);
                        JFileUtil.saveBitmap(bitmap, file);
                        // 上传文件
                        mPresenter.upLoadPic(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (imageUri != null) {
                try {
                    Bitmap bitmap = getBitmapFormUri(SettingActivity.this, imageUri);
                    File file = new File(filePath);
                    JFileUtil.saveBitmap(bitmap, file);
                    // 上传文件
                    mPresenter.upLoadPic(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public void getUserInfoSuccess(LoginBean bean) {
        UserBean userBean = bean.getUserInfo();
        if (userBean == null) {
            return;
        }
        mPhoneNum.setText(userBean.getDecryptPhone());
        String userName = userBean.getNameFast();
        mUserName.setText(TextUtils.isEmpty(userName) ? "未设置昵称" : userName);
        GlideImageLoader.setCircleImg(SettingActivity.this, userBean.getImageFast(), mHeadView, R.mipmap.setup_head, R.mipmap.setup_head);
    }

    @Override
    public void logoutSuccess() {
        RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
        finish();
    }

    @Override
    public void uploadSuccess(String link) {
        GlideImageLoader.setCircleImg(SettingActivity.this, link, mHeadView, R.mipmap.setup_head, R.mipmap.setup_head);
        RxBusMessage message = new RxBusMessage(RXBUS_UPLOAAD_SUCCESS_KEY);
        message.setMsg(link);
        RxBus.getInstanceBus().post(message);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (hearFile != null) {
            outState.putString("hearFile", hearFile.getAbsolutePath());
        }
        if (imageUri != null) {
            outState.putParcelable("imageUri", imageUri);
        }
    }

    private String pageTitle = "设置";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(pageTitle);
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(pageTitle);
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }
}
