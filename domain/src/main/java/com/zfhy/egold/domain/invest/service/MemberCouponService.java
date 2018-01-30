package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.domain.invest.dto.CashAndDiscountCouponDto;
import com.zfhy.egold.domain.invest.dto.MemberCouponListDto;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.order.entity.Order;

import java.util.List;
import java.util.Map;



public interface MemberCouponService  extends Service<MemberCoupon> {

    CashAndDiscountCouponDto findMemberEnableCoupons(Integer memberId, Integer productId, Double orderAmount);

    List<MemberCouponListDto> list(Map<String, String> params);

    void fallback(Order order);


    void expiredCoupon();

    Double findMemberUsedCoupon(Integer memberId);
}
