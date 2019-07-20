package com.zdjf.qqh.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.presenter.FeedbackPresenter;
import com.zdjf.qqh.ui.base.BaseActivity;
import com.zdjf.qqh.ui.customview.ActionSheetDialog;
import com.zdjf.qqh.ui.customview.CustomDialog;
import com.zdjf.qqh.utils.AddSpaceTextWatcher;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.zdjf.qqh.utils.ImageUtil;
import com.zdjf.qqh.utils.JFileUtil;
import com.zdjf.qqh.utils.RegexUtil;
import com.zdjf.qqh.view.IFeedbackView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zdjf.qqh.data.commons.Constants.CHOOSE_PICTURE;
import static com.zdjf.qqh.data.commons.Constants.INTENT_REQUESTCODE_CAMERA_SYSTEM;
import static com.zdjf.qqh.utils.IntentUtil.openFile;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity<FeedbackPresenter> implements IFeedbackView {
    /**
     * 意见内容
     */
    @BindView(R.id.feedback_content)
    EditText mFeedbackContent;
    /**
     * 联系方式
     */
    @BindView(R.id.feedback_contact)
    EditText mFeedbackContact;
    /**
     * 提交按钮
     */
    @BindView(R.id.feedback_submit)
    Button mSubmitFeedback;

    @BindView(R.id.feedback_img1)
    ImageView mFeedbackImgF;

    @BindView(R.id.feedback_img2)
    ImageView mFeedbackImgS;
    /**
     * 添加图片提示
     */
    @BindView(R.id.add_pic_hint)
    TextView add_pic_hint;
    /**
     * 第一张图地址
     */
    String imgSLink;
    /**
     * 第二张图地址
     */
    String imgFLink;

    private ActionSheetDialog dialog;
    private static File photoPath;//图片保存目录
    private File mFile;
    private Intent cameraIntent;  //相机
    private Uri photoUri = null;   //拍照
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            imgSLink = savedInstanceState.getString("imgSLink");
            imgFLink = savedInstanceState.getString("imgFLink");
            photoUri = savedInstanceState.getParcelable("photoUri");
            position = savedInstanceState.getInt("position");
            String filePath = savedInstanceState.getString("mFile");
            if (!TextUtils.isEmpty(filePath)) {
                mFile = new File(filePath);
            }
            switch (position) {
                case 0:
                    if (!TextUtils.isEmpty(imgFLink)) {
                        GlideImageLoader.setImg(FeedbackActivity.this, imgFLink, mFeedbackImgF, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
                        add_pic_hint.setVisibility(View.GONE);
                        mFeedbackImgS.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                    if (!TextUtils.isEmpty(imgFLink)) {
                        GlideImageLoader.setImg(FeedbackActivity.this, imgFLink, mFeedbackImgF, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
                    }
                    if (!TextUtils.isEmpty(imgSLink)) {
                        GlideImageLoader.setImg(FeedbackActivity.this, imgSLink, mFeedbackImgS, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
                    }
                    add_pic_hint.setVisibility(View.GONE);
                    mFeedbackImgS.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new FeedbackPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        AddSpaceTextWatcher asEditTexts = new AddSpaceTextWatcher(mFeedbackContent, 200, false);
        asEditTexts.setButtonListen(mSubmitFeedback);
        photoPath = getExternalCacheDir();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showLoading() {
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @OnClick({R.id.feedback_submit, R.id.feedback_img1, R.id.feedback_img2})
    void clickView(View view) {
        switch (view.getId()) {
            case R.id.feedback_submit:
                //提交反馈
                String content = mFeedbackContent.getText().toString().trim().replace("\n", "");
                if (content.length() < 5) {
                    showToast("反馈内容至少输入5个字符");
                    return;
                }
                String contact = mFeedbackContact.getText().toString().trim();
                if (!TextUtils.isEmpty(contact) && !RegexUtil.isMobileSimple(contact) && !RegexUtil.isEmail(contact)) {
                    showToast("联系方式格式有误");
                    return;
                }
                mPresenter.submitFeedback(content, imgFLink, imgSLink,
                        mFeedbackContact.getText().toString().trim());
                break;
            case R.id.feedback_img1:
                showDialog();
                position = 0;
                break;
            case R.id.feedback_img2:
                showDialog();
                position = 1;
                break;
        }
    }

    @Override
    public void hideLoading() {
        if (mLoading != null) {
            mLoading.dismissLoading();
        }
    }

    @Override
    public void submitSuccess() {
        new CustomDialog(this).builder().setTitle(getResources().getString(R.string.default_dialog_title))
                .setMsg("反馈成功，感谢您的支持！").setPositiveButton("好的，知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).showNegativeButton(false).show();
    }

    @Override
    public void uploadSuccess(String picLink) {
        switch (position) {
            case 0:
                imgFLink = picLink;
                GlideImageLoader.setImg(FeedbackActivity.this, picLink, mFeedbackImgF, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
                add_pic_hint.setVisibility(View.GONE);
                mFeedbackImgS.setVisibility(View.VISIBLE);
                break;
            case 1:
                imgSLink = picLink;
                GlideImageLoader.setImg(FeedbackActivity.this, picLink, mFeedbackImgS, R.mipmap.bitmap_wechat, R.mipmap.bitmap_wechat);
                break;
        }
    }

    @Override
    public void uploadFailed() {
        showToast("图片上传失败，请重试");
    }

    void showDialog() {
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
                    AndPermission.with(FeedbackActivity.this).runtime()
                            .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    mFile = new File(photoPath, ImageUtil.getPicName(FeedbackActivity.this));
                                    try {
                                        if (mFile.exists()) {
                                            mFile.delete();
                                        }
                                        mFile.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        photoUri = FileProvider.getUriForFile(FeedbackActivity.this, getApplicationContext().getPackageName() + ".provider", mFile);
                                    } else {
                                        photoUri = Uri.fromFile(mFile);
                                    }
                                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                    FeedbackActivity.this.startActivityForResult(cameraIntent, INTENT_REQUESTCODE_CAMERA_SYSTEM);
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
                    AndPermission.with(FeedbackActivity.this).runtime()
                            .permission(Permission.Group.STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override

                                public void onAction(List<String> permissions) {
                                    openFile(FeedbackActivity.this);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (data != null && resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = JFileUtil.getPathByUri4kitkat(this, uri);
                    File file = new File(path);
                    mPresenter.uploadPic(file);

                }
                break;
            case INTENT_REQUESTCODE_CAMERA_SYSTEM:
                if (resultCode == RESULT_OK) {
                    if (mFile != null) {
                        mPresenter.uploadPic(mFile);
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imgSLink", imgSLink);
        outState.putString("imgFLink", imgFLink);
        outState.putParcelable("photoUri", photoUri);
        outState.putInt("position", position);
        if (mFile != null) {
            outState.putString("mFile", mFile.getAbsolutePath());
        }
    }

    private String pageTitle = "意见反馈";

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
