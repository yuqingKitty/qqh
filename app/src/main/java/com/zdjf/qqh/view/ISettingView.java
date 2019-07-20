package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.BaseBean;

public interface ISettingView<T extends BaseBean> extends IBaseView {
    void getUserInfoSuccess(T bean);

    void logoutSuccess();

    void uploadSuccess(String link);
}
