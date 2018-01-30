package com.zfhy.egold.domain.invest.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.invest.dto.MemberCouponListDto;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MemberCouponMapper extends Mapper<MemberCoupon> {
    @Select("select * from invest_member_coupon c where c.member_id=#{memberId} and status=1 and now() between begin_time and end_time order by coupon_amount desc")
    List<MemberCoupon> findMemberEnableCoupons(Integer memberId);

    List<MemberCouponListDto> list(Map<String, String> params);

    @Update("update invest_member_coupon set status=2 where status=1 and end_time < now() ")
    void expiredCoupon();

    @Select("select sum(ifnull(coupon_amount, 0))from invest_member_coupon c where c.member_id=#{memberId}  and status=3")
    Double findMemberUsedCoupon(Integer memberId);
}
