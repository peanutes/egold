package com.zfhy.egold.common.aspect;

import com.google.gson.Gson;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.ThreadUtil;
import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.CouponUseScene;
import com.zfhy.egold.domain.sys.service.CouponConfigService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Aspect
@Component
@Slf4j
public class CouponActivityAspect {

    @Autowired
    private CouponConfigService couponConfigService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FinancialProductService financialProductService;



    
    @Pointcut("@annotation(com.zfhy.egold.common.aspect.ann.Register)")
    public void registerPointcut() {
    }

    
    @Pointcut("@annotation(com.zfhy.egold.common.aspect.ann.BindBankCard)")
    public void bindBankCardPointcut() {
    }


    
    @Pointcut("@annotation(com.zfhy.egold.common.aspect.ann.AfterInvest)")
    public void afterInvestPointcut() {
    }

    
    @AfterReturning(returning = "rvt", pointcut = "registerPointcut()")
    public void afterRegister(Object rvt) {

        if (Objects.isNull(rvt)) {
            return;
        }

        try {
            Result<MemberOutPutDto> rs = (Result<MemberOutPutDto>) rvt;
            if (rs.getCode() == ResultCode.SUCCESS.getCode()) {
                
                MemberOutPutDto member = rs.getData();

                ThreadUtil.getThreadPollProxy().execute(() -> this.couponConfigService.grant(member.getId(), CouponUseScene.REGISTER));

            }

        } catch (Exception e) {
            log.error("注册时放发卡卷失败，", e);
        }


    }


    
    @AfterReturning(returning = "rvt", pointcut = "bindBankCardPointcut()")
    public void afterBindBankCard(JoinPoint joinPoint, Object rvt) {
        if (Objects.isNull(rvt)) {
            return;
        }

        try {

            Result<MemberOutPutDto> rs = (Result<MemberOutPutDto>) rvt;
            if (rs.getCode() == ResultCode.SUCCESS.getCode()) {
                Object[] args = joinPoint.getArgs();
                if (args.length >= 3) {
                    SysParameter sysParameter = new Gson().fromJson(new Gson().toJson(args[2]), SysParameter.class);

                    MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
                    
                    ThreadUtil.getThreadPollProxy()
                            .execute(() -> this.couponConfigService.grant(member.getId(), CouponUseScene.BIND_BANK_CARD_SELF));

                    Integer referee = member.getReferee();
                    if (Objects.nonNull(referee)) {
                        
                        ThreadUtil.getThreadPollProxy()
                                .execute(() -> this.couponConfigService.grant(referee, CouponUseScene.BIND_BANK_CARD_INVESTOR));

                    }
                }
            }

        } catch (Exception e) {
            log.error("绑卡时放发卡卷失败，", e);
        }

    }


    
    @AfterReturning(returning = "rvt", pointcut = "afterInvestPointcut()")
    public void afterInvest(JoinPoint joinPoint,Object rvt) {
        try {

            Object[] args = joinPoint.getArgs();
            if (args.length >= 1) {
                Order order = (Order) args[0];

                if (Objects.isNull(order)) {
                    return;
                }


                log.info(">>>>>>有户投资成功，订单：{}", new Gson().toJson(order));


                boolean firstInvest = investRecordService.isFirstInvest(order.getMemberId());

                
                if (firstInvest) {
                    

                    FinancialProduct product = this.financialProductService.findById(order.getProductId());
                    if (!Objects.equals(product.getProductType(), FinancialProductType.TERM_DEPOSIT.name())) {
                        
                        return;
                    }

                    if (product.getTerm() < 30) {
                        
                        return;
                    }

                    
                    ThreadUtil.getThreadPollProxy()
                            .execute(() -> this.couponConfigService.grantByOrderAmount(order.getMemberId(), CouponUseScene.FIRST_INVEST_SELF, order.getTotalAmount(), FundOperateType.FIRST_INVEST_CASH_AWARD, "首次投资现金奖励"));


                    Member member = this.memberService.findById(order.getMemberId());

                    if (Objects.isNull(member.getReferee())) {
                        return;
                    }


                    Date createDate = member.getCreateDate();

                    if (createDate.after(DateUtil.afterDays(-90))) {
                        

                        
                        ThreadUtil.getThreadPollProxy()
                                .execute(() -> this.couponConfigService.grantByOrderAmount(member.getReferee(), CouponUseScene.FIRST_INVEST_INVESTOR, order.getTotalAmount(), FundOperateType.INVITER_CASH_AWARD, "被邀请人首次投资现金奖励"));
                    }

                } else {
                    
                    Member member = this.memberService.findById(order.getMemberId());

                    if (Objects.isNull(member.getReferee())) {
                        return;
                    }

                    
                    log.info("create date >>>>{}", DateUtil.toString(member.getCreateDate()));
                    log.info("一年前 date >>>>{}", DateUtil.toString(DateUtil.afterDays(-365)));
                    if (member.getCreateDate().before(DateUtil.afterDays(-365))) {
                        
                        return;
                    }

                    FinancialProduct product = this.financialProductService.findById(order.getProductId());
                    if (Objects.equals(product.getProductType(), FinancialProductType.CURRENT_DEPOSIT.name())) {
                        
                        return;
                    }

                    ThreadUtil.getThreadPollProxy()
                            .execute(() -> this.couponConfigService.investAward(member.getReferee(), CouponUseScene.INVESTOR_AWARD, order.getTotalAmount(), product.getTerm()));

                }

            }




        } catch (Exception e) {
            log.error("投资时放发卡卷失败，", e);
        }


    }

}
