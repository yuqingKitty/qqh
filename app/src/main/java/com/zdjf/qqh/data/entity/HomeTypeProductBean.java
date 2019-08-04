package com.zdjf.qqh.data.entity;

import java.util.List;

public class HomeTypeProductBean extends BaseBean {
    public List<BannerBean> advertisementList;
    public List<TypeProductBean> productList;

    public class BannerBean {
        public String id;
        public String index;
        public String imageURL;
        public String srcURL; // 链接跳转地址
        public String productId;
    }

    public class TypeProductBean {
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
