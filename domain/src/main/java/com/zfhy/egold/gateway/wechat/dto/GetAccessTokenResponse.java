package com.zfhy.egold.gateway.wechat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LAI on 2017/11/7.
 */
@Data
public class GetAccessTokenResponse {

    @ApiModelProperty(value = "接口调用凭证")
    private String access_token;
    @ApiModelProperty(value = "access_token接口调用凭证超时时间，单位（秒）")
    private Integer expires_in;
    @ApiModelProperty(value = "用户刷新access_token")
    private String refresh_token;
    @ApiModelProperty(value = "授权用户唯一标识")
    private String openid;
    @ApiModelProperty(value = "用户授权的作用域，使用逗号（,）分隔")
    private String scope;
    @ApiModelProperty(value = "当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段")
    private String unionid;
    @ApiModelProperty(value = "错误码", hidden = true)
    private String errcode;
    @ApiModelProperty(value = "错误信息", hidden = true)
    private String errmsg;

}
