package com.zfhy.egold.gateway.sms;

import com.google.gson.Gson;
//import com.taobao.api.ApiException;
//import com.taobao.api.DefaultTaobaoClient;
//import com.taobao.api.TaobaoClient;
//import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
//import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.exception.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by LAI on 2017/7/19.
 */
@Component
public class SMSApi {
    private static final Logger logger = LoggerFactory.getLogger(SMSApi.class);

    @Value("${TAOBAO_API_URL}")
    private String taobaoApiUrl;

    @Value("${TAOBAO_API_APPKEY}")
    private String appkey;

    @Value("${TAOBAO_API_SECRET}")
    private String secret;


    public  void sendSms(String mobile, Map<String, String> content, SmsTplType tplType) {
        // 短信模板的内容
        //String json = "{\"code\":\"" + checkCode + "\",\"product\":\"E黄金\"}";
       /* TaobaoClient client = new DefaultTaobaoClient(taobaoApiUrl, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("E黄金");
        req.setSmsParamString(new Gson().toJson(content));
        req.setRecNum(mobile);
        req.setSmsTemplateCode(tplType.getSmsTplCode());

        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            throw new BusException("发送短信异常，", e);
        }
        logger.info("短信信息》》》》" + rsp.getBody());*/
    }
}
