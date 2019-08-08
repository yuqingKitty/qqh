package com.leuters.qqh.presenter;

import android.app.Activity;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.LoginBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.ImageUtil;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.ISettingView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

import static com.leuters.qqh.data.commons.Constants.USER_IMG;

public class SettingPresenter extends BasePresenter<ISettingView> {
    public SettingPresenter(Activity context, ISettingView view) {
        super(context, view);
    }

    public void upLoadPic(final File file) {
        ImageUtil.upPicToAliyun(mContext, USER_IMG, file, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Map<String, Object> params = new HashMap<>();
                params.put("imageIcon", ImageUtil.getAliyunLink(USER_IMG, file.getName()));
                mModel.uploadHead(params, new DisposableObserver<BaseBean>() {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (parse(mContext, baseBean)) {
                            obtainView().uploadSuccess(ImageUtil.getAliyunLink(USER_IMG, file.getName()));
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

            @Override
            public void onFailure(final PutObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 请求异常
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            clientException.printStackTrace();
                            obtainView().ShowToast("网络异常");
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                            obtainView().ShowToast("服务异常");
                        }
                        if (request == null) {
                            obtainView().ShowToast("服务异常");
                        }
                        obtainView().hideLoading();
                    }
                });
            }
        });

    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        obtainView().showLoading();
        mModel.getUserInfo(new HashMap<String, Object>(), new DisposableObserver<LoginBean>() {

            @Override
            public void onNext(LoginBean bean) {
                if (parse(mContext, bean)) {
                    obtainView().getUserInfoSuccess(bean);
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
     * 退出登陆
     */
    public void logout() {
        obtainView().showLoading();
        mModel.logout(new HashMap<String, Object>(), new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean baseBean) {
                if (parse(mContext, baseBean)) {
                    obtainView().logoutSuccess();
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
}
