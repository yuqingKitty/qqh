package com.leuters.qqh.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.module.LoanModule;
import com.leuters.qqh.utils.rxbus.RxBus;

import java.util.HashMap;

import io.reactivex.observers.DisposableObserver;

import static com.leuters.qqh.data.commons.Constants.RXBUS_LOGOUT_SUCCESS_KEY;

public class TokenVerifyUtil {

    public static void verifyUserInfo(final Activity context){
        LoanModule loanModule = new LoanModule(context);
        loanModule.getTokenState(new HashMap<String, Object>(), new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean baseBean) {
                Log.e("yuq", "onNext"+baseBean.getStatus() + baseBean.getDes());
                if (baseBean.getStatus() == 0) {
                    IntentUtil.toMainActivity(context);
                    context.finish();
                } else {
                    BaseApplication.ClearUser(context);
                    RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_LOGOUT_SUCCESS_KEY));
                    Toast.makeText(context, baseBean.getDes(), Toast.LENGTH_SHORT).show();
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
