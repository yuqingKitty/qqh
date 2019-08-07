package com.leuters.qqh.view;

import com.leuters.qqh.data.entity.BaseBean;

public interface IServiceView<T extends BaseBean> extends IBaseView {
    void getDataSuccess(T bean);

    void getDataFailed();
}
