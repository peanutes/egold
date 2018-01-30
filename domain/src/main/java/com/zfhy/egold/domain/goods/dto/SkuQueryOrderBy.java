package com.zfhy.egold.domain.goods.dto;


public enum SkuQueryOrderBy {
    PRICE_ASC("k.price asc"),
    PRICE_DESC("k.price desc"),
    NEW_GOOD("sort asc");


    private String orderBy;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    SkuQueryOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
