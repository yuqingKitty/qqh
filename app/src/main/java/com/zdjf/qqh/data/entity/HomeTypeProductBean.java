package com.zdjf.qqh.data.entity;

import java.util.List;

public class HomeTypeProductBean extends BaseBean {
    public List<BannerBean> adList;
    public List<TypeProductBean> typeProductBeanList;

    public class BannerBean {
        public String index;
        /**
         * 图片地址
         */
        public String imageURL;
        /**
         * 链接跳转地址
         */
        public String srcURL;
        public String width;
        public String height;
    }

    public class TypeProductBean {
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
