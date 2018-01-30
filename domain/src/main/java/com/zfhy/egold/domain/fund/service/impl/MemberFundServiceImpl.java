package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dao.MemberFundMapper;
import com.zfhy.egold.domain.fund.dto.AvgBuyPriceDto;
import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.fund.dto.GoldAssetsDto;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.fund.service.WithdrawService;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.invest.service.IncomeDetailService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.entity.CouponConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.zfhy.egold.common.util.DoubleUtil.doubleAdd;
import static com.zfhy.egold.common.util.DoubleUtil.toScal4;



@Service
@Transactional
@Slf4j
public class MemberFundServiceImpl extends AbstractService<MemberFund> implements MemberFundService{
    @Autowired
    private MemberFundMapper memberFundMapper;

    @Autowired
    private FundRecordService fundRecordService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private MemberCouponService memberCouponService;


    @Autowired
    private IncomeDetailService incomeDetailService;

    @Override
    public void iniMemberFunAccount(Integer memberId) {

        MemberFund memberFundEntity = super.findBy("memberId", memberId);
        if (Objects.nonNull(memberFundEntity)) {
            throw new BusException("该用户已存在资金账户！");
        }
        MemberFund memberFund = new MemberFund();
        memberFund.setMemberId(memberId);
        memberFund.setVersion(1);
        memberFund.setCreateDate(new Date());
        memberFund.setDelFlag("0");
        memberFund.setCashBalance(0D);
        memberFund.setTermGoldBalance(0D);
        memberFund.setCurrentGoldBalance(0D);
        memberFund.setInvestBalance(0D);
        super.save(memberFund);

    }

    @Override
    public void subCashBalance(Integer memberId, Integer version, Double balancePayAmount) {
        int count = this.memberFundMapper.subCashBalance(memberId, version, balancePayAmount);
        if (count != 1) {
            throw new BusException("扣减余额操作失败，请联系客服妹妹");
        }
    }

    @Override
    public void switchInto(Order order, Integer version) {
        int count = this.memberFundMapper.switchInto(order.getMemberId(), order.getGoldWeight(), version);
        if (count != 1) {
            throw new BusException( "账户黄金余额转入失败，请联系客服妹妹");
        }


        this.fundRecordService.currentSwitchIntoTerm(order);
    }

    @Override
    public void addTermGoldBalance(Integer memberId, Double goldWeight) {
        this.memberFundMapper.addTermGoldBalance(memberId, goldWeight);
    }

    @Override
    public void addCurrentGoldBalance(Integer memberId, Double goldWeight) {
        this.memberFundMapper.addCurrentGoldBalance(memberId, goldWeight);
    }

    @Override
    public MemberFund checkCurrentGoldBalance(Integer memberId, Double goldWeight) {
        MemberFund memberFund = this.findBy("memberId", memberId);
        if (memberFund.getCurrentGoldBalance() < goldWeight) {
            throw new BusException("您好，您的活期金余额不足");
        }

        return memberFund;
    }

    @Override
    public void saleGold(Integer id, Integer version, SoldDetail soldDetail) {
        int count = this.memberFundMapper.saleGold(id, version, soldDetail.getGoldWeight(), soldDetail.getIncomeAmount());
        if (count != 1) {
            log.error("卖金时更改账户失败！");

            throw new BusException("您好，操作失败，请重新操作");
        }


        
        this.fundRecordService.saleGold(soldDetail);

    }

    @Override
    public void fallback(Order order) {
        Double balancePayAmount = order.getBalancePayAmount();
        if (DoubleUtil.notEqual(balancePayAmount, 0D)) {
            
            MemberFund memberFund = this.findBy("memberId", order.getMemberId());

            int count = this.memberFundMapper.fallBackCashBalance(memberFund.getId(), memberFund.getVersion(), order.getBalancePayAmount());
            if (count != 1) {
                throw new BusException("您好，回退余额失败！");
            }

            
            this.fundRecordService.fallBackOrder(order);

        }
    }

    
    @Override
    public void termSwitchCurrent(InvestRecord investRecord) {
        
        MemberFund memberFund = this.findBy("memberId", investRecord.getMemberId());
        if (investRecord.getInvestGoldWeight() > memberFund.getTermGoldBalance()) {
            throw new BusException("定期金余额不足");
        }

        int count = this.memberFundMapper.termSwitchCurrent(memberFund.getId(), memberFund.getVersion(), investRecord.getInvestGoldWeight());
        if (count != 1) {
            throw new BusException("你好，账户发生变化，操作失败，请您重新操作");
        }

        
        this.fundRecordService.termSwitchCurrent(investRecord);


    }

    @Override
    public void addInvestBalance(Integer memberId, Double addAmount) {
        this.memberFundMapper.addInvestBalance(memberId, addAmount);

    }

    @Override
    public void closeRiseFallInvest(InvestRecord investRecord, Double investAmount, Double interest) {
        
        Double totalAmount = doubleAdd(investAmount, interest);

        this.memberFundMapper.closeRiseFallInvest(investRecord.getMemberId(), investAmount, totalAmount);


        
        this.fundRecordService.closeRiseFallInvest(investRecord, investAmount, interest);

    }

    @Override
    public void rechargeSuccess(Recharge recharge) {
        
        this.memberFundMapper.rechargeSuccess(recharge.getMemberId(), recharge.getRechargeAmount());



        
        this.fundRecordService.recharge(recharge);

    }

    @Override
    public GoldAssetsDto goldAssets(Integer memberId) {
        MemberFund memberFund = this.findByMemberId(memberId);

        if (Objects.isNull(memberFund)) {
            return null;
        }


        Double realTimePrice = this.redisService.getRealTimePrice();


        
        AvgBuyPriceDto avgBuyPriceDto = this.investRecordService.getAvgBuyPrice(memberId);


        Double totalGold = doubleAdd(memberFund.getCurrentGoldBalance(), memberFund.getTermGoldBalance());
        return GoldAssetsDto.builder()
                .currentGold(toScal4(memberFund.getCurrentGoldBalance()))
                .termGold(toScal4(memberFund.getTermGoldBalance()))
                .totalGold(toScal4(totalGold))
                .goldAmount(DoubleUtil.toString(DoubleUtil.doubleMul(totalGold, realTimePrice)))
                .historyBalance(DoubleUtil.toString(avgBuyPriceDto.getHistoryBalance()))
                .currentGoldAvgPrice(DoubleUtil.toString(avgBuyPriceDto.getCurrentGoldAvgPrice()))
                .termGoldAvgPrice(DoubleUtil.toString(avgBuyPriceDto.getTermGoldAvgPrice()))
                .build();



    }

    @Override
    public MemberFund findByMemberId(Integer memberId) {
        return this.findBy("memberId", memberId);
    }

    
    @Override
    public void buyGold(Order order) {
        Double balancePayAmount = order.getBalancePayAmount();
        if (Objects.nonNull(balancePayAmount) && DoubleUtil.notEqual(0D, balancePayAmount)) {

            MemberFund memberFund = this.findBy("memberId", order.getMemberId());
            if (balancePayAmount > memberFund.getCashBalance()) {
                throw new BusException("余额不足");
            }

            
            this.subCashBalance(order.getMemberId(), memberFund.getVersion(), balancePayAmount);

            
            this.fundRecordService.cashBalanceBuyGold(order);


        }
    }

    
    @Override
    public void fallBackWithdraw(Withdraw withdrawEntity) {
        this.addCashBalance(withdrawEntity.getMemberId(), withdrawEntity.getWithdrawAmount());


        this.fundRecordService.fallbackWithdraw(withdrawEntity);

    }

    @Override
    public void addCashBalance(Integer memberId, Double amount) {
        this.memberFundMapper.addCashBalance(memberId, amount);
    }

    @Override
    public void withdrawGold(MemberFund memberFund, GoodsOrder goodsOrder) {
        
        int count = 0;
        if (DoubleUtil.notEqual(goodsOrder.getBalanceAmount(), 0D)) {
            
            count = this.memberFundMapper.subCashBalanceAndCurrentGold(memberFund.getId(), memberFund.getVersion(), goodsOrder.getBalanceAmount(), goodsOrder.getGoldWeight());
        } else {
            count = this.memberFundMapper.subCurrentGoldBalance(memberFund.getId(), memberFund.getVersion(), goodsOrder.getGoldWeight());

        }

        if (count != 1) {
            throw new BusException("您好，您的账户余额可能发生了变化，请重新操作");
        }


        this.fundRecordService.withdrawGold(goodsOrder);




    }

    @Override
    public void rollbackWithdrawGold(GoodsOrder goodsOrder) {
        
        Double balanceAmount = goodsOrder.getBalanceAmount();
        this.memberFundMapper.rollBackWithdrawGold(goodsOrder.getMemberId(), balanceAmount == null ? 0D : balanceAmount, goodsOrder.getGoldWeight());


        
        this.fundRecordService.rollBackWithdrawGold(goodsOrder);

    }

    @Override
    public void rollbackMallPay(GoodsOrder goodsOrder) {

        
        Double balanceAmount = goodsOrder.getBalanceAmount();

        if (DoubleUtil.notEqual(balanceAmount, 0D)) {
            this.memberFundMapper.rllBackMallPay(goodsOrder.getMemberId(),  balanceAmount);
        }

        this.fundRecordService.rollBackMallPay(goodsOrder);

    }

    @Override
    public List<MemberFund> list(Map<String, String> params) {
        List<MemberFund> memberFundList = this.memberFundMapper.list(params);

        Double realTimePrice = this.redisService.getRealTimePrice();

        for (MemberFund memberFund : memberFundList) {

            Double goldBalance = DoubleUtil.doubleAdd(memberFund.getTermGoldBalance(), memberFund.getCurrentGoldBalance(), 3);
            
            Double goldAmount = DoubleUtil.doubleMul(realTimePrice, goldBalance);

            
            Double totalAssets = DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(goldAmount, memberFund.getCashBalance()), memberFund.getInvestBalance(), 2);
            memberFund.setTotalAssets(totalAssets);


            Double onWithdraw = this.withdrawService.findOnWithdraw(memberFund.getMemberId());
            memberFund.setOnWithdraw(DoubleUtil.changeDecimal(onWithdraw, 2));


            Double income = this.incomeDetailService.findAllIncome(memberFund.getMemberId());


            Double coupon = this.memberCouponService.findMemberUsedCoupon(memberFund.getMemberId());
            memberFund.setInterest(income);
            memberFund.setCoupon(coupon);



        }
        return memberFundList;
    }

    
    @Override
    public void cashAward(Integer memberId, CouponConfig config, FundOperateType operateType, String awardRemark, Double couponAmount) {
        
        this.memberFundMapper.addCashBalance(memberId, couponAmount);

        
        this.fundRecordService.cashAward(memberId, config,  operateType, awardRemark, couponAmount);


    }

    @Override
    public void dailyIncome(InvestRecord investRecord, Double interest) {
        if (DoubleUtil.notEqual(interest, 0D)) {
            
            
            this.memberFundMapper.addCashBalance(investRecord.getMemberId(), interest);

            
            this.fundRecordService.income(investRecord, interest);



        }



    }

    @Override
    public void finishTermGold(InvestRecord investRecord, Double goldValue) {
        
        

        this.memberFundMapper.finishTermGold(investRecord.getMemberId(), investRecord.getInvestGoldWeight(), goldValue);

        this.fundRecordService.finishTermGold(investRecord, goldValue);

    }


}
