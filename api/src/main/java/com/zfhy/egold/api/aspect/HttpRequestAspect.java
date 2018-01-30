package com.zfhy.egold.api.aspect;

import com.google.gson.Gson;
import com.zfhy.egold.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Aspect
@Component
@Order(-5)
@Slf4j
public class HttpRequestAspect {

    
    public static ThreadLocal<String> IP_THREAD_LOCAL = new ThreadLocal<>();



    
    @Pointcut("execution(public * com.zfhy.egold.api.web..*Controller.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){



        
        log.info("WebLogAspect.doBefore()");
        HttpServletRequest request = RequestUtil.getHttpServletRequest();

        String ip = RequestUtil.getIpAddress(request);
        IP_THREAD_LOCAL.set(ip);



        
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + ip);
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        log.info("请求参数 : {}", new Gson().toJson(request.getParameterMap()));


    }



    @AfterReturning("webLog()")
    public void  doAfterReturning(JoinPoint joinPoint){
        
        log.info("WebLogAspect.doAfterReturning()");
    }

    @AfterThrowing("webLog()")
    public void doAfterThrowing(JoinPoint joinPoint) {

    }



}
