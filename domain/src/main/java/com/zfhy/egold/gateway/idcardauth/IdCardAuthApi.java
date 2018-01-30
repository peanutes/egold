/*
package com.zfhy.egold.gateway.idcardauth;

import cn.net.wnd.credit.platform.common.WndJson;
import cn.net.wnd.credit.platform.pkg.*;
import com.google.gson.Gson;
import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.common.util.HttpClientUtil;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

*/
/**
 * Created by LAI on 2017/10/1.
 *//*

@Component
@Slf4j
public class IdCardAuthApi {

    @Value("${zhongsheng_login_url}")
    private String zhongshengLoginUrl;
    @Value("${zhongsheng_account}")
    private String zhongshengAccount;
    @Value("${zhongsheng_password}")
    private String zhongshengPassword;


    @Autowired
    private RedisService redisService;



    */
/**
     * 中胜信用验证
     *
     * @param realName 姓名
     * @param idNo     证件号码
     * @param place     地点
     * @return
     * @throws Exception
     *//*

    public Map<String, String> verifyIdCard(String realName, String idNo, String place) {
        Map<String, String> retMap = new HashMap<>();
        int code;//验证返回码 100请求  1100验证成功
        String msg;//验证结果
        try {
            if (StringUtils.isBlank(place)) {
                place = "guangz";
            }
            SfzReqPkg sfzReqPkg = new SfzReqPkg();
            sfzReqPkg.setMid(1);
            sfzReqPkg.setAccount(zhongshengAccount);
            sfzReqPkg.setVersion("1.0");
            sfzReqPkg.setFsd(place);
            sfzReqPkg.setGmsfhm(idNo);
            sfzReqPkg.setXm(new String(realName.getBytes("utf-8"), "utf-8"));
            sfzReqPkg.setYwlx("open account");

            SfzReqPkgAnna anna = new SfzReqPkgAnna();
            anna.getReqMap().put(sfzReqPkg.getGmsfhm(), sfzReqPkg);

            IPkg pkgresp = sfzCheck(anna, zhongshengAccount, zhongshengPassword);
            SfzRespPkg resp = (SfzRespPkg) pkgresp;
            code = resp.getStatcode();
            Map<String, SfzRespPkg> respmap = WndJson.SfzRespMsg2Map(new String(Base64.decodeBase64(resp.getMsg()), "utf-8"));
            // 获取某个单个号码
            if (respmap.containsKey(sfzReqPkg.getGmsfhm())) {
                SfzRespPkg pk = respmap.get(sfzReqPkg.getGmsfhm());
                code = pk.getStatcode();
                log.info(pk.getGmsfhm() + " " + pk.getStatcode());
            }
        } catch (Exception e) {
            log.error("实名认证失败", e);
            throw new BusException("实名认证失败");

        }
        msg = getMsg(code);


        if (code != 1100) {
            throw new BusException(String.format("您好，实名认证失败，原因为：%s", msg));
        }

        retMap.put("code", code + "");
        retMap.put("msg", msg);
        return retMap;
    }


    public IPkg sfzCheck(IPkg pkg, String account, String password) throws IOException, URISyntaxException {

        SfzReqPkgAnna anna = (SfzReqPkgAnna) pkg;
        RespPkg respppp = getToken(account, password);


        log.info("token response>>>>>{},{}", respppp.getMsg(), respppp.getStatcode());
        if (respppp != null) {
            URIBuilder uri = new URIBuilder(respppp.getCallbackUrl());
            uri.setParameter("mid", "1");
            uri.setParameter("action", WndAction.qsfz.name());
            uri.setParameter("token", respppp.getMsg());
            String s = new Gson().toJson(anna.getReqMap());
            uri.setParameter("msg", org.apache.commons.codec.binary.Base64.encodeBase64String(s.getBytes("utf-8")));
            uri.setParameter("f", "2");


            String result2 = HttpClientUtil.doPost(Collections.emptyMap(), uri.build().toString());


            SfzRespPkgDto sfzRespPkgDto = new Gson().fromJson(result2, SfzRespPkgDto.class);

            SfzRespPkg sfzRespPkg = new SfzRespPkg();
            BeanUtils.copyProperties(sfzRespPkgDto, sfzRespPkg);

            return sfzRespPkg;

        } else {
            log.info("retry get tkn"); //$NON-NLS-1$
//            URIBuilder uriBuilder = new URIBuilder(WndConfigure.getInstance().getConfigItem("login_url"));
            URIBuilder uriBuilder = new URIBuilder(zhongshengLoginUrl);
            uriBuilder.setParameter("msg", account + HashUtil.getMd5(password).substring(8, 24));
//            uriBuilder.setParameter("msg", account + JMD5.md516(password));
            IPkg returnIPkg = sfzCheck(pkg, uriBuilder);
            log.info("sfzCheck() - end"); //$NON-NLS-1$
            return returnIPkg;
        }
    }

    public static IPkg sfzCheck(IPkg pkg, URIBuilder callURL) throws ClientProtocolException, IOException, URISyntaxException {
        // CloseableHttpClient hc = HttpClients.createDefault();
        log.info(callURL.build().toString());
        callURL.setParameter("token", "");
        callURL.setParameter("action", "login");
        CloseableHttpClient hcc = HttpClients.createDefault();
        try{
            CloseableHttpResponse resp = hcc.execute(new HttpPost(callURL.build()));
            HttpEntity ent = resp.getEntity();
            InputStream is = ent.getContent();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
            String result = bReader.readLine();

            log.info("String result=" + result); //$NON-NLS-1$

            RespPkgDto respPkgDto = new Gson().fromJson(result, RespPkgDto.class);

            RespPkg respppp = new RespPkg();
            BeanUtils.copyProperties(respPkgDto, respppp);

            log.debug(respppp.getState());
            if (respppp.getState().equals("success")) {
                if (pkg instanceof SfzReqPkgAnna) {
                    SfzReqPkgAnna anna = (SfzReqPkgAnna) pkg;
                    URIBuilder uri = new URIBuilder(respppp.getCallbackUrl());
                    uri.setParameter("mid", "1");
                    uri.setParameter("action", WndAction.qsfz.name());
                    uri.setParameter("token", respppp.getMsg());
                    uri.setParameter("msg", new Gson().toJson(anna.getReqMap()));
                    HttpPost post2 = new HttpPost(uri.build());

                    log.info("HttpPost post2=" + post2.toString()); //$NON-NLS-1$
                    CloseableHttpResponse resp2 = hcc.execute(post2);

                    HttpEntity ent2 = resp2.getEntity();
                    InputStream is2 = ent2.getContent();
                    BufferedReader bReader2 = new BufferedReader(new InputStreamReader(is2));
                    String result2 = bReader2.readLine();

                    log.debug(new String(result2.getBytes("iso-8859-1"), "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
                    //

                    SfzRespPkgDto sfzRespPkgDto = new Gson().fromJson(result2, SfzRespPkgDto.class);
                    SfzRespPkg sfzRespPkg = new SfzRespPkg();

                    BeanUtils.copyProperties(sfzRespPkgDto, sfzRespPkg);
                    log.info("SfzRespPkg sfzRespPkg=" + sfzRespPkg); //$NON-NLS-1$
                    return sfzRespPkg;
                } else {
                    log.warn("error :{}", new Gson().toJson(pkg));
                }

            } else {
                log.debug("sfzCheck() - end"); //$NON-NLS-1$
                return null;

            }
        }catch (Exception e) {
            log.error("实名认证异常：", e);
        }finally{
            hcc.close();
        }
        log.debug("sfzCheck() - end"); //$NON-NLS-1$
        return null;
    }

    public static String getMsg(int code){
        switch(code){
            case 100:
                return "请求应答成功";
            case 9100:
                return "余额不足";
            case 9110:
                return "无模块操作权限";
            case 9200:
                return "账户不存在";
            case 9201:
                return "密码不正确";
            case 9202:
                return "无权限查询(IP 限制)";
            case 9300:
                return "无效 token";
            case 9301:
                return "请求 token 太频繁";
            case 1000:
                return "请求格式错误  ";
            case 1100:
                return "身份证结果一致,验证成功";
            case 1101:
                return "身份证姓名和身份证号不一致";
            case 1102:
                return "身份证姓名和身份证号不一致";
            case 1103:
                return "库中无此号,请到户籍所在地进行核实";
            case 1190:
                return "身份证内部接口错误";
            case 1200:
                return "银行卡核查一致";
            case 1201:
                return "银行卡核查不一致";
            case 1202:
                return "银行卡核查状态未知";
            case 1203:
                return "其他错误";
            case 1205:
                return "身份证一致银行卡不一致";
            case 1290:
                return "银行卡内部接口错误";
            case 1902:
                return "无效请求 mapkey";
            case 1903:
                return "无效身份证号码";
            case 1904:
                return "姓名不正确";
            case 1905:
                return "无效相片";
            case 1906:
                return "业务类型不正确";
            case 1907:
                return "发生地为空或长度不正确";
            case 1911:
                return "无效的 cvv 取值";
            case 1912:
                return "无效过期时间";
            case 1913:
                return "无效手机号码";
            case 1920:
                return "银行账号不正确";
            default:
                return "发生其他错误";
        }
    }


    private  RespPkg getToken(String account, String password) throws URISyntaxException {
        log.info("getToken() - start " + account + "," + password); //$NON-NLS-1$
        //中胜信用登录token保存在memcache 不超过30分钟可以继续使用


        RespPkg cacheToken = this.redisService.get(RedisConst.ZHONG_SHENG_TOKEN);
        if (Objects.nonNull(cacheToken)) {
            return cacheToken;
        }



        URIBuilder uriBuilder = new URIBuilder(zhongshengLoginUrl);

        String msg = account + HashUtil.getMd5(password).substring(8, 24);
        uriBuilder.setParameter("msg", msg);

        log.info("msg -----> {}", msg);

//        uriBuilder.setParameter("msg", account + JMD5.md516(password));
        uriBuilder.setParameter("token", "");
        uriBuilder.setParameter("action", "login");
        BufferedReader bReader = null;
        CloseableHttpClient hcc = HttpClients.createDefault();
        try {
            for (int i = 0; i < 3; i++) {
                CloseableHttpResponse resp = hcc.execute(new HttpPost(uriBuilder.build()));
                HttpEntity ent = resp.getEntity();
                InputStream is = ent.getContent();
                bReader = new BufferedReader(new InputStreamReader(is));
                String result = bReader.readLine();

                log.error("String result=" + result); //$NON-NLS-1$


                RespPkgDto respppp = new Gson().fromJson(result, RespPkgDto.class);
                log.error("中胜信用登录获取respppp成功:{}", new Gson().toJson(respppp));
                if (respppp.getState().equals("success")) {
                    log.info("has new token " + account); //$NON-NLS-1$
                    RespPkg respPkg = new RespPkg();
                    BeanUtils.copyProperties(respppp, respPkg);
                    this.redisService.set(RedisConst.ZHONG_SHENG_TOKEN, respPkg, 1800L);
                    return respPkg;
                } else {
                    log.error("中胜信用登录获取respppp失败");
                }
            }
        } catch (IOException e) {
            log.error("getToken(String account=" + account + ", String password=" + password + ") - exception ignored", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            log.info(e.getLocalizedMessage());
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (hcc != null) {
                try {
                    hcc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    public int testBankPrivateAes(String realName, String idno, String cardno) {

        int code = -1;//验证返回码 100请求成功  1200验证成功
        try {
            BankServicePrivateReqPkgWrap reqPkgWrap = new BankServicePrivateReqPkgWrap();
            BankServicePrivateReqPkg reqPkg = new BankServicePrivateReqPkg();
            reqPkg.setMid(61);
            reqPkg.setAccount(zhongshengAccount);//请求账号
            reqPkg.setVersion("1.0");
            reqPkg.setIdCardName(realName.trim());//持卡人姓名
            reqPkg.setIdCardType("00");//证件类型 00身份证
            reqPkg.setIdCard(idno);//身份证号
            reqPkg.setBankCardType("1");//银行账户类型 0-存折	1-借记卡	2-贷记卡
            reqPkg.setBankCardNum(cardno);//银行账号
            reqPkgWrap.getReqMap().put(reqPkg.getBankCardNum(), reqPkg);

            IPkg pkgResp = yhCheck(reqPkgWrap, zhongshengAccount, zhongshengPassword);
            log.error("中胜信用银行卡认证返回结果：" + pkgResp);
            SfzRespPkg resp = (SfzRespPkg) pkgResp;
            code = resp.getStatcode();
            log.error("中胜信用银行卡认证返回code：" + code);

//            Map<String, BankServicePrivateRespPkg> respmap = WndJson.BankPrivateRespMsg2Map(new String(Base64.decode(resp.getMsg()), "utf-8"));
            Map<String, BankServicePrivateRespPkg> respmap = WndJson.BankPrivateRespMsg2Map(new String(Base64.decodeBase64(resp.getMsg()), "utf-8"));
            log.error("中胜信用银行卡认证返回msg" + respmap); //$NON-NLS-1$
            //Map<String, BankServicePrivateRespPkg> respmap = (Map<String, BankServicePrivateRespPkg>) JimJson.Str2Map(WndUtils.decodeData(resp.getMsg()), BankServicePrivateRespPkg.class);
            // 获取某个单个号码
            if (respmap.containsKey(reqPkg.getBankCardNum())) {
                BankServicePrivateRespPkg pk = respmap.get(reqPkg.getBankCardNum());
                code = pk.getStatcode(); //$NON-NLS-1$
            }

        } catch (Exception e) {
            log.error("银行卡验证失败！", e);

        }
        return code;
    }


    private  IPkg yhCheck(BankServicePrivateReqPkgWrap pkg, String account, String password) throws URISyntaxException, ClientProtocolException, IOException {
        log.info("sfzCheck() - start"); //$NON-NLS-1$

//		SfzReqPkgAnna anna = (SfzReqPkgAnna) pkg;
        RespPkg respppp = getToken(account, password);
        log.error("中胜信用获取respppp成功:"+respppp);
        log.info("respppp=" + respppp);
        if (respppp != null) {
            URIBuilder uri = new URIBuilder(respppp.getCallbackUrl());
            uri.setParameter("mid", "61");
            uri.setParameter("action", WndAction.bkdprivate.name());
            uri.setParameter("token", respppp.getMsg());
//			uri.setParameter("msg", "" + JSONObject.fromObject(pkg.getReqMap()));
//            uri.setParameter("msg", Base64.encode(JSONObject.fromObject(pkg.getReqMap()).toString().getBytes("utf-8")));
            uri.setParameter("msg",  new String(Base64.encodeBase64(JSONObject.fromObject(pkg.getReqMap()).toString().getBytes("utf-8")), "utf-8"));
            uri.setParameter("f", "2");
            log.info("Req2=" + uri.build().toString()); //$NON-NLS-1$

            String result2 = HttpClientUtil.doPost(Collections.emptyMap(), uri.build().toString());

//            HttpClient client = new HttpClient();
//            PostMethod method = new PostMethod(uri.build().toString());
//            client.executeMethod(method);
//            String result2 = method.getResponseBodyAsString();

            log.info(new String(result2.getBytes("iso-8859-1"), "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
            SfzRespPkg sfzRespPkg = (SfzRespPkg) JSONObject.toBean(JSONObject.fromObject(result2), SfzRespPkg.class);
            log.info("校验银行卡返回信息》》》》{}" , result2); //$NON-NLS-1$
            return sfzRespPkg;

        } else {
            return null;
        }
    }


}
*/
