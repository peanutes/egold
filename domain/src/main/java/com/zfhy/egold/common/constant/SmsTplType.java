package com.zfhy.egold.common.constant;


public enum SmsTplType {
    REGISTER("SMS_38465127"),
    UPDATE_PWD("SMS_38465125"),
    CHANGE_MOBILE("SMS_38465125");
    
    private String smsTplCode;

    public String getSmsTplCode() {
        return smsTplCode;
    }

    public void setSmsTplCode(String smsTplCode) {
        this.smsTplCode = smsTplCode;
    }

    SmsTplType(String smsTplCode) {

        this.smsTplCode = smsTplCode;
    }
}
