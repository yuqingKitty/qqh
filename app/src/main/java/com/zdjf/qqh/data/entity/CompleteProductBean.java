package com.zdjf.qqh.data.entity;

import java.util.List;

/**
 * 贷款大全bean
 */
public class CompleteProductBean extends BaseBean {
    private List<ProductBean> productList;

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

    public class ProductBean {
        private String id;
        private String logoURL;
        private String name;
        private String marketPhrases;
        private String link;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogoURL() {
            return logoURL;
        }

        public void setLogoURL(String logoURL) {
            this.logoURL = logoURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMarketPhrases() {
            return marketPhrases;
        }

        public void setMarketPhrases(String marketPhrases) {
            this.marketPhrases = marketPhrases;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

}
