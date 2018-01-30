package com.zfhy.egold.domain.redis.service;

import com.zfhy.egold.common.constant.IdTypeConstant;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.domain.gold.dto.PriceDto;
import com.zfhy.egold.domain.gold.dto.RealtimePriceDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


public interface RedisService {
    String createAndSetMemberToken(Member member);

    MemberSession checkAndGetMemberToken(String token);

    MemberSession checkAndGetMemberToken4Mall(String token);

    void setSmsCode(String mobile, String smsCode, SmsTplType smsTplType);

    String getSmsCode(String mobile, SmsTplType smsTplType);

    Double getRealTimePrice();

    List<PriceDto> hourGoldPrices();

    Double getAndSetRealTimePrice(Double latestPrice);

    boolean setIfAbsentWeekPrice(String key, Double latestPrice, Date expiredAt);

    boolean setIfAbsentMonthPrice(String hourKey, Double latestPrice, Date monthPriceExpiredAt);

    boolean setIfAbsentDailyPrice(String dailyKey, Double latestPrice);

    boolean setIfAbsentRealTimePrice(String key, PriceDto latestPrice, Date weekPriceExpiredAt);

    void setRealTimePriceDto(RealtimePriceDto realTimePriceDto);

    RealtimePriceDto getRealTimePriceDto();

    <T> T get(final String key);

    boolean set(final String key, Object value, Long expireTime);

    Long incr(String key, Long value, Long expireTime);

    void logout(String token);

    String getIdByType(IdTypeConstant idTypeConstant);

    MemberSession getMemberSession(String token, HttpServletRequest request);
}
