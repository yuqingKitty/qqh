package com.leuters.qqh.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.LoginBean;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.module.LoanModule;
import com.leuters.qqh.utils.rxbus.RxBus;

import java.util.HashMap;

import io.reactivex.observers.DisposableObserver;

import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGIN_SUCCESS_KEY;
import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;

public class JiGuangLoginUtil {

    public static void verifyUserInfo(final Activity context) {
        LoanModule loanModule = new LoanModule(context);
        loanModule.userLogin(new HashMap<String, Object>(), new DisposableObserver<LoginBean>() {
            @Override
            public void onNext(LoginBean bean) {
                if (parse(context, bean)) {
                    BaseApplication.setToken(context, bean.getToken());
                    BaseApplication.setUserId(context, String.valueOf(bean.getUid()));
                    BaseApplication.setUserInfo(context, bean.getUserVo());
                    RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGIN_SUCCESS_KEY));
                }
            }

            @Override
            public void onError(Throwable e) {
                BaseApplication.ClearUser(context);
                RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
                LogUtil.e(e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static boolean parse(final Context context, Object obj) {
        BaseBean baseBean = (BaseBean) obj;
        if (baseBean == null) {
            Toast.makeText(context, "数据异常",Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (baseBean.getStatus()) {
            case 0:
                return true;
            case -103:
                //登陆过期
                BaseApplication.ClearUser(context);
                IntentUtil.toLoginActivity((Activity) context);
                Toast.makeText(context, baseBean.getDes(),Toast.LENGTH_SHORT).show();
                RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
                return false;
            default:
                Toast.makeText(context, baseBean.getDes(),Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}
