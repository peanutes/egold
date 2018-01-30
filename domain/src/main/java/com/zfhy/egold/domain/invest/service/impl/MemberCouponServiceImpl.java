package com.zfhy.egold.domain.invest.service.impl;

import com.google.gson.Gson;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.dao.MemberCouponMapper;
import com.zfhy.egold.domain.invest.dto.CashAndDiscountCouponDto;
import com.zfhy.egold.domain.invest.dto.CouponStatus;
import com.zfhy.egold.domain.invest.dto.MemberCouponDto;
import com.zfhy.egold.domain.invest.dto.MemberCouponListDto;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;



@Service
@Transactional
@Slf4j
public class MemberCouponServiceImpl extends AbstractService<MemberCoupon> implements MemberCouponService{
    @Autowired
    private MemberCouponMapper memberCouponMapper;

    @Autowired
    private FinancialProductService financialProductService;

    @Autowired
    private RedisService redisService;

    @Override
    public CashAndDiscountCouponDto findMemberEnableCoupons(Integer memberId, Integer productId, Double orderAmount) {
        CashAndDiscountCouponDto cashAndDiscountCouponDto = new CashAndDiscountCouponDto();
        List<MemberCouponDto> cashCoupons = cashAndDiscountCouponDto.getCashCoupons();
        List<MemberCouponDto> discountCoupons = cashAndDiscountCouponDto.getDiscountCoupons();


        FinancialProduct product = this.financialProductService.findById(productId);
        if (Objects.isNull(product)) {
            throw new BusException("产品不存在");
        }

        List<MemberCoupon> memberEnableCoupons = this.memberCouponMapper.findMemberEnableCoupons(memberId);

        for (MemberCoupon membercoupon : memberEnableCoupons) {
            
            Integer investDealineMin = membercoupon.getInvestDealineMin();

            if (product.getTerm() < investDealineMin) {
                
                continue;
            }

            
            String productType = membercoupon.getProductType();
            if (!Objects.equals(productType, "0") && Objects.nonNull(productType)) {
                List<String> productTypeList = Arrays.asList(productType.split(","));
                if (!productTypeList.contains(product.getProductType())) {
                    continue;
                }


            }

            
            Double investAmountMin = membercoupon.getInvestAmountMin();
            if (DoubleUtil.notEqual(investAmountMin, 0D)) {
                if (orderAmount < investAmountMin) {
                    continue;
                }
            }


            if (membercoupon.getType() == 1) {
                
                cashCoupons.add(new MemberCouponDto().convertFrom(membercoupon));
            } else {
                discountCoupons.add(new MemberCouponDto().convertFrom(membercoupon));
            }

        }

        cashCoupons.sort((c1, c2) -> c2.getCouponAmount().compareTo(c1.getCouponAmount()));

        discountCoupons.sort((c1, c2) -> c2.getCouponAmount().compareTo(c1.getCouponAmount()));


        
        /*if (CollectionUtils.isNotEmpty(cashCoupons)) {
            cashAndDiscountCouponDto.setCashCoupons(Collections.singletonList(cashCoupons.get(0)));
        }

        if (CollectionUtils.isNotEmpty(discountCoupons)) {
            cashAndDiscountCouponDto.setDiscountCoupons(Collections.singletonList(discountCoupons.get(0)));
        }*/

        return cashAndDiscountCouponDto;

    }

    @Override
    public List<MemberCouponListDto> list(Map<String, String> params) {

        return this.memberCouponMapper.list(params);
    }

    @Override
    public void fallback(Order order) {
        Integer cashCouponId = order.getCashCouponId();
        Integer discountCouponId = order.getDiscountCouponId();
        if (Objects.nonNull(cashCouponId)) {
            fallbackCoupon(cashCouponId);
        }

        if (Objects.nonNull(discountCouponId)) {
            fallbackCoupon(discountCouponId);

        }

    }

    @Override
    public void expiredCoupon() {
        this.memberCouponMapper.expiredCoupon();
    }

    @Override
    public Double findMemberUsedCoupon(Integer memberId) {

        return this.memberCouponMapper.findMemberUsedCoupon(memberId);


    }


    private void fallbackCoupon(Integer couponId) {
        MemberCoupon coupon = this.findById(couponId);

        if (Objects.equals(coupon.getStatus(), CouponStatus.EXPIRED.getCode())) {
            return;
        }

        coupon.setStatus(CouponStatus.NON_USE.getCode());
        coupon.setUpdateDate(new Date());
        this.update(coupon);
    }

    public static void main(String[] args) {
        List<Double> d = Arrays.asList(0.2D, 0.1D, 2D, 3D, 0.9d);
        d.sort((e1, e2) -> e2.compareTo(e1));

        System.out.println(new Gson().toJson(d));
    }


}
