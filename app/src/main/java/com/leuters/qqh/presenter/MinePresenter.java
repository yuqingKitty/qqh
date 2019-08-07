package com.leuters.qqh.presenter;

import android.app.Activity;

import com.leuters.qqh.ui.base.BasePresenter;
import com.leuters.qqh.view.IMineView;


public class MinePresenter extends BasePresenter<IMineView> {
    public MinePresenter(Activity context, IMineView view) {
        super(context, view);
    }
}
