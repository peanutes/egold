package com.zfhy.egold.common.util;

import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class HttpClientUtil {


    public enum ContentTypeEnum {
        JSON("application/json"), XML("application/xml");
        private String code;

        ContentTypeEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(5000).build();

    
    public static String doPost(Map<String, String> params, String url) {
        List<NameValuePair> parameterList = new ArrayList<>();
        if (MapUtils.isNotEmpty(params)) {
            parameterList = params.entrySet().stream()
                    .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        }



        log.info("请求post URL:{}", url);
        log.info("请求参数：{}", new Gson().toJson(parameterList));
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        if (CollectionUtils.isNotEmpty(parameterList)) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(parameterList, "UTF-8"));
            } catch (Exception e) {
                log.error("封装请求参数失败，", e);
            }
        }
        
        httpPost.setConfig(RequestConfig.copy(requestConfig).build());
        CloseableHttpResponse httpResp = null;
        long begin = System.currentTimeMillis();
        try {
            httpResp = httpclient.execute(httpPost);
        } catch (Exception e) {
            log.error("执行post请求失败，", e);
        } finally {
            log.info(">>>>执行时间》》》》》:{}", System.currentTimeMillis() - begin);

        }
        return handleResponse(httpResp, httpclient);
    }

    
    public static String doGet(String url) {
        log.info("请求URL:{}", url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        
        httpGet.setConfig(RequestConfig.copy(requestConfig).build());
        CloseableHttpResponse httpResp = null;
        try {
            httpResp = httpclient.execute(httpGet);
        } catch (Exception e) {
            log.error("执行get请求失败，", e);
        }
        return handleResponse(httpResp, httpclient);
    }

    private static String handleResponse(CloseableHttpResponse httpResp, CloseableHttpClient httpclient)  {
        try {
            if (Objects.isNull(httpResp) || Objects.isNull(httpResp.getStatusLine())) {
                throw new BusException("您好，请求超时");
            }

            int statusCode = httpResp.getStatusLine().getStatusCode();
            
            if (statusCode == HttpStatus.SC_OK) {
                log.info("请求成功!");
                
                try {
                    String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
                    log.info("result>>>{}", result);
                    return result;
                } catch (Exception e) {
                    log.error("处理返回结果失败，", e);
                }
            } else {
                log.error("请求失败!状态码：{}，", httpResp.getStatusLine().getStatusCode());
            }
        } finally {
            try {
                if (httpResp != null) {
                    httpResp.close();

                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (Exception e) {
                log.error("关闭连接失败，，", e);
            }
        }
        return "";
    }


    
    public static String doPostBody(String url, String body, ContentTypeEnum contentType){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(body, ContentType.create(contentType.getCode(), "UTF-8")));
        try {
            CloseableHttpResponse resp = httpclient.execute(httpPost);
            return handleResponse(resp, httpclient);
        } catch (Exception e) {
            log.error("执行postStream请求失败，", e);
        }

        return "";
    }



    public static void main(String[] args) {
        String s = doGet("http://ad.toutiao.com/track/activate/?callback=CPGNr6DnARDyja-g5wEY4I2W6-UBIInRxv6IASiT5KCg5wEwDDgBQiAyMDE3MDYxNjE1NTMzNzAxMDAwNDA1MTEzMDQ2MkZBQQ==&os=1&muid=53F84932-7629-4FD8-92EA-6A94B261E6A3");
        System.out.println(s);

        Map map = new HashMap();
        map.put("code","hgOUT");
        String str = doPost(map,"https://test.watchbank.cn/getBorrowDetail.do");
        System.out.println(str);
    }
}
