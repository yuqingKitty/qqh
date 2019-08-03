package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.CompleteBean;

import java.util.List;

public interface ICompleteView extends IBaseView {
    /**
     * 获取排序列表
     */
    void getProductSortLabelList(List<CompleteBean.ProductSortLabel> data);

    /**
     * 加载列表成功
     */
    void loadDataSuccess(List<CompleteBean.ProductBean> data);

    /**
     * 刷新列表成功
     */
    void refreshDataSuccess(List<CompleteBean.ProductBean> data);

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

    /**
     * 记录成功
     *
     * @param url
     * @param id
     */
    void onRecordSuccess(String url, String id);

}
