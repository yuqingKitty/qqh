package com.zdjf.qqh.view;

import com.zdjf.qqh.data.entity.MyLoanRecordBean;

import java.util.List;

public interface IMyLoanRecordView extends IBaseView{
    /**
     * 获取申请列表成功
     */
    void loadLoanRecordDataSuccess(List<MyLoanRecordBean.MyLoanBean> data);

    /**
     * 刷新申请列表成功
     */
    void refreshLoanRecordDataSuccess(List<MyLoanRecordBean.MyLoanBean> data);

    /**
     * 申请没有更多数据
     */
    void noLoanRecordMordData();

    /**
     * 加载更多失败
     */
    void loadLoanRecordMordFail();

    /**
     * 清除数据
     */
    void clearLoanRecordData();

    /**
     * 获取推荐列表
     * @param data
     */
    void loadRecommendProductDataSuccess(List<MyLoanRecordBean.MyRecommendProductBean> data);

    /**
     * 记录成功
     *
     * @param url
     * @param id
     */
    void onRecordSuccess(String url, String id);
}
