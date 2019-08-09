package com.leuters.qqh.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.data.entity.VerifyUserTokenBean;
import com.leuters.qqh.module.LoanModule;
import com.leuters.qqh.utils.rxbus.RxBus;

import java.util.HashMap;

import io.reactivex.observers.DisposableObserver;

import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;

public class TokenVerifyUtil {

    public static void verifyUserInfo(final Activity context) {
        LoanModule loanModule = new LoanModule(context);
        loanModule.getTokenState(new HashMap<String, Object>(), new DisposableObserver<VerifyUserTokenBean>() {
            @Override
            public void onNext(VerifyUserTokenBean verifyUserTokenBean) {
                Log.e("yuq", "onNext" + verifyUserTokenBean.getStatus() + verifyUserTokenBean.getDes());
                if (verifyUserTokenBean.getStatus() == 0) {
                    BaseApplication.setToken(context, verifyUserTokenBean.token);
                    BaseApplication.setUserId(context, verifyUserTokenBean.uid);
                    IntentUtil.toMainActivity(context);
                    context.finish();
                } else {
                    BaseApplication.ClearUser(context);
                    RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
                    Toast.makeText(context, verifyUserTokenBean.getDes(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(e.toString());
                Log.e("yuq", "onError");
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
