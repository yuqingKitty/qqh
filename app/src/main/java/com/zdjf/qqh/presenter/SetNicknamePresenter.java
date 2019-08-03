package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.view.ISetNicknameView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class SetNicknamePresenter extends BasePresenter<ISetNicknameView> {
    public SetNicknamePresenter(Activity context, ISetNicknameView view) {
        super(context, view);
    }

    /**
     * 设置昵称
     *
     * @param nickname
     */
    public void editNickname(final String nickname) {
        obtainView().showLoading();
        Map<String, Object> params = new HashMap<>();
        params.put("nickname", nickname);
        mModel.setNickname(params, new DisposableObserver<BaseBean>() {
            @Override
            public void onNext(BaseBean baseBean) {
                if (parse(mContext, baseBean)) {
                    obtainView().changeSuccess(nickname);
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
