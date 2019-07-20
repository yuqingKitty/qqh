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
