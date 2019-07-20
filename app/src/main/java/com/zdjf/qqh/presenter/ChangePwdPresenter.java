package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IChangePwdView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class ChangePwdPresenter extends BasePresenter<IChangePwdView> {
    public ChangePwdPresenter(Activity context, IChangePwdView view) {
        super(context, view);
    }

    public void changePwd(String oldPassword, String newPassword) {
        Map<String, Object> params = new HashMap<>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        params.put("userId", BaseApplication.getUserId(mContext));
        params.put("token", BaseApplication.getToken(mContext));
        mModel.changePwd(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean o) {
                if (parse(mContext, o)) {
                    obtainView().changeSuccess();
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
