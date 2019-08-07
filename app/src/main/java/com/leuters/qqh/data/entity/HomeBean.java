package com.leuters.qqh.data.entity;

import java.util.List;

public class HomeBean extends BaseBean {
    private List<BannerBean> advertisementList;
    private List<NoticeBean> systemNotifyList;
    private List<TypeBean> prodTypeList;
    private List<RecommendProductBean> prodRecommendList;  // 推荐list
    private List<ProductBean> productList;  // 列表list

    /**
     * banner
     */
    public class BannerBean {
        public String id;
        public String index;
        public String imageURL;
        public String srcURL; // 链接跳转地址
        public String productId;
    }

    /**
     * 通告
     */
    public class NoticeBean {
        public String id;
        public String msg;
    }

    /**
     * 产品类型
     */
    public class TypeBean {
        public String id;
        public String locationNo;  // 排序
        public String name; // 类型名字
        public String iconPath;
    }

    // 推荐产品
    public class RecommendProductBean {
        public String id;
        public String productId;
        public String logoUrl;
        public String name;
        public String des;
        public String link;
    }

    // 产品
    public class ProductBean {
        public String id;
        public String productId;
        public String logoUrl;
        public String name;
        public String link;
        public String prodLoanRange;
        public String prodLoanTerm;
        public String prodLoanInterest;
        public String prodLoanTime;
        public String prodLoanFeature;
    }

    public List<BannerBean> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<BannerBean> advertisementList) {
        this.advertisementList = advertisementList;
    }

    public List<NoticeBean> getSystemNotifyList() {
        return systemNotifyList;
    }

    public void setSystemNotifyList(List<NoticeBean> systemNotifyList) {
        this.systemNotifyList = systemNotifyList;
    }

    public List<TypeBean> getProdTypeList() {
        return prodTypeList;
    }

    public void setProdTypeList(List<TypeBean> prodTypeList) {
        this.prodTypeList = prodTypeList;
    }

    public List<RecommendProductBean> getProdRecommendList() {
        return prodRecommendList;
    }

    public void setProdRecommendList(List<RecommendProductBean> prodRecommendList) {
        this.prodRecommendList = prodRecommendList;
    }

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

}
