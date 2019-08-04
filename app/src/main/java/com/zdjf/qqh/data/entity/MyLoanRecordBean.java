package com.zdjf.qqh.data.entity;

import java.util.List;

/**
 * 我的申请记录
 */
public class MyLoanRecordBean {

    public List<MyLoanBean> myLoanList;
    public List<MyRecommendProductBean> myRecommendProductList;

    public class MyLoanBean {
        public String id;
        public String productId;
        public String logoUrl;
        public String name;
        public long createDate;
    }

    public class MyRecommendProductBean {
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
}

