package com.zfhy.egold.domain.goods.dto;


public enum SpuType {
    WITHDRAW_GOLD(1, "提金产品"),
    MALL_GOLD(2, "商城产品"),;

    private int code;
    private String label;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    SpuType(int code, String label) {

        this.code = code;
        this.label = label;
    }

    public static String getNameByType(Integer goodsTypeId) {

        for(SpuType spuType : SpuType.values()){

            if (goodsTypeId == spuType.getCode()) {
                return spuType.getLabel();
            }

        }
        return "";


    }
}
