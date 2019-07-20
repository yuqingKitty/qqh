package com.zdjf.qqh.presenter;

import android.app.Activity;

import com.zdjf.qqh.ui.base.BasePresenter;
import com.zdjf.qqh.view.IMineView;


public class MinePresenter extends BasePresenter<IMineView> {
    public MinePresenter(Activity context, IMineView view) {
        super(context, view);
    }
}
