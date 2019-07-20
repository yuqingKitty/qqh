package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.ImageUtil;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IFeedbackView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

import static com.zdjf.qqh.data.commons.Constants.FEEDBACK_IMG;

public class FeedbackPresenter extends BasePresenter<IFeedbackView> {
    public FeedbackPresenter(Activity context, IFeedbackView view) {
        super(context, view);
    }

    /**
     * 提交反馈
     *
     * @param content            内容
     * @param imageURL           第一张图片
     * @param imageTwoURL        第二张图片
     * @param contactInformation 联系方式
     */
    public void submitFeedback(String content, String imageURL, String imageTwoURL, String contactInformation) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", BaseApplication.getUserId(mContext));
        params.put("token", BaseApplication.getToken(mContext));
        params.put("content", content);
        params.put("imageURL", imageURL);
        params.put("imageTwoURL", imageTwoURL);
        params.put("contactInformation", contactInformation);
        mModel.submitFeedBack(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean baseBean) {
                if (parse(mContext, baseBean)) {
                    obtainView().submitSuccess();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }

    /**
     * 上传图片
     *
     * @param file
     */
    public void uploadPic(final File file) {
        obtainView().showLoading();
        ImageUtil.upPicToAliyun(mContext, FEEDBACK_IMG, file, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        obtainView().uploadSuccess(ImageUtil.getAliyunLink(FEEDBACK_IMG, file.getName()));
                        obtainView().hideLoading();
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        obtainView().uploadFailed();
                        obtainView().hideLoading();
                    }
                });
            }
        });
    }
}
