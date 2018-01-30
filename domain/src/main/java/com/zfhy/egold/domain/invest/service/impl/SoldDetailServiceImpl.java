package com.zfhy.egold.domain.invest.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dto.AvgBuyPriceDto;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dao.SoldDetailMapper;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.invest.service.SoldDetailService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class SoldDetailServiceImpl extends AbstractService<SoldDetail> implements SoldDetailService {
    @Autowired
    private SoldDetailMapper soldDetailMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private InvestRecordService investRecordService;


    @Autowired
    private DictService dictService;


    @Autowired
    private MyorderService myorderService;


    @Override
    public SoldDetail saleGold(Integer memberId, Double goldWeight) {


        this.memberService.checkMember(memberId);


        
        MemberFund memberFund = this.memberFundService.checkCurrentGoldBalance(memberId, goldWeight);


        
        Double realTimePrice = this.redisService.getRealTimePrice();


        
        Double feeRate = this.dictService.findDouble(DictType.SALE_GOLD_FEE_RATE);
        
        Integer times = dictService.findIntByType(DictType.SALE_GOLD_FREE_TIME);
        int soldTimes = countSoldTime(memberId);

        Double fee = 0D;
        if (soldTimes >= times) {
            

            fee = DoubleUtil.doubleMul(feeRate, goldWeight, 2);

        }


        Double totalAmount = DoubleUtil.doubleMul(goldWeight, realTimePrice, 2);


        SoldDetail soldDetail = new SoldDetail();
        soldDetail.setUpdateDate(new Date());
        soldDetail.setDelFlag("0");
        soldDetail.setCreateDate(new Date());
        soldDetail.setFee(fee);
        soldDetail.setGoldWeight(goldWeight);
        soldDetail.setIncomeAmount(DoubleUtil.doubleSub(totalAmount, fee));
        soldDetail.setTotalAmount(totalAmount);

        soldDetail.setMemberId(memberId);
        soldDetail.setPrice(realTimePrice);
        soldDetail.setSoldTime(new Date());

        this.save(soldDetail);


        return soldDetail;
    }

    public int countSoldTime(Integer memberId) {
        
        Condition condition = new Condition(SoldDetail.class);
        LocalDate now = LocalDate.now();
        condition.createCriteria()
                .andBetween("soldTime", LocalDate.of(now.getYear(), now.getMonth(), 1), LocalDateTime.now())
                .andEqualTo("delFlag", "0")
                .andNotEqualTo("termGoldSold", "1")
                .andEqualTo("memberId", memberId);


        return this.soldDetailMapper.selectCountByCondition(condition);
    }

    @Override
    public SoldDetail confirmSaleGold(Integer memberId, Double goldWeight, String dealPwd, Boolean estimateFall) {

        this.memberService.checkMember(memberId);

        
        MemberFund memberFund = this.memberFundService.checkCurrentGoldBalance(memberId, goldWeight);

        
        this.memberService.checkDealPwd(memberId, dealPwd);

        
        Double feeRate = this.dictService.findDouble(DictType.SALE_GOLD_FEE_RATE);

        
        Integer times = dictService.findIntByType(DictType.SALE_GOLD_FREE_TIME);
        int soldTimes = countSoldTime(memberId);

        Double fee = 0D;
        if (soldTimes >= times) {
            


            double ceilGoldWeight = Math.ceil(goldWeight);
            fee = DoubleUtil.doubleMul(feeRate, ceilGoldWeight, 2);

        }

        SoldDetail soldDetail = new SoldDetail();
        Double realTimePrice = this.redisService.getRealTimePrice();

        Double totalAmount = 0D;

        if (Objects.nonNull(estimateFall) && estimateFall) {
            
            Double currentGoldFallPrice = this.investRecordService.getCurrentGoldFallPrice(memberId, 1);
            Double price = DoubleUtil.doubleSub(DoubleUtil.doubleMul(2D, currentGoldFallPrice), realTimePrice);

            totalAmount = DoubleUtil.doubleMul(goldWeight, price, 2);
            soldDetail.setProductName("活期金买跌");
            soldDetail.setEstimateFall(1);

            soldDetail.setBuyPrice(price);
            soldDetail.setTermGoldSold(0);

            
            Double profitOrLoss = DoubleUtil.doubleMul(DoubleUtil.doubleSub(price, realTimePrice), goldWeight, 2);

            soldDetail.setProfitOrLoss(profitOrLoss);

            
            this.investRecordService.soldCurrentGold(memberId, true, goldWeight);


        } else {


            totalAmount = DoubleUtil.doubleMul(goldWeight, realTimePrice, 2);
            soldDetail.setProductName("活期金买涨");
            soldDetail.setEstimateFall(0);

            
            Double currentGoldRisePrice = this.investRecordService.getCurrentGoldFallPrice(memberId, 0);

            Double profitOrLoss = DoubleUtil.doubleMul(DoubleUtil.doubleSub(realTimePrice, currentGoldRisePrice), goldWeight);
            soldDetail.setProfitOrLoss(profitOrLoss);
            soldDetail.setBuyPrice(currentGoldRisePrice);
            soldDetail.setTermGoldSold(0);

            
            this.investRecordService.soldCurrentGold(memberId, false, goldWeight);
        }

        soldDetail.setUpdateDate(new Date());
        soldDetail.setDelFlag("0");
        soldDetail.setCreateDate(new Date());
        soldDetail.setFee(fee);
        soldDetail.setGoldWeight(goldWeight);
        soldDetail.setIncomeAmount(DoubleUtil.doubleSub(totalAmount, fee));
        soldDetail.setTotalAmount(totalAmount);

        soldDetail.setMemberId(memberId);
        soldDetail.setPrice(realTimePrice);
        soldDetail.setSoldTime(new Date());

        this.save(soldDetail);

        this.myorderService.addSaleGold(soldDetail);

        
        this.memberFundService.saleGold(memberFund.getId(), memberFund.getVersion(), soldDetail);


        return soldDetail;
    }

    @Override
    public void soldTermGold(InvestRecord investRecord, Double goldValue, Double realTimePrice) {
        SoldDetail soldDetail = new SoldDetail();
        soldDetail.setTermGoldSold(1);
        soldDetail.setBuyPrice(investRecord.getPrice());
        soldDetail.setSoldTime(new Date());
        soldDetail.setPrice(realTimePrice);
        soldDetail.setCreateDate(new Date());
        soldDetail.setTotalAmount(goldValue);
        soldDetail.setDelFlag("0");
        soldDetail.setEstimateFall(0);
        soldDetail.setFee(0D);
        soldDetail.setIncomeAmount(goldValue);
        soldDetail.setMemberId(investRecord.getMemberId());
        soldDetail.setProductName(investRecord.getProductName());

        
        Double profitOrLoss = DoubleUtil.doubleMul(DoubleUtil.doubleSub(realTimePrice, investRecord.getPrice()), investRecord.getInvestGoldWeight(), 2);
        soldDetail.setProfitOrLoss(profitOrLoss);
    }

    @Override
    public Double historyBalance(Integer memberId) {

        return this.soldDetailMapper.historyBalance(memberId);
    }

    public static void main(String[] args) {
        log.info(">>>>{}", Math.ceil(1.375));

    }
}
