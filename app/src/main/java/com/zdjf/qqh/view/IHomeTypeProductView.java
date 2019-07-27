package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.HomeTypeProductBean;

import java.util.List;

public interface IHomeTypeProductView extends IBaseView{
    /**
     * 获取产品列表成功
     */
    void loadTypeProductDataSuccess(List<HomeTypeProductBean.TypeProductBean> data);

    /**
     * 刷新产品列表成功
     */
    void refreshTypeProductDataSuccess(List<HomeTypeProductBean.TypeProductBean> data);

    /**
     * 产品没有更多数据
     */
    void noTypeProductMordData();

    /**
     * 加载更多失败
     */
    void loadTypeProductMordFail();

    /**
     * 清除数据
     */
    void clearTypeProductData();

    /**
     * 获取推荐列表
     * @param data
     */
    void loadTypeAdDataSuccess(List<HomeTypeProductBean.BannerBean> data);

    /**
     * 记录成功
     *
     * @param url
     * @param id
     */
    void onRecordSuccess(String url, String id);
}
