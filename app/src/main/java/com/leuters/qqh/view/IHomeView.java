package com.leuters.qqh.view;

import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.HomeBean;
import com.leuters.qqh.data.entity.VerifyUserTokenBean;

import java.util.List;

public interface IHomeView<T extends BaseBean> extends IBaseView {
    void fillHeadData(T data); // 加载头部数据

    /**
     * 加载列表成功
     */
    void loadProductDataSuccess(List<HomeBean.ProductBean> data);

    /**
     * 刷新列表成功
     */
    void refreshProductDataSuccess(List<HomeBean.ProductBean> data);

    /**
     * 没有更多数据
     */
    void noMoreProductData();

    /**
     * 清除数据
     */
    void clearProductData();

    /**
     * 加载更多失败
     */
    void loadMordProductFail();

    void showErrorView(String throwable);

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
