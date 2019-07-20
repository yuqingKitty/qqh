package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.data.entity.ProtocolBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.IProtocolView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class ProtocolPresenter extends BasePresenter<IProtocolView> {
    public ProtocolPresenter(Activity context, IProtocolView view) {
        super(context, view);
    }

    public void getProtocol(String cmsType) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("cmsType", cmsType);
        mModel.cms(params, new DisposableObserver<ProtocolBean>() {
            @Override
            public void onNext(ProtocolBean protocolBean) {
                if (parse(mContext, protocolBean)) {
                    obtainView().cmsSuccess(protocolBean);
                } else {
                    obtainView().cmsError();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
                obtainView().hideLoading();
                obtainView().cmsError();
            }

            @Override
            public void onComplete() {
                obtainView().hideLoading();
            }
        });

    }
}
