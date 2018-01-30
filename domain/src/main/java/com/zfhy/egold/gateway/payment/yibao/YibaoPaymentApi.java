package com.zfhy.egold.gateway.payment.yibao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.common.util.HttpClientUtil;
import com.zfhy.egold.common.util.RandomUtil;
import com.zfhy.egold.gateway.payment.yibao.util.AES;
import com.zfhy.egold.gateway.payment.yibao.util.DigestUtil;
import com.zfhy.egold.gateway.payment.yibao.util.EncryUtil;
import com.zfhy.egold.gateway.payment.yibao.util.RSA;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by LAI on 2017/10/25.
 */
@Component
@Slf4j
public class YibaoPaymentApi {

    @Value("${merchantAccount}")
    private String merchantno;

    @Value("${merchantPrivateKey}")
    private String merchantPrivateKey;

    @Value("${yeepayPublicKey}")
    private String yeepayPublicKey;

    @Value("${bindPayRequestURL}")
    private String bindPayRequestURL;

    @Value("${bindCardRequestURL}")
    private String bindCardRequestURL;

    @Value("${bindCardConfirmURL}")
    private String bindCardConfirmURL;

    @Value("${bindPayConfirmURL}")
    private String bindPayConfirmURL;


    @Value("${bankCardCheckURL}")
    private String bankCardCheckURL;

    @Value("${pcMerchantAccount}")
    private String pcMerchantno;

    @Value("${pcMerchantPrivateKey}")
    private String pcMerchantPrivateKey;

    @Value("${pcPayRequestURL}")
    private String pcPayRequestURL;


    @Value("${pcPayPayResultCallback}")
    private String pcPayPayResultCallback;

    @Value("${app.env}")
    private String appEnv;




    /**
     * bindCardRequest() : 有短验绑卡请求接口
     */

    public  Map<String, String> bindCardRequest(Map<String, String> params) {

        log.info("##### bindCardRequest() #####");

        Map<String, String> result = new HashMap<>();
        String customError;    //自定义，非接口返回

        String merchantAESKey = RandomUtil.getRandom(16);


        String requestno            = StringUtils.trimToEmpty(params.get("requestno"));
        String identityid           = StringUtils.trimToEmpty(params.get("identityid"));
        String identitytype         = StringUtils.trimToEmpty(params.get("identitytype"));
        String cardno               = StringUtils.trimToEmpty(params.get("cardno"));
        String idcardno             = StringUtils.trimToEmpty(params.get("idcardno"));
        String idcardtype           = StringUtils.trimToEmpty(params.get("idcardtype"));
        String username             = StringUtils.trimToEmpty(params.get("username"));
        String phone                = StringUtils.trimToEmpty(params.get("phone"));
        String advicesmstype        = StringUtils.trimToEmpty(params.get("advicesmstype"));
        String customerenhancedtype = StringUtils.trimToEmpty(params.get("customerenhancedtype"));
        String avaliabletime        = StringUtils.trimToEmpty(params.get("avaliabletime"));
        String callbackurl          = StringUtils.trimToEmpty(params.get("callbackurl"));
        String requesttime     		= StringUtils.trimToEmpty(params.get("requesttime"));

        TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
        dataMap.put("merchantno", 		merchantno);
        dataMap.put("requestno", 		requestno);
        dataMap.put("identityid", 		identityid);
        dataMap.put("identitytype", 	identitytype);
        dataMap.put("cardno", 			cardno);
        dataMap.put("idcardno", 		idcardno);
        dataMap.put("idcardtype", 		idcardtype);
        dataMap.put("username", 		username);
        dataMap.put("phone", 			phone);
        dataMap.put("advicesmstype", 	advicesmstype);
        dataMap.put("customerenhancedtype", 	customerenhancedtype);
        dataMap.put("avaliabletime", 	avaliabletime);
        dataMap.put("callbackurl", 		callbackurl);
        dataMap.put("requesttime", 		requesttime);

        String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        System.out.println("bindBankcardURL : " + bindCardRequestURL);
        System.out.println("dataMap : " + dataMap);


        try {
            String jsonStr				= JSON.toJSONString(dataMap);
            String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);


            ImmutableMap<String, String> parameterMap = ImmutableMap.<String, String>builder()
                    .put("merchantno", merchantno)
                    .put("data", data)
                    .put("encryptkey", encryptkey)
                    .build();


            String responseBody = HttpClientUtil.doPost(parameterMap, bindCardRequestURL);

            result = parseHttpResponseBody(responseBody);

        } catch(Exception e) {
            customError	= "Caught Exception!" + e.toString();
            result.put("customError", customError);
            log.error("绑卡请求失败", e);
            throw new BusException("您好，绑卡失败");

        }

        if (!Objects.equals(result.get("status"), "TO_VALIDATE")) {
            log.error("绑卡请求失败,返回信息为：{}", new Gson().toJson(result));

            throw new BusException(String.format("您好，绑卡失败，原因：%s", result.get("errormsg")));
        }


        return result;
    }


    /**
     * bindCardConfirm() : 有短验绑卡请求确认接口
     */
    public  Map<String, String> bindCardConfirm(Map<String, String> params) {

        log.info("##### bindCardConfirm() #####");

        String merchantAESKey = RandomUtil.getRandom(16);

        String requestno            	= StringUtils.trimToEmpty(params.get("requestno"));
        String validatecode      		= StringUtils.trimToEmpty(params.get("validatecode"));

        TreeMap<String, Object> dataMap	= new TreeMap<>();
        dataMap.put("merchantno", 	merchantno);
        dataMap.put("requestno", 		requestno);
        dataMap.put("validatecode", 	validatecode);

        String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        System.out.println("confirmBindBankcardURL : " + bindCardConfirmURL);
        System.out.println("dataMap : " + dataMap);

        Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回


        try {
            String jsonStr				= JSON.toJSONString(dataMap);
            String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);


            ImmutableMap<String, String> parameterMap = ImmutableMap.<String, String>builder()
                    .put("merchantno", merchantno)
                    .put("data", data)
                    .put("encryptkey", encryptkey)
                    .build();

            String responseBody = HttpClientUtil.doPost(parameterMap, bindCardConfirmURL);



            result = parseHttpResponseBody(responseBody);

        } catch(Exception e) {
            customError	= "Caught Exception!" + e.toString();
            result.put("customError", customError);
            log.error("绑卡请求确认失败", e);
            throw new BusException("您好，绑卡确认失败");
        }

        if (!Objects.equals(result.get("status"), "BIND_SUCCESS")) {
            log.error("绑卡请求确认失败,返回信息为：{}", new Gson().toJson(result));

            throw new BusException(String.format("您好，绑卡请求确认失败，原因：%s", result.get("errormsg")));
        }

        return result;
    }





    /**
     * bindPayRequest() : 有短验充值请求接口
     *
     * @param params
     * @return
     */
    public  Map<String, String> bindPayRequest(Map<String, String> params) {

        log.info("##### bindPayRequest() #####");

        Map<String, String> result = new HashMap<>();
        String customError = "";    //自定义，非接口返回

        String merchantAESKey = RandomUtil.getRandom(16);

        String requestno 				= StringUtils.trimToEmpty(params.get("requestno"));
        String identityid 				= StringUtils.trimToEmpty(params.get("identityid"));
        String identitytype 			= StringUtils.trimToEmpty(params.get("identitytype"));
        String cardtop 					= StringUtils.trimToEmpty(params.get("cardtop"));
        String cardlast 				= StringUtils.trimToEmpty(params.get("cardlast"));
        String amount 					= StringUtils.trimToEmpty(params.get("amount"));
        String advicesmstype 			= StringUtils.trimToEmpty(params.get("advicesmstype"));
        String avaliabletime 			= StringUtils.trimToEmpty(params.get("avaliabletime"));
        String productname 				= StringUtils.trimToEmpty(params.get("productname"));
        String callbackurl 				= StringUtils.trimToEmpty(params.get("callbackurl"));
        String requesttime				= StringUtils.trimToEmpty(params.get("requesttime"));
        String terminalid 				= StringUtils.trimToEmpty(params.get("terminalid"));
        String registtime 				= StringUtils.trimToEmpty(params.get("registtime"));
        String registip 				= StringUtils.trimToEmpty(params.get("registip"));
        String lastloginip 				= StringUtils.trimToEmpty(params.get("lastloginip"));
        String lastlogintime			= StringUtils.trimToEmpty(params.get("lastlogintime"));
        String lastloginterminalid 		= StringUtils.trimToEmpty(params.get("lastloginterminalid"));
        String issetpaypwd 				= StringUtils.trimToEmpty(params.get("issetpaypwd"));
        String free1 					= StringUtils.trimToEmpty(params.get("free1"));
        String free2 					= StringUtils.trimToEmpty(params.get("free2"));
        String free3 					= StringUtils.trimToEmpty(params.get("free3"));

        TreeMap<String, Object> dataMap	= new TreeMap<>();
        dataMap.put("merchantno", 	merchantno);
        dataMap.put("requestno", 		requestno);
        dataMap.put("identityid", 		identityid);
        dataMap.put("identitytype", 		identitytype);
        dataMap.put("cardtop", 		cardtop);
        dataMap.put("cardlast", 		cardlast);
        dataMap.put("amount", 		amount);
        dataMap.put("advicesmstype", 	advicesmstype);
        dataMap.put("avaliabletime",	 	avaliabletime);
        dataMap.put("productname", 	productname);
        dataMap.put("callbackurl", 		callbackurl);
        dataMap.put("requesttime", 	requesttime);
        dataMap.put("terminalid", HashUtil.getMd5(terminalid).substring(8, 24));
        dataMap.put("registtime", 	registtime);
//        dataMap.put("registip", 	registip);
//        dataMap.put("lastloginip", 	lastloginip);
//        dataMap.put("lastlogintime", 	lastlogintime);
        dataMap.put("lastloginterminalid", 	HashUtil.getMd5(terminalid).substring(8, 24));
        dataMap.put("issetpaypwd", 	issetpaypwd);
        dataMap.put("free1", 	free1);
        dataMap.put("free2", 	free2);
        dataMap.put("free3", 	free3);

        String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);



        try {
            String jsonStr              = new Gson().toJson(dataMap);
            String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);



            ImmutableMap<String, String> parameters = ImmutableMap.<String, String>builder()
                    .put("merchantno", merchantno)
                    .put("data", data)
                    .put("encryptkey", encryptkey)
                    .build();

            String responseBody = HttpClientUtil.doPost(parameters, bindPayRequestURL);


            result						= parseHttpResponseBody( responseBody);

        } catch(Exception e) {
            customError	= "Caught Exception!" + e.toString();
            result.put("customError", customError);
            log.error("支付请求失败", e);
            throw new BusException("您好，支付请求失败");
        }

        if (!Objects.equals(result.get("status"), "TO_VALIDATE")) {
            log.error("支付请求失败,返回信息为：{}", new Gson().toJson(result));

            throw new BusException(String.format("您好，支付请求失败，原因：%s", result.get("errormsg")));
        }


        return (result);
    }


    /**
     * bindPayConfirm() : 有短验充值请求确认接口
     */

    public  Map<String, String> bindPayConfirm(Map<String, String> params) {

        log.info("##### bindPayConfirm() #####");

        String merchantAESKey			= RandomUtil.getRandom(16);

        String requestno            	= StringUtils.trimToEmpty(params.get("requestno"));
        String validatecode      		= StringUtils.trimToEmpty(params.get("validatecode"));

        TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
        dataMap.put("merchantno", 	merchantno);
        dataMap.put("requestno", 		requestno);
        dataMap.put("validatecode", 	validatecode);

        String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        System.out.println("bindPayConfirmURL : " + bindPayConfirmURL);
        System.out.println("dataMap : " + dataMap);

        Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回


        try {
            String jsonStr				= JSON.toJSONString(dataMap);
            String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);


            ImmutableMap<String, String> parameterMap = ImmutableMap.<String, String>builder()
                    .put("merchantno", merchantno)
                    .put("data", data)
                    .put("encryptkey", encryptkey)
                    .build();

            String responseBody = HttpClientUtil.doPost(parameterMap, bindPayConfirmURL);

            result = parseHttpResponseBody(responseBody);

        } catch(Exception e) {
            customError	= "Caught Exception!" + e.toString();
            result.put("customError", customError);
            log.error("支付确认请求失败", e);
            throw new BusException("您好，支付确认请求失败");
        }


        if (!Objects.equals(result.get("status"), "PROCESSING")) {
            log.error("支付请求失败,返回信息为：{}", new Gson().toJson(result));

            throw new BusException(String.format("您好，支付请求失败，原因：%s", result.get("errormsg")));
        }
        return (result);
    }


    /**
     * 解析http请求返回
     */
    private  Map<String, String> parseHttpResponseBody(String responseBody) throws Exception {


        Map<String, String> result	= new HashMap<>();
        String customError			= "";


        Map<String, String> jsonMap	= JSON.parseObject(responseBody,  new TypeReference<TreeMap<String, String>>() {});

        if(jsonMap.containsKey("errorcode")) {
            result	= jsonMap;
            return (result);
        }

        String dataFromYeepay		= StringUtils.trimToEmpty(jsonMap.get("data"));
        String encryptkeyFromYeepay	= StringUtils.trimToEmpty(jsonMap.get("encryptkey"));

        boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, yeepayPublicKey, merchantPrivateKey);
        if(!signMatch) {
            customError	= "Sign not match error";
            result.put("customError",	customError);
            return (result);
        }

        String yeepayAESKey		= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
        String decryptData		= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);

        result	= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

        return(result);
    }

    /**
     * 解析并验证支付回调信息
     *
     * @param data
     * @param encryptKey
     * @return
     */
    public Map<String, String> decryptCallbackData(String data, String encryptKey) {

        log.info("##### decryptCallbackData() #####");


        Map<String, String> callbackResult;
        String customError;

        try {
            boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptKey, yeepayPublicKey, merchantPrivateKey);

            if(!signMatch) {
                throw new BusException("支付回调接口参数签名不匹配");
            }

            String yeepayAESKey	= RSA.decrypt(encryptKey, merchantPrivateKey);
            String decryptData	= AES.decryptFromBase64(data, yeepayAESKey);
            callbackResult		= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

        } catch(Exception e) {

            log.error("解析支付回调参数失败", e);
            throw new BusException("解析支付回调参数失败");
        }


        return callbackResult;


    }



    /**
     * bankCardCheck() : 银行卡信息查询接口
     *
     */
    public Map<String, String> bankCardCheck(String cardno) {

        log.info("##### bankCardCheck() #####");

        String merchantAESKey		= RandomUtil.getRandom(16);

        TreeMap<String, Object> dataMap	= new TreeMap<>();
        dataMap.put("merchantno", 	merchantno);
        dataMap.put("cardno", 			cardno);

        String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);


        Map<String, String> result	= new HashMap<>();
        String customError;


        try {
            String jsonStr			= JSON.toJSONString(dataMap);
            String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

            System.out.println("data=" + data);
            System.out.println("encryptkey=" + encryptkey);

            Map<String, String> parameterMap = ImmutableMap.<String, String>builder()
                    .put("merchantno", merchantno)
                    .put("data", data)
                    .put("encryptkey", encryptkey)
                    .build();

            String responseBody = HttpClientUtil.doPost(parameterMap, bankCardCheckURL);
            result = parseHttpResponseBody(responseBody);

        } catch(Exception e) {
            customError		= "Caught an Exception. " + e.toString();
            result.put("customError", customError);
            log.error("查询银行卡信息失败", e);
            throw new BusException("您好，查询银行卡信息失败");
        }


        if (!Objects.equals(result.get("isvalid"), "VALID")) {
            log.error("无效银行卡,返回信息为：{}", new Gson().toJson(result));

            throw new BusException("您输入的银行卡是无效卡，请确认再输入");
        }

        if (StringUtils.isNotBlank(result.get("errormsg"))) {
            log.error("查询银行卡信息失败,返回信息为：{}", new Gson().toJson(result));

            throw new BusException(result.get("errormsg"));
        }


        return result;
    }

    /**
     * getPayURL() : 生成支付链接
     */
    public  String getPayURL(Map<String, String> params) {

        System.out.println("##### getPayURL() #####");

        String p0_Cmd			= "Buy";
        String p1_MerId         = this.pcMerchantno;
        String p2_Order         = StringUtils.trimToEmpty(params.get("p2_Order")); //订单号
        String p3_Amt           = StringUtils.trimToEmpty(params.get("p3_Amt"));   //支付/充值金额
        String p4_Cur           = "CNY";   // 交易币种
        String p5_Pid           = "E-GOLD recharge"; // 商品名称
        String p8_Url           = pcPayPayResultCallback;   // 回调地址(前台回调地址)，后台回调地址需要在易宝配置

        String pm_Period        = "1";    //订单有效期
        String pn_Unit         	= "hour";  // 订单有效期单位
        String pr_NeedResponse  = "1"; // 应答机制
        String pd_FrpId         =  StringUtils.trimToEmpty(params.get("pd_FrpId")); //支付通道编码


//        String p6_Pcat          = formatString(params.get("p6_Pcat"));  //商品种类
//        String p7_Pdesc         = formatString(params.get("p7_Pdesc")); //商品描述
//        String p9_SAF           = formatString(params.get("p9_SAF"));   //送货地址
//        String pa_MP            = formatString(params.get("pa_MP")); //商户扩展信息
//        String pt_UserName  	= formatString(params.get("pt_UserName"));
//        String pt_PostalCode  	= formatString(params.get("pt_PostalCode"));
//        String pt_Address  		= formatString(params.get("pt_Address"));
//        String pt_TeleNo  		= formatString(params.get("pt_TeleNo"));
//        String pt_Mobile  		= formatString(params.get("pt_Mobile"));
//        String pt_Email  		= formatString(params.get("pt_Email"));
//        String pt_LeaveMessage  = formatString(params.get("pt_LeaveMessage"));




        String keyValue			= this.pcMerchantPrivateKey;

        String[] strArr = new String[]{p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid,
                p8_Url,pd_FrpId, pm_Period, pn_Unit, pr_NeedResponse};


        System.out.println("params=" + params);
        for(int i = 0; i < strArr.length; i++) {
            System.out.print(strArr[i]);
        }
        System.out.println();

        String hmac				= DigestUtil.getHmac(strArr, keyValue);

        try {
            p0_Cmd			= URLEncoder.encode(p0_Cmd, 	"GBK");
            p1_MerId		= URLEncoder.encode(p1_MerId, 	"GBK");
            p2_Order		= URLEncoder.encode(p2_Order, 	"GBK");
            p3_Amt			= URLEncoder.encode(p3_Amt, 	"GBK");
            p4_Cur			= URLEncoder.encode(p4_Cur, 	"GBK");
            p5_Pid			= URLEncoder.encode(p5_Pid, 	"GBK");
            p8_Url 			= URLEncoder.encode(p8_Url, 	"GBK");
            pm_Period		= URLEncoder.encode(pm_Period, 	"GBK");
            pn_Unit			= URLEncoder.encode(pn_Unit, 	"GBK");
            pr_NeedResponse	= URLEncoder.encode(pr_NeedResponse,"GBK");
            pd_FrpId		= URLEncoder.encode(pd_FrpId, 	"GBK");



//            p6_Pcat			= URLEncoder.encode(p6_Pcat, 	"GBK");
//            p7_Pdesc		= URLEncoder.encode(p7_Pdesc, 	"GBK");
//            p9_SAF 			= URLEncoder.encode(p9_SAF, 	"GBK");
//            pa_MP 			= URLEncoder.encode(pa_MP, 		"GBK");
//            pt_UserName		= URLEncoder.encode(pt_UserName, 	"GBK");
//            pt_PostalCode	= URLEncoder.encode(pt_PostalCode, 	"GBK");
//            pt_Address		= URLEncoder.encode(pt_Address, "GBK");
//            pt_TeleNo		= URLEncoder.encode(pt_TeleNo, 	"GBK");
//            pt_Mobile		= URLEncoder.encode(pt_Mobile, 	"GBK");
//            pt_Email		= URLEncoder.encode(pt_Email, 	"GBK");
//            pt_LeaveMessage	= URLEncoder.encode(pt_LeaveMessage, 	"GBK");




            hmac			= URLEncoder.encode(hmac, 		"GBK");
        } catch(Exception e) {
            e.printStackTrace();
        }

        String requestURL	= this.pcPayRequestURL;
        StringBuffer payURL = new StringBuffer();

        payURL.append(requestURL).append("?");
        payURL.append("p0_Cmd=").append(p0_Cmd);
        payURL.append("&p1_MerId=").append(p1_MerId);
        payURL.append("&p2_Order=").append(p2_Order);
        payURL.append("&p3_Amt=").append(p3_Amt);
        payURL.append("&p4_Cur=").append(p4_Cur);
        payURL.append("&p5_Pid=").append(p5_Pid);
        payURL.append("&p8_Url=").append(p8_Url);
        payURL.append("&pd_FrpId=").append(pd_FrpId);
        payURL.append("&pm_Period=").append(pm_Period);
        payURL.append("&pn_Unit=").append(pn_Unit);
        payURL.append("&pr_NeedResponse=").append(pr_NeedResponse);



//        payURL.append("&p6_Pcat=").append(p6_Pcat);
//        payURL.append("&p7_Pdesc=").append(p7_Pdesc);
//        payURL.append("&p9_SAF=").append(p9_SAF);
//        payURL.append("&pa_MP=").append(pa_MP);
//        payURL.append("&pt_UserName=").append(pt_UserName);
//        payURL.append("&pt_PostalCode=").append(pt_PostalCode);
//        payURL.append("&pt_Address=").append(pt_Address);
//        payURL.append("&pt_TeleNo=").append(pt_TeleNo);
//        payURL.append("&pt_Mobile=").append(pt_Mobile);
//        payURL.append("&pt_Email=").append(pt_Email);
//        payURL.append("&pt_LeaveMessage=").append(pt_LeaveMessage);



        payURL.append("&hmac=").append(hmac);

        // System.out.println("payURL : " + payURL);

        return (payURL.toString());
    }


    /**
     * verifyCallbackHmac() : 验证回调参数是否有效
     */
    public boolean verifyCallbackHmac(String[] stringValue, String hmacFromYeepay) {

        log.info("##### verifyCallbackHmac() #####");

        String keyValue			= pcMerchantPrivateKey;

        StringBuffer sourceData	= new StringBuffer();
        for(int i = 0; i < stringValue.length; i++) {

//			stringValue[i] = URLDecoder.decode(stringValue[i],"GBK");
//			stringValue[i] = new String(stringValue[i].getBytes("8859_1"),"GB2312");
            sourceData.append(stringValue[i]);
            log.info("stringValue ～～～～: " + stringValue[i]);

        }
        log.info("sourceData ～～～～: " + sourceData);

        String localHmac		= DigestUtil.getHmac(stringValue, keyValue);

        StringBuffer hmacSource	= new StringBuffer();
        for(int i = 0; i < stringValue.length; i++) {
            hmacSource.append(stringValue[i]);
        }

        return (localHmac.equals(hmacFromYeepay) ? true : false);
    }

    /**
     * : 验证回调安全签名数据是否有效
     */
    public boolean verifyCallbackHmac_safe(String[] stringValue, String hmac_safeFromYeepay)  {


        String keyValue			= pcMerchantPrivateKey;

        StringBuffer sourceData	= new StringBuffer();
        for(int i = 0; i < stringValue.length; i++) {
            if(!"".equals(stringValue[i])){
                sourceData.append(stringValue[i]+"#");
            }

            log.info("stringValue ～～～～: " + stringValue[i]);
        }

        sourceData = sourceData.deleteCharAt(sourceData.length()-1);
        log.info("sourceData ～～～～: " + sourceData.toString());

        String localHmac_safe		= DigestUtil.hmacSign(sourceData.toString(), keyValue);
        log.info("localHmac_safe:"+localHmac_safe);
        StringBuffer hmacSource	= new StringBuffer();
        for(int i = 0; i < stringValue.length; i++) {
            hmacSource.append(stringValue[i]);
        }

        return (localHmac_safe.equals(hmac_safeFromYeepay) ? true : false);
    }
}
