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
        public String productLogoUrl;
        public String productName;
        public long productApplyTime;
    }

    public class MyRecommendProductBean {
        public String id;
        public String logoURL;
        public String name;
        public String tagDesc;
        public String loanRange;
        public String loanTerm;
        public String marketPhrases;
        public String link;
    }
}

