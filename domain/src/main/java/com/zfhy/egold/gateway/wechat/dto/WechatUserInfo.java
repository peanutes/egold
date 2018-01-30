package com.zfhy.egold.gateway.wechat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LAI on 2017/11/7.
 */
@Data
public class WechatUserInfo {
    @ApiModelProperty(value = "普通用户的标识，对当前开发者帐号唯一")
    private String openid;
    @ApiModelProperty(value = "普通用户昵称")
    private String nickname;
    @ApiModelProperty(value = "普通用户性别，1为男性，2为女性")
    private Integer sex;
    @ApiModelProperty(value = "普通用户个人资料填写的省份")
    private String province;
    @ApiModelProperty(value = "普通用户个人资料填写的城市")
    private String city;
    @ApiModelProperty(value = "国家，如中国为CN")
    private String country;
    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空")
    private String headimgurl;
    @ApiModelProperty(value = "用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。")
    private String unionid;

    @ApiModelProperty(value = "错误码", hidden = true)
    private String errcode;
    @ApiModelProperty(value = "错误信息", hidden = true)
    private String errmsg;

}
