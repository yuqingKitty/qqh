package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.ProtocolBean;

public interface IProtocolView<T extends ProtocolBean> extends IBaseView {
    void cmsSuccess(T bean);

    void cmsError();

}
