package com.zfhy.egold.domain.redis.dao;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
@Slf4j
public class RedisRepository {


    @Autowired
    private RedisTemplate redisTemplate;

    private static String redisCode = "utf-8";
    
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("缓存写入失败:", e);
        }
        return result;
    }
    
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("缓存写入失败:", e);
        }
        return result;
    }
    
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }


    
    public void getPattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
    
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    
    public <T> T get(final String key) {
        T result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = (T) operations.get(key);
        return result;
    }
    
    public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    
    public <T> T hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return (T) hash.get(key,hashKey);
    }

    
    public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    
    public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    
    public void add(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    
    public void reduce(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key,value);
    }

    
    public Set getSet(String key){
        Set<Object> s;
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        s = set.members(key);
        return s;
    }

    
    public boolean isMember(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.isMember(key, value);
    }



    
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    public long generateByKey(String key){
        log.info("---------------------------generate "+key);
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.getAndAdd(1);
        counter.expire(1, TimeUnit.DAYS);
        long x1=counter.longValue();
        log.info("---------------------------generate "+key+"----------value="+x1);
        return  x1;
    }


    public <T> List<T> getByPattern(String pattern) {

        Set<String> keys = this.redisTemplate.keys(pattern);
        log.info(">>>>>{}", new Gson().toJson(keys));


        
        List<T> list = Lists.newArrayList();
        for (String key : keys) {
            T t = this.get(key);
            list.add(t);
        }

        return mget(keys);

    }

    public <T> List<T> mget(final Set<String> keys) {

        return (List<T>) this.redisTemplate.execute((RedisCallback) connection -> {

            List<T> rs = Lists.newArrayList();
            byte[][] ret = new byte[keys.size()][];

            int i = 0;
            for (String key : keys) {
                ret[i] = key.getBytes();
                i++;
            }


            List<byte[]> results = connection.mGet(ret);

            for (byte[] result : results) {
                T deserialize = (T) redisTemplate.getValueSerializer().deserialize(result);

                rs.add(deserialize);
            }


            return rs;
        });
    }

    public <T> T getAndSet(String key, Object val) {
        T result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = (T) operations.getAndSet(key, val);

        return result;

    }

    public Boolean setNx(String key, Object val, Date expiredAt) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        Boolean flag = operations.setIfAbsent(key, val);
        if (flag) {
            redisTemplate.expireAt(key, expiredAt);
        }
        return flag;
    }

    public boolean setNx(String key, Object val) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, val);
    }

    public Long incr(String key, Long value, Long expireTime) {
        Long result = 0L;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            result = operations.increment(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存写入失败:", e);
        }


        return result;
    }
}
