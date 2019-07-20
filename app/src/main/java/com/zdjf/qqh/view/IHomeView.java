package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.BaseBean;

public interface IHomeView<T extends BaseBean> extends IBaseView {
    /**
     * 添加更多数据（用于刷新）
     *
     * @param data
     */
    void appendMoreDataToView(T data);

    /**
     * 没有更多
     */
    void hasNoMoreData();

    /**
     * 列表没有数据
     */
    void noListData();

    void showErrorView(String throwable);

    void fillData(T data);

    void fillList(T data);

    void getHeadFinish();

    void getListFinish();

    void onRefreshFinish();

    /**
     * 获取列表失败
     */
    void getListFailed();

    /**
     * 刷新
     *
     * @param data
     */
    void onRefreshData(T data);

    /**
     * 记录成功
     *
     * @param url
     * @param id
     */
    void onRecordSuccess(String url, String id);

    /**
     * 检测更新
     */
    void checkUpdate(String srcURL, int appEnForce, String title, String name);
}
