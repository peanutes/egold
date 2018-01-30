package com.zfhy.egold.common.config.cache;

import com.zfhy.egold.common.util.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


@Configuration
@EnableCaching
@Slf4j

public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String parameters = Arrays.asList(params).stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining("_"));
            Class<?> clazz = target.getClass();
            String keyStr = String.join("_", clazz.getName(), method.getName(), parameters);
            return String.format("egold:%s", HashUtil.getMd5(keyStr));
        };
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        SysRedisCacheManager sysRedisCacheManager = new SysRedisCacheManager(redisTemplate);
        sysRedisCacheManager.setDefaultExpiration(60);
        return sysRedisCacheManager;
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }

    @Primary
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory, RedisSerializer fastJson2JsonRedisSerializer) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setValueSerializer(fastJson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    








}
