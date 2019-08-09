package com.leuters.qqh.view;

import com.leuters.qqh.data.entity.MessageCenterBean;

import java.util.List;

public interface IMessageCenterView extends IBaseView {
    /**
     * 获取列表成功
     */
    void loadDataSuccess(List<MessageCenterBean.MessageBean> data);

    /**
     * 刷新列表成功
     */
    void refreshDataSuccess(List<MessageCenterBean.MessageBean> data);

    /**
     * 没有更多数据
     */
    void noMordData();

    /**
     * 加载更多失败
     */
    void loadMordFail();

    /**
     * 清除数据
     */
    void clearData();

}
