package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.BaseBean;

public interface IRegisterView<T extends BaseBean> extends IBaseView {
    void registerSuccess();

    void portError(String des);

    void getSmsSuccess();

}
