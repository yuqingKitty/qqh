package com.leuters.qqh.data.entity;

import java.util.List;

/**
 * 贷款大全bean
 */
public class CompleteBean extends BaseBean {
    private List<ProductSortLabel> prodSortLabelList;
    private List<ProductBean> productList;

    public class ProductSortLabel{
        public String id;
        public String name;
        public int sortRule;
    }

    public class ProductBean {
        public String id;
        public String logoUrl;
        public String name;
        public String link;
        public String prodLoanRange;
        public String prodLoanTerm;
        public String prodLoanInterest;
        public String prodLoanTime;
        public String prodLoanFeature;
    }

    public List<ProductSortLabel> getProdSortLabelList() {
        return prodSortLabelList;
    }

    public void setProdSortLabelList(List<ProductSortLabel> prodSortLabelList) {
        this.prodSortLabelList = prodSortLabelList;
    }

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

}
