package com.zfhy.egold.api.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zfhy.egold.api.util.TokenUtil;
import com.zfhy.egold.common.constant.AppEnvConst;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.entity.Dict;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zfhy.egold.domain.sys.dto.DictType.SIGN_EXPIRE_DURATION;
import static com.zfhy.egold.domain.sys.dto.DictType.SIGN_SECRET;


@Configuration
@Slf4j
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {



    @Value("${app.env}")
    private String env;


    @Resource
    private DictService dictService;

    @Resource
    private RedisService redisService;

    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }





    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        if (Objects.equals(env, AppEnvConst.dev.name()) || Objects.equals(env, AppEnvConst.test.name())) {

            
            return;
        }
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                
                if (!validateTimestamp(request)) {
                    log.warn("签名时间过期，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), RequestUtil.getIpAddress(request), JSON.toJSONString(request.getParameterMap()));

                    throw new BusException(ResultCode.TIME_EXPIRED, "签名时间过期,请重新按当前时间签名");
                }
                
                if (!validateSign(request)) {
                    log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                            request.getRequestURI(), RequestUtil.getIpAddress(request), JSON.toJSONString(request.getParameterMap()));

                    throw new BusException(ResultCode.SIGN_ERROR, "签名认证失败");
                }

                
                validateToken(request);


                return true;
            }
        });
    }

    private boolean validateToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            
            return true;
        }

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(token);

        TokenUtil.MEMBER_SESSION_THREAD_LOCAL.set(memberSession);
        
        return true;
    }

    private boolean validateTimestamp(HttpServletRequest request) {
        String callTimestamp = request.getParameter("callTimestamp");
        if (StringUtils.isEmpty(callTimestamp)) {
            
            return true;
        }

        long duration = Math.abs(System.currentTimeMillis() - Long.parseLong(callTimestamp));


        Dict dict = dictService.findByType(SIGN_EXPIRE_DURATION);
        if (Objects.isNull(dict)) {
            throw new BusException("查找不到过期时间，请联系客服");
        }

        return duration < Integer.parseInt(dict.getValue()) * 60 * 1000;
    }


    
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");
        if (StringUtils.isEmpty(requestSign)) {
            
            return true;
        }



        List<String> keys =  Lists.newArrayList(request.getParameterMap().keySet());
        keys.remove("sign");
        Collections.sort(keys);

        String linkString = keys.stream()
                .filter(Objects::nonNull)
                .map(key -> String.join("=", key, request.getParameter(key)))
                .collect(Collectors.joining("&"));


        Dict dict = dictService.findByType(SIGN_SECRET);

        if (Objects.isNull(dict)) {
            throw new BusException("查找不到密钥，请联系客服");
        }
        
        String secret = dict.getValue();
        
        String sign = HashUtil.sha1(linkString + secret);

        return StringUtils.equals(sign, requestSign);
    }


    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
    }


}
