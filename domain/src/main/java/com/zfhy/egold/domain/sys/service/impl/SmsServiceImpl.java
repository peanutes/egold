package com.zfhy.egold.domain.sys.service.impl;

import com.google.common.collect.ImmutableMap;
import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dao.SmsMapper;
import com.zfhy.egold.domain.sys.entity.Sms;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.gateway.sms.SMSApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.SmsService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;



@Service
@Transactional
@Slf4j
public class SmsServiceImpl extends AbstractService<Sms> implements SmsService {
    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private SMSApi smsApi;

    @Autowired
    private RedisService redisService;


    @Override
    public String send(String mobile, SmsTplType smsTplType, String terminalType, String ip) {
        String key = String.format(RedisConst.SMS_KEY_FORMAT, String.join("", mobile, smsTplType.getSmsTplCode()));


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMddHHmm");
        String minute = formatter.format(now);

        String hour = DateTimeFormatter.ofPattern("YYYYMMddHH").format(now);

        
        Integer minuCount = redisService.get(String.join("_", key, minute));

        

        
        Integer hourCount = this.redisService.get(String.join("_", key, hour));



        if (minuCount != null && minuCount > 1) {
            throw new BusException("发送短信过于频繁，请稍后再试");
        }


        if (hourCount != null && hourCount > 10) {
            throw new BusException("您发的有点频繁，请过一会再试");
        }


        String code = StrFunUtil.genRandom(4);

        Map<String, String> content = ImmutableMap.<String, String>builder().put("code", code).put("product", "E黄金").build();
        smsApi.sendSms(mobile, content, smsTplType);


        Sms sms = new Sms();
        sms.setCode(code);
        sms.setCreateDate(new Date());
        sms.setIp(ip);
        sms.setMobile(mobile);
        sms.setTplType(smsTplType.getSmsTplCode());
        sms.setSendTime(new Date());

        this.smsMapper.insert(sms);

        return code;
    }
}
