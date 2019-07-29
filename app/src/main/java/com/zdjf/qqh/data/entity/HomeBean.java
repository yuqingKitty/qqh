package com.zdjf.qqh.data.entity;

import java.util.List;

public class HomeBean extends BaseBean {
    private List<BannerBean> adList;
    private List<NoticeBean> noticeList;
    private List<TypeBean> typePOs;
    private List<ProductBean> proRecommendVOs;  // 推荐list
    private List<ProductBean> productVOs;  // 列表list

    /**
     * banner
     */
    public class BannerBean {
        private String index;
        private String imageURL;
        private String srcURL; // 链接跳转地址
        private String width;
        private String height;

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getSrcURL() {
            return srcURL;
        }

        public void setSrcURL(String srcURL) {
            this.srcURL = srcURL;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }

    /**
     * 通告
     */
    public class NoticeBean {
        private String id;
        private String noticeIndex;
        private String phone;
        private String money;
        private String time;
        private String productId;
        private String logoUrl;
        private String name;
        private String des;
        private String number;
        private String link;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNoticeIndex() {
            return noticeIndex;
        }

        public void setNoticeIndex(String noticeIndex) {
            this.noticeIndex = noticeIndex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    /**
     * 产品类型
     */
    public class TypeBean {
        private String id;
        private String createDate; // 创建时间
        private String orderNumber;  // 排序
        private String name; // 类型名字
        private String enableFlag; // 是否启用

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(String enableFlag) {
            this.enableFlag = enableFlag;
        }
    }

    public static class ProductBean {
        private String id;
        private String productId;
        private int noticeIndex; //推荐排序
        private String orderNumber;  //列表排序
        private String name;
        private String loanRange; // 金额区间
        private String logoUrl;
        private String link;
        private String des;
        private String loanTerm; //天数区间
        private String productTypes; //产品类型
        private String number;
        private String marketPhrases; // 营销短语
        private String tagDesc;

        private List<labelBean> labels;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNoticeIndex() {
            return noticeIndex;
        }

        public void setNoticeIndex(int noticeIndex) {
            this.noticeIndex = noticeIndex;
        }

        public String getLoanRange() {
            return loanRange;
        }

        public void setLoanRange(String loanRange) {
            this.loanRange = loanRange;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public List<labelBean> getLabels() {
            return labels;
        }

        public void setLabels(List<labelBean> labels) {
            this.labels = labels;
        }

        public String getLoanTerm() {
            return loanTerm;
        }

        public void setLoanTerm(String loanTerm) {
            this.loanTerm = loanTerm;
        }

        public String getProductTypes() {
            return productTypes;
        }

        public void setProductTypes(String productTypes) {
            this.productTypes = productTypes;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMarketPhrases() {
            return marketPhrases;
        }

        public void setMarketPhrases(String marketPhrases) {
            this.marketPhrases = marketPhrases;
        }

        public String getTagDesc() {
            return tagDesc;
        }

        public void setTagDesc(String tagDesc) {
            this.tagDesc = tagDesc;
        }
    }

    /**
     * 标签
     */
    public class labelBean {
        private String id;
        private String orderNumber;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<BannerBean> getAdList() {
        return adList;
    }

    public void setAdList(List<BannerBean> adList) {
        this.adList = adList;
    }

    public List<NoticeBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeBean> noticeList) {
        this.noticeList = noticeList;
    }

    public List<TypeBean> getTypePOs() {
        return typePOs;
    }

    public void setTypePOs(List<TypeBean> typePOs) {
        this.typePOs = typePOs;
    }

    public List<ProductBean> getProRecommendVOs() {
        return proRecommendVOs;
    }

    public void setProRecommendVOs(List<ProductBean> proRecommendVOs) {
        this.proRecommendVOs = proRecommendVOs;
    }

    public List<ProductBean> getProductVOs() {
        return productVOs;
    }

    public void setProductVOs(List<ProductBean> productVOs) {
        this.productVOs = productVOs;
    }

}
