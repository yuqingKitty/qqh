package com.zdjf.qqh.ui.base;

import android.app.Activity;
import android.content.Context;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.module.LoanModule;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.zdjf.qqh.view.IBaseView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;

public abstract class BasePresenter<GV extends IBaseView> {
    protected WeakReference<GV> mView;
    protected Activity mContext;
    protected LoanModule mModel;

    public BasePresenter(Activity context, GV view) {
        mContext = context;
        mView = new WeakReference<>(view);
        this.mModel = new LoanModule(context);
    }

    public boolean parse(final Context context, Object obj) {
        BaseBean baseBean = (BaseBean) obj;
        if (baseBean == null) {
            obtainView().ShowToast("数据异常");
            return false;
        }
        switch (baseBean.getStatus()) {
            case 0:
                return true;
            case -103:
                //登陆过期
                BaseApplication.ClearUser(context);
                IntentUtil.toLoginActivity((Activity) context);
                obtainView().ShowToast(baseBean.getDes());
                RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
                return false;
            default:
                obtainView().ShowToast(baseBean.getDes());
                return false;
        }
    }

    /**
     * 产品点击统计
     */
    public void simpleRecord(String productId, String module_name, String module_order) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", BaseApplication.getUserId(mContext));
        params.put("token", BaseApplication.getToken(mContext));
        params.put("productId", productId);
        params.put("module_name", module_name);
        params.put("module_order", module_order);

        mModel.toStatistics(params, new DisposableObserver<BaseBean>() {

            @Override
            public void onNext(BaseBean baseBean) {
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                obtainView().ShowToast("网络异常");
            }

            @Override
            public void onComplete() {
            }
        });

    }

    public void detach() {
        if (isAttach()) {
            mView.clear();
            mView = null;
        }
    }

    public GV obtainView() {
        return isAttach() ? mView.get() : null;
    }

    private boolean isAttach() {
        return mView != null &&
                mView.get() != null;
    }
}
