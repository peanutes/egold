package com.zfhy.egold.domain.sys.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dto.CouponStatus;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.domain.sys.dao.CouponConfigMapper;
import com.zfhy.egold.domain.sys.dto.CouponUseScene;
import com.zfhy.egold.domain.sys.entity.CouponConfig;
import com.zfhy.egold.domain.sys.entity.CouponConfigType;
import com.zfhy.egold.domain.sys.service.CouponConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class CouponConfigServiceImpl extends AbstractService<CouponConfig> implements CouponConfigService{
    @Autowired
    private CouponConfigMapper couponConfigMapper;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private MemberFundService memberFundService;


    
    @Override
    public void grant(Integer memberId, CouponUseScene couponUseScene) {
        List<CouponConfig> couponConfigs = this.findByUseScene(couponUseScene);
        List<MemberCoupon> memberCoupons = Lists.newArrayList();


        for (CouponConfig config : couponConfigs) {

            if (config.getType() == CouponConfigType.CASH_AWARD.getCode()) {
                


            } else {
                
                MemberCoupon coupon = this.convertToMemberCoupon(memberId, config);
                memberCoupons.add(coupon);

            }


        }


        if (CollectionUtils.isNotEmpty(memberCoupons)) {
            this.memberCouponService.save(memberCoupons);
        }


    }

    
    @Override
    public void grantByOrderAmount(Integer memberId, CouponUseScene useScene, Double orderTotalAmount, FundOperateType fundOperateType,  String awardRemark) {
        List<CouponConfig> configs = this.findByUseScene(useScene);

        
        CouponConfig matchCouponConfig = null;
        for (CouponConfig config : configs) {
            if (orderTotalAmount > config.getConditionInvestAmount()) {
                matchCouponConfig = config;
                break;
            }
        }

        if (Objects.nonNull(matchCouponConfig)) {
            this.memberFundService.cashAward(memberId, matchCouponConfig, fundOperateType, awardRemark, matchCouponConfig.getCouponAmount());

            /*Double conditionInvestAmount = matchCouponConfig.getConditionInvestAmount();

            List<MemberCoupon> memberCoupons = configs.stream()
                    .filter(e -> DoubleUtil.equal(e.getConditionInvestAmount(), conditionInvestAmount))
                    .map(e -> this.convertToMemberCoupon(memberId, e))
                    .collect(Collectors.toList());


            this.memberCouponService.save(memberCoupons);*/
        }
    }

    
    @Override
    public void investAward(Integer referee, CouponUseScene useScene, Double totalAmount, Integer term) {
        List<CouponConfig> configs = this.findByUseScene(useScene);

        if (CollectionUtils.isNotEmpty(configs)) {
            CouponConfig config = configs.get(0);

            
            Double amount = DoubleUtil.doubleDiv(DoubleUtil.doubleMul(totalAmount, new Double(term)), 365D, 4);

            Double couponAmount = DoubleUtil.doubleDiv(DoubleUtil.doubleMul(amount, config.getCouponAmount()), 100D, 2);

            this.memberFundService.cashAward(referee, config, FundOperateType.INVITER_CASH_AWARD, "邀请人全年反利", couponAmount);



        }

    }

    private MemberCoupon convertToMemberCoupon(Integer memberId, CouponConfig couponConfig) {
        MemberCoupon coupon = new MemberCoupon();
        coupon.setCouponAmount(couponConfig.getCouponAmount());
        coupon.setDelFlag("0");
        coupon.setUpdateDate(new Date());
        coupon.setCreateDate(new Date());
        coupon.setBeginTime(new Date());
        if (Objects.nonNull(couponConfig.getEffectDay())) {
            coupon.setEndTime(DateUtil.afterDays(new Date(), couponConfig.getEffectDay()));

        }
        coupon.setInvestAmountMin(couponConfig.getInvestAmountMin());
        coupon.setInvestAmountMinDesc(couponConfig.getInvestAmountMinDesc());
        coupon.setInvestDeadlineMinDesc(couponConfig.getInvestDeadlineMinDesc());
        coupon.setInvestDealineMin(couponConfig.getInvestDeadlineMin());

        coupon.setProductType(couponConfig.getProductType());
        coupon.setStatus(CouponStatus.NON_USE.getCode());
        coupon.setMemberId(memberId);
        coupon.setType(couponConfig.getType());
        coupon.setInsertTime(new Date());

        return coupon;
    }

    @CacheDuration(duration = 300L)
    @Cacheable("CouponConfigServiceImpl_findByUseScene")
    public List<CouponConfig> findByUseScene(CouponUseScene couponUseScene) {

        Condition condition = new Condition(CouponConfig.class);
        condition.setOrderByClause("condition_invest_amount desc");

        condition.createCriteria().andEqualTo("useScene", couponUseScene.getCode());

        return this.findByCondition(condition);

    }


    public static void main(String[] args) {
        Double amount = DoubleUtil.doubleDiv(DoubleUtil.doubleMul(10000D, new Double(7)), 365D, 4);

        Double couponAmount = DoubleUtil.doubleDiv(DoubleUtil.doubleMul(amount, 1D), 100D, 2);

        System.out.println(couponAmount);
    }
}
