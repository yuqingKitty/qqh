package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.data.entity.ServiceBean;
import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.view.IServiceView;

import java.util.HashMap;

import io.reactivex.observers.DisposableObserver;

/**
 * 客服
 */
public class ServicePresenter extends BasePresenter<IServiceView> {
    public ServicePresenter(Activity context, IServiceView view) {
        super(context, view);
    }

    public void getData() {
        obtainView().showLoading();
        mModel.service(new HashMap<String, Object>(), new DisposableObserver<ServiceBean>() {

            @Override
            public void onNext(ServiceBean serviceBean) {
                if (parse(mContext, serviceBean)) {
                    obtainView().getDataSuccess(serviceBean);
                } else {
                    obtainView().getDataFailed();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().getDataFailed();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });
    }
}