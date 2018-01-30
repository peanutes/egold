package com.zfhy.egold.common.config.cache;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class SysRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    
    public SysRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



    @Override
    public void afterPropertiesSet() {
        parseCacheDuration(applicationContext);
    }

    private void parseCacheDuration(ApplicationContext applicationContext) {
        final Map<String, Long> cacheExpires = new HashMap<>();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Service.class);


        beansWithAnnotation.entrySet().forEach(entry->addCacheExpires(AopUtils.getTargetClass(entry.getValue()), cacheExpires));

        
        super.setExpires(cacheExpires);
    }


    private void addCacheExpires(final Class clazz, final Map<String, Long> cacheExpires) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            CacheDuration cacheDuration = findCacheDuration(clazz, method);
            if (Objects.nonNull(cacheDuration)) {
                Cacheable cacheable = findAnnotation(method, Cacheable.class);
                CacheConfig cacheConfig = findAnnotation(clazz, CacheConfig.class);
                Set<String> cacheNames1 = findCacheNames(cacheConfig, cacheable);
                for (String cacheName : cacheNames1) {
                    cacheExpires.put(cacheName, cacheDuration.duration());
                }

            }
        }, method -> null != findAnnotation(method, Cacheable.class));
    }




    
    private CacheDuration findCacheDuration(Class clazz, Method method) {
        CacheDuration methodCacheDuration = findAnnotation(method, CacheDuration.class);
        if (null != methodCacheDuration) {
            return methodCacheDuration;
        }
        CacheDuration classCacheDuration = findAnnotation(clazz, CacheDuration.class);
        if (null != classCacheDuration) {
            return classCacheDuration;
        }

        return null;
        
    }
    private Set<String> findCacheNames(CacheConfig cacheConfig, Cacheable cacheable) {
        if (Objects.isNull(cacheable.value()) || cacheable.value().length == 0) {
            return Arrays.stream(cacheConfig.cacheNames()).collect(Collectors.toSet());
        } else {
            return Arrays.stream(cacheable.value()).collect(Collectors.toSet());
        }

    }


    private <T> T findAnnotation(Method method, Class<? extends Annotation> clazz) {
        if (method.isAnnotationPresent(clazz)) {
            return (T) method.getAnnotation(clazz);
        }
        return null;
    }

    private <T> T findAnnotation(Class cls, Class<? extends Annotation> clazz) {
        if (cls.isAnnotationPresent(clazz)) {
            return (T) cls.getAnnotation(clazz);
        }
        return null;
    }




}
