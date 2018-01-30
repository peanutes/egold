package com.zfhy.egold.domain.redis.service.impl;

import com.zfhy.egold.common.constant.IdTypeConstant;
import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.dto.RealtimePriceDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.redis.dao.RedisRepository;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    public enum AppTerminal {
        mobile,
        web,
    }



    
    public static final long TOKEN_EXPIRE = 7 * 24 * 60 * 60;

    @Value("${app.terminal}")
    private String appTerminal;

    @Value("${app.env}")
    private String appEnv;


    @Autowired
    private RedisRepository redisRepository;

    @Override
    public String createAndSetMemberToken(Member member) {

        MemberSession memberSession = new MemberSession().convertFrom(member);
        String token;
        if (Objects.equals(appTerminal, AppTerminal.web.name())) {
            
            HttpSession session = RequestUtil.getHttpServletSession();
            session.setAttribute("auth_member_session", memberSession);
            token = session.getId();

        } else {
            
            token = UUID.randomUUID().toString().replaceAll("-", "");
            this.redisRepository.set(String.format(RedisConst.TOKEN_KEY_FORMAT, token), memberSession, TOKEN_EXPIRE);

        }

        return token;
    }


    @Override
    public MemberSession checkAndGetMemberToken(String token) {

        MemberSession memberSession;
        if (Objects.equals(appTerminal, AppTerminal.web.name())) {
            
            HttpSession session = RequestUtil.getHttpServletSession();
            memberSession = (MemberSession) session.getAttribute("auth_member_session");

        } else {
            
            memberSession = redisRepository.get(String.format(RedisConst.TOKEN_KEY_FORMAT, token));
        }

        if (Objects.isNull(memberSession)) {
            throw new BusException(ResultCode.BUS_TOKEN_EXPIRED, "登录过期，请重新登录");
        }
        return memberSession;

    }

    

    @Override
    public MemberSession checkAndGetMemberToken4Mall(String token) {

        
        MemberSession
                memberSession = redisRepository.get(String.format(RedisConst.TOKEN_KEY_FORMAT, token));




        return memberSession;

    }

    @Override
    public void setSmsCode(String mobile, String smsCode, SmsTplType smsTplType) {
        String key = String.format(RedisConst.SMS_KEY_FORMAT, String.join("", mobile, smsTplType.getSmsTplCode()));
        redisRepository.set(key, smsCode, 300L);
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMddHHmm");
        String minute = formatter.format(now);

        String hour = DateTimeFormatter.ofPattern("YYYYMMddHH").format(now);

        redisRepository.incr(String.join("_", key, minute), 1L, 65L);
        redisRepository.incr(String.join("_", key, hour), 1L, 3600L);
    }

    @Override
    public String getSmsCode(String mobile, SmsTplType smsTplType) {

        return this.redisRepository.get(String.format(RedisConst.SMS_KEY_FORMAT, String.join("", mobile, smsTplType.getSmsTplCode())));
    }

    
    @Override
    public Double getRealTimePrice() {
        Object o = this.redisRepository.get(RedisConst.GOLD_REAL_PRICE);
        if (Objects.isNull(o)) {
            return 0D;
        }
        return (Double) o;
    }

    

    @Override
    public List<PriceDto> hourGoldPrices() {

        List<PriceDto> priceDtos = this.redisRepository.getByPattern(RedisConst.GOLD_HOUR_PRICE_LIST);


        return priceDtos;

    }

    @Override
    public Double getAndSetRealTimePrice(Double latestPrice) {
        return this.redisRepository.getAndSet(RedisConst.GOLD_REAL_PRICE, latestPrice);
    }

    @Override
    public boolean setIfAbsentWeekPrice(String key, Double latestPrice, Date expiredAt) {
        return this.redisRepository.setNx(key, latestPrice, expiredAt);
    }

    @Override
    public boolean setIfAbsentMonthPrice(String key, Double latestPrice, Date expiredAt) {

        return this.redisRepository.setNx(key, latestPrice, expiredAt);
    }

    @Override
    public boolean setIfAbsentDailyPrice(String key, Double latestPrice) {
        return this.redisRepository.setNx(key, latestPrice);

    }

    @Override
    public boolean setIfAbsentRealTimePrice(String key, PriceDto latestPrice, Date weekPriceExpiredAt) {

        return this.redisRepository.setNx(key, latestPrice, weekPriceExpiredAt);
    }

    @Override
    public void setRealTimePriceDto(RealtimePriceDto realTimePriceDto) {
        this.redisRepository.set(RedisConst.GOLD_REAL_PRICEDTO, realTimePriceDto);
    }

    @Override
    public RealtimePriceDto getRealTimePriceDto() {

        return this.redisRepository.get(RedisConst.GOLD_REAL_PRICEDTO);
    }

    @Override
    public <T> T get(String key) {
        return this.redisRepository.get(key);
    }

    @Override
    public boolean set(String key, Object value, Long expireTime) {
        return this.redisRepository.set(key, value, expireTime);
    }


    @Override
    public Long incr(String key, Long value, Long expireTime) {
        return this.redisRepository.incr(key, value, expireTime);
    }

    @Override
    public void logout(String token) {
        if (Objects.equals(appTerminal, AppTerminal.web.name())) {
            
            HttpSession session = RequestUtil.getHttpServletSession();
            session.removeAttribute("auth_member_session");
            session.removeAttribute("memberOutPutDto");

        } else {
            
            this.redisRepository.remove(String.format(RedisConst.TOKEN_KEY_FORMAT, token));

        }

    }

    
    @Override
    public String getIdByType(IdTypeConstant idTypeConstant) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String prefix = String.join("", idTypeConstant.getPrefix(), dateStr);
        Long number = this.incr(prefix, 1L, 86400L);

        return String.format("%s_%s%06d", appEnv, prefix, number);
    }

    @Override
    public MemberSession getMemberSession(String token, HttpServletRequest request) {
        MemberSession memberSession;
        if (Objects.equals(appTerminal, AppTerminal.web.name())) {
            
            HttpSession session = request.getSession();
            memberSession = (MemberSession) session.getAttribute("auth_member_session");

        } else {
            
            memberSession = redisRepository.get(String.format(RedisConst.TOKEN_KEY_FORMAT, token));
        }

        return memberSession;

    }


    public static void main(String[] args) {
        System.out.println(String.format(RedisConst.TOKEN_KEY_FORMAT, "23123"));

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMddHHmm");
        String minute = formatter.format(now);

        System.out.println(minute);
    }
}
