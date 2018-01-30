package com.zfhy.egold.domain.invest.dto;


public enum ArticleType {
    HOST_INFO(1, "热门资讯"),
    HELP_DESK(2, "帮助中心"),
    




    AGREEMENT_REGISTER(8, "注册协议"),
    AGREEMENT_CURRENT_GOLD(9, "活期金协议"),
    AGREEMENT_FIXED_GOLD(10, "定期金协议"),
    AGREEMENT_RISE_N_FALL_GOLD(11, "看涨跌协议"),
    
    SPECIAL_PRICE_GOLD_RULE(13, "特价金细则"),
    AGREEMENT_REAL_NAME(14, "实名认证协议"),
    AGREEMENT_NEW_USER(15, "新手金协议"),;



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

    ArticleType(int code, String label) {
        this.code = code;
        this.label = label;
    }
    }
