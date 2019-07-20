package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.BaseBean;

public interface IServiceView<T extends BaseBean> extends IBaseView {
    void getDataSuccess(T bean);

    void getDataFailed();
}
