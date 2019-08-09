package com.leuters.qqh.ui.base;

import android.app.Activity;
import android.content.Context;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.module.LoanModule;
import com.leuters.qqh.utils.IntentUtil;
import com.leuters.qqh.utils.rxbus.RxBus;
import com.leuters.qqh.view.IBaseView;

import java.lang.ref.WeakReference;

import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;

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
