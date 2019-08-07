package com.leuters.qqh.view;

import com.leuters.qqh.data.entity.BaseBean;

public interface ISettingView<T extends BaseBean> extends IBaseView {
    void getUserInfoSuccess(T bean);

    void logoutSuccess();

    void uploadSuccess(String link);
}
