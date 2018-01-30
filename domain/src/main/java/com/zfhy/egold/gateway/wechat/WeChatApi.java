package com.zfhy.egold.gateway.wechat;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HttpClientUtil;
import com.zfhy.egold.gateway.wechat.dto.GetAccessTokenResponse;
import com.zfhy.egold.gateway.wechat.dto.WechatUserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by LAI on 2017/11/7.
 */
@Component
public class WeChatApi {

    @Value("${wechat_appid}")
    private String appId;
    @Value("${wechat_secret}")
    private String secret;
    @Value("${wechat_getaccesstoken_url}")
    private String getAccessTokenUrl;

    @Value("${wechat_userinfo_url}")
    private String getUserInfoUrl;

    private static String GRANT_TYPE = "authorization_code";



    public GetAccessTokenResponse getAccessToken(String code) {
        Map<String, String> params = ImmutableMap.<String, String>builder()
                .put("appid", appId)
                .put("secret", secret)
                .put("code", code)
                .put("grant_type", GRANT_TYPE)
                .build();

        String result = HttpClientUtil.doPost(params, getAccessTokenUrl);
        GetAccessTokenResponse response = new Gson().fromJson(result, GetAccessTokenResponse.class);
        if (StringUtils.isNotBlank(response.getErrcode())) {
            throw new BusException(String.format("您好，获取access_token失败，错误码为：%s，错误信息为：%s", response.getErrcode(), response.getErrmsg()));
        }


        return response;

    }

    public WechatUserInfo getUserInfo(String accessToken, String openId) {
        Map<String, String> params = ImmutableMap.<String, String>builder()
                .put("access_token", accessToken)
                .put("openid", openId)
                .build();

        String result = HttpClientUtil.doPost(params, getUserInfoUrl);
        WechatUserInfo response = new Gson().fromJson(result, WechatUserInfo.class);
        if (StringUtils.isNotBlank(response.getErrcode())) {
            throw new BusException(String.format("您好，获取access_token失败，错误码为：%s，错误信息为：%s", response.getErrcode(), response.getErrmsg()));
        }

        return response;
    }

}
