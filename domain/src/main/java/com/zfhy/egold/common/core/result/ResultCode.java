package com.zfhy.egold.common.core.result;


public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    SIGN_ERROR(401),
    TIME_EXPIRED(402),
    UNAUTHORIZATION(403),
    NOT_FOUND(404),

    INTERNAL_SERVER_ERROR(500),
    BUS_EXCEPTION(550),
    PARAM_VALIDATE(551),


    BUS_NO_LOGIN(55001), 
    BUS_NO_REAL_NAME(55002), 
    BUS_NO_BIND_CARD(55003), 
    BUS_NO_DEAL_PWD(55004),
    BUS_TOKEN_EXPIRED(55005); 


    public int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    ResultCode(int code) {
        this.code = code;
    }
}
