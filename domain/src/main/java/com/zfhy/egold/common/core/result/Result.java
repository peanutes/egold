package com.zfhy.egold.common.core.result;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;


public class Result<T> {
    @ApiModelProperty(value = "错误编码（200=成功，400=失败，401=签名错误, 402=签名时间过期, 404=接口不存在，500=服务器内部错误，" +
            "550=业务异常, 551=参数校验错误，55002=没有实名认证，55003=没有绑定银行卡，55004=没有设置交易密码，55005：登陆过期，需要重新登陆）",
            required = false)
    private int code;
    
    private String msg;
    
    private T data;

    public Result<T> setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
