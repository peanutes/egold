package com.zfhy.egold.domain.invest.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dto.AvgBuyPriceDto;
import com.zfhy.egold.domain.fund.dto.InvestStatus;
import com.zfhy.egold.domain.fund.dto.MyInvestDto;
import com.zfhy.egold.domain.fund.dto.MyInvestSummaryDto;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dao.InvestRecordMapper;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.dto.InvestRecordStatus;
import com.zfhy.egold.domain.invest.dto.RiseFallInvestStatus;
import com.zfhy.egold.domain.invest.dto.RiseOrFall;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.*;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.dto.CurrentGoldAvgPrice;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class InvestRecordServiceImpl extends AbstractService<InvestRecord> implements InvestRecordService{
    @Autowired
    private InvestRecordMapper investRecordMapper;

    @Autowired
    private SwitchinfoService switchinfoService;

    @Autowired
    private FinancialProductService financialProductService;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IncomeDetailService incomeDetailService;

    @Autowired
    private MemberFundService memberFundService;


    @Autowired
    private MemberService memberService;

    @Autowired
    private DictService dictService;

    @Autowired
    private SoldDetailService soldDetailService;


    @Override
    public boolean hadInvestNewUserProduct(Integer memberId) {
        List<InvestRecord> investRecords = investRecordMapper.findNewUserInvestRecord(memberId);
        return CollectionUtils.isNotEmpty(investRecords);
    }

    @Override
    public List<InvestRecord> buyHistory(Integer productId) {

        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria().andEqualTo("productId", productId);
        condition.setOrderByClause("create_date desc");

        return super.findByCondition(condition);

    }

    
    @Override
    public void switchIntoTerm(Integer memberId, Double switchGoldWeight, Integer orderId) {
        List<InvestRecord> investRecordList = findCurrentByMemberId(memberId, FinancialProductType.CURRENT_DEPOSIT);

        Double sumWeight = 0D;

        for (InvestRecord investRecord : investRecordList) {


            sumWeight += investRecord.getInvestGoldWeight();


            if (sumWeight > switchGoldWeight) {


                
                investRecord.setStatus(InvestRecordStatus.HAD_REPAY.getCode());
                investRecord.setUpdateDate(new Date());
                investRecord.setRemarks("已转入到定期产品");
                investRecord.setInvestGoldWeight(DoubleUtil.doubleSub(sumWeight, switchGoldWeight, 4));

                super.update(investRecord);


                break;
            }

            
            investRecord.setStatus(InvestRecordStatus.HAD_REPAY.getCode());
            investRecord.setUpdateDate(new Date());
            investRecord.setRemarks("已转入到定期产品");
            investRecord.setInvestGoldWeight(0D);
            super.update(investRecord);

            
            this.switchinfoService.saveSwitchinfo(memberId, orderId, investRecord.getInvestGoldWeight());
            if (Objects.equals(sumWeight, switchGoldWeight)) {
                
                break;
            }

        }

        if (sumWeight < switchGoldWeight) {
            throw new BusException("活期金账户余额不足");
        }
    }

    @Override
    public InvestRecord addInvestRecord(Order order) {
        String financeProductType = order.getFinanceProductType();

        Member member = this.memberService.findById(order.getMemberId());

        FinancialProduct product = this.financialProductService.findById(order.getProductId());
        Integer discountCouponId = order.getDiscountCouponId();
        Integer cashCouponId = order.getCashCouponId();
        MemberCoupon discountCoupon = null;
        MemberCoupon cashCoupon = null;
        if (Objects.nonNull(discountCouponId)) {
            discountCoupon = this.memberCouponService.findById(discountCouponId);
        }
        if (Objects.nonNull(cashCouponId)) {
            cashCoupon = this.memberCouponService.findById(cashCouponId);
        }

        InvestRecord investRecord = new InvestRecord();
        investRecord.setMemberId(order.getMemberId());
        investRecord.setProductId(order.getProductId());
        investRecord.setProductName(order.getProductName());
        investRecord.setProductType(financeProductType);
        investRecord.setInvestAmount(order.getTotalAmount());
        investRecord.setInvestGoldWeight(order.getGoldWeight());
        investRecord.setPrice(order.getCurrentGoldPrice());
        investRecord.setStatus(InvestRecordStatus.WAIT_REPAY.getCode());
        investRecord.setTerminalType(order.getTerminalType());
        investRecord.setEstimateFall(order.getEstimateFall());
        investRecord.setOrderId(order.getId());


        investRecord.setEffectDate(DateUtil.tomorrow());
        if (Objects.equals(financeProductType, FinancialProductType.TERM_DEPOSIT.name())) {
            investRecord.setDeadlineDate(DateUtil.afterDays(product.getTerm()));
        } else if (Objects.equals(financeProductType, FinancialProductType.RISE_FALL.name())) {
            
            
            investRecord.setEffectDate(product.getInterestBeginDate());
            
            investRecord.setDeadlineDate(DateUtil.afterDays(product.getDeadLineDate(), product.getTerm()-1));
            
            investRecord.setInvestDeadlineDate(product.getDeadLineDate());

            investRecord.setInvestTerm(product.getTerm());

        }
        investRecord.setAnnualRate(product.getAnnualRate());
        Double additionalRate = 0D;
        if (Objects.nonNull(discountCoupon)) {
            additionalRate = discountCoupon.getCouponAmount();
        }
        investRecord.setAdditionalRate(additionalRate);
        investRecord.setCreateDate(new Date());
        investRecord.setUpdateDate(new Date());
        investRecord.setDelFlag("0");

        investRecord.setMobile(member.getMobilePhone());

        this.save(investRecord);

        
        if (Objects.nonNull(discountCoupon)) {
            discountCoupon.setInvestId(investRecord.getId());
            this.memberCouponService.update(discountCoupon);
        }

        if (Objects.nonNull(cashCoupon)) {
            cashCoupon.setInvestId(investRecord.getId());
            this.memberCouponService.update(cashCoupon);
        }


        return investRecord;

    }

    @Override
    public int countInvest(Integer memberId) {
        return this.investRecordMapper.countInvest(memberId);

    }


    
    public List<InvestRecord> findCurrentByMemberId(Integer memberId, FinancialProductType type) {

        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("memberId", memberId)
                .andEqualTo("productType", type.name())
                .andEqualTo("status", InvestRecordStatus.WAIT_REPAY.getCode())
                .andEqualTo("delFlag", "0");

        condition.setOrderByClause("create_date desc");


        return super.findByCondition(condition);
    }



    
    @Override
    public List<InvestRecord> saleGold(Integer memberId, Double saleGoldWeight) {
        List<InvestRecord> saleList = Lists.newArrayList();

        List<InvestRecord> investRecordList = findCurrentByMemberId(memberId, FinancialProductType.CURRENT_DEPOSIT);

        Double sumWeight = 0D;

        for (InvestRecord investRecord : investRecordList) {


            sumWeight += investRecord.getInvestGoldWeight();


            if (sumWeight > saleGoldWeight) {
                saleList.add(investRecord);


                
                
                investRecord.setUpdateDate(new Date());
                investRecord.setRemarks("已卖出");
                investRecord.setInvestGoldWeight(DoubleUtil.doubleSub(sumWeight, saleGoldWeight, 4));

                super.update(investRecord);


                break;
            } else {
                
                investRecord.setStatus(InvestRecordStatus.HAD_REPAY.getCode());
                investRecord.setUpdateDate(new Date());
                investRecord.setRemarks("已卖出");
                investRecord.setInvestGoldWeight(0D);
                super.update(investRecord);
                saleList.add(investRecord);

                if (Objects.equals(sumWeight, saleGoldWeight)) {
                    
                    break;
                }

            }


        }

        if (sumWeight < saleGoldWeight) {
            throw new BusException("活期金账户余额不足");
        }

        return saleList;
    }

    @Override
    public int countInvestNewUser() {
        return this.investRecordMapper.countInvestNewUser();
    }

    
    @Override
    public List<InvestRecord> findNoPayInvestList() {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", "0")
                .andNotEqualTo("productType", FinancialProductType.RISE_FALL.name())
                .andLessThan("effectDate", DateUtil.today());

        condition.orderBy("createDate");

        return this.findByCondition(condition);
    }

    @Override
    public void calculateInterest(List<InvestRecord> investRecordList) {
        investRecordList.stream().forEach(e -> {
            try {
                this.calculateInterest(e);
            } catch (Throwable throwable) {
                log.error("计算利息失败", throwable);
            }
        });

    }

    
    @Override
    public List<InvestRecord> findNoPayRiseFallInvestList() {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", InvestRecordStatus.WAIT_REPAY.getCode())
                .andEqualTo("productType", FinancialProductType.RISE_FALL.name())
                .andLessThan("deadLineDate", DateUtil.today());

        condition.orderBy("createDate");

        return this.findByCondition(condition);
    }

    
    @Override
    public void calculateRiseFallInterest(List<InvestRecord> investRecordList) {

        investRecordList.stream().forEach(e->{
            try {
                calculateRiseFallInterest(e);
            } catch (Throwable throwable) {
                log.error("黄金计算利息失败", throwable);
            }
        });

    }

    
    @Override
    public boolean isFirstInvest(Integer memberId) {
        int count = this.investRecordMapper.investCount(memberId);

        return count == 1;

    }

    @Override
    public List<InvestRecord> findTermGold(Integer memberId) {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("productType", FinancialProductType.TERM_DEPOSIT.name())
                .andEqualTo("memberId", memberId)
                .andEqualTo("delFlag", "0");
        condition.setOrderByClause("create_date desc");

        return this.findByCondition(condition);
    }

    @Override
    public MyInvestSummaryDto myInvestSummary(Integer memberId) {
        Double onInvestAmount = this.investRecordMapper.onInvestAmount(memberId);

        Double sumInvestAmount = this.investRecordMapper.sumInvestAmount(memberId);

        Double sumIncome = this.incomeDetailService.sumInvestIncome(memberId);

        return MyInvestSummaryDto.builder()
                .onInvestAmount(DoubleUtil.trimToZero(onInvestAmount))
                .sumInvestAmount(DoubleUtil.trimToZero(sumInvestAmount))
                .sumIncome(DoubleUtil.trimToZero(sumIncome))
                .build();

    }

    @Override
    public List<MyInvestDto> myInvest(Integer memberId, InvestStatus investStatus) {
        return this.investRecordMapper.myInvest(memberId, investStatus.getCode());

    }

    @Override
    public List<InvestRecord> findDeadLineInvestList() {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", InvestRecordStatus.WAIT_REPAY.getCode())
                .andEqualTo("productType", FinancialProductType.RISE_FALL.name())
                .andBetween("investDeadlineDate", LocalDate.now(), DateUtil.tomorrow());

        condition.orderBy("createDate");

        return this.findByCondition(condition);
    }

    @Override
    public void setEndPriceOnRiseFallInvest(List<InvestRecord> investRecordList) {
        investRecordList.stream().forEach(e->{
            try {
                setEndPriceOnRiseFallInvest(e);
            } catch (Throwable throwable) {
                log.error("设置截止申购价失败", throwable);
            }
        });

    }

    @Override
    public List<InvestRecord> findShouldCloseInvestList() {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", InvestRecordStatus.WAIT_REPAY.getCode())
                .andEqualTo("productType", FinancialProductType.RISE_FALL.name())
                .andIsNotNull("closePrice")
                .andBetween("deadlineDate", LocalDate.now(), DateUtil.tomorrow());

        condition.orderBy("createDate");

        return this.findByCondition(condition);


    }

    @Override
    public void setClosePriceOnRiseFallInvest(List<InvestRecord> investRecordList) {
        investRecordList.stream().forEach(e->{
            try {
                setClosePriceOnRiseFallInvest(e);
            } catch (Throwable throwable) {
                log.error("设置看涨跌产品收盘价失败", throwable);
            }
        });

    }

    @Override
    public Double findCurrentGoldBalanceFall(Integer memberId) {

        return this.investRecordMapper.findCurrentGoldBalanceFall(memberId);
    }

    @Override
    public CurrentGoldAvgPrice getCurrentGoldAvgPrice(Integer memberId) {
        Double riseAvgPrice = this.investRecordMapper.queryCurrentGoldAvgPrice(memberId, 0);
        Double fallAvgPrice = this.investRecordMapper.queryCurrentGoldAvgPrice(memberId, 1);
        return CurrentGoldAvgPrice.builder()
                .estimateFallPrice(fallAvgPrice == null ? 0D : DoubleUtil.changeDecimal(fallAvgPrice, 2))
                .estimateRisePrice(riseAvgPrice == null ? 0d : DoubleUtil.changeDecimal(riseAvgPrice, 2))
                .build();

    }

    @Override
    public Double getCurrentGoldFallPrice(Integer memberId, Integer estimateFall) {

        Double price = this.investRecordMapper.queryCurrentGoldAvgPrice(memberId, estimateFall);
        return price == null ? 0D : price;
    }

    @Override
    public AvgBuyPriceDto getAvgBuyPrice(Integer memberId) {
        Double termGoldAvgPrice = this.investRecordMapper.queryTermGoldAvgPrice(memberId);
        Double currentGoldAvgPrice = this.investRecordMapper.queryAllCurrentGoldAvgPrice(memberId);
        Double historyBalance = this.soldDetailService.historyBalance(memberId);

        return AvgBuyPriceDto.builder()
                .termGoldAvgPrice(termGoldAvgPrice)
                .currentGoldAvgPrice(currentGoldAvgPrice)
                .historyBalance(historyBalance)
                .build();



    }

    @Override
    public void soldCurrentGold(Integer memberId, Boolean estimateFall, Double goldWeight) {
        List<InvestRecord> saleList = Lists.newArrayList();

        List<InvestRecord> investRecordList = findCurrentByMemberIdAndEstimateFall(memberId, FinancialProductType.CURRENT_DEPOSIT, estimateFall);

        Double sumWeight = 0D;

        for (InvestRecord investRecord : investRecordList) {

            sumWeight += investRecord.getInvestGoldWeight();
            if (sumWeight > goldWeight) {
                saleList.add(investRecord);

                
                
                investRecord.setUpdateDate(new Date());
                investRecord.setRemarks("已卖出");
                investRecord.setInvestGoldWeight(DoubleUtil.doubleSub(sumWeight, goldWeight, 3));

                super.update(investRecord);

                break;
            } else {
                
                investRecord.setStatus(InvestRecordStatus.HAD_REPAY.getCode());
                investRecord.setUpdateDate(new Date());
                investRecord.setRemarks("已卖出");
                investRecord.setInvestGoldWeight(0D);
                super.update(investRecord);
                saleList.add(investRecord);

                if (Objects.equals(sumWeight, goldWeight)) {
                    
                    break;
                }

            }


        }

        if (sumWeight < goldWeight) {
            throw new BusException("活期金账户余额不足");
        }

    }

    private List<InvestRecord> findCurrentByMemberIdAndEstimateFall(Integer memberId, FinancialProductType type, Boolean estimateFall) {
        Condition condition = new Condition(InvestRecord.class);
        condition.createCriteria()
                .andEqualTo("memberId", memberId)
                .andEqualTo("productType", type.name())
                .andEqualTo("status", InvestRecordStatus.WAIT_REPAY.getCode())
                .andEqualTo("estimateFall", estimateFall ? 1 : 0)
                .andEqualTo("delFlag", "0");

        condition.setOrderByClause("create_date desc");


        return super.findByCondition(condition);

    }


    private void setClosePriceOnRiseFallInvest(InvestRecord investRecord) {
        
        Double realTimePrice = this.redisService.getRealTimePrice();
        investRecord.setClosePrice(realTimePrice);
        this.update(investRecord);


        
        FinancialProduct product = this.financialProductService.findById(investRecord.getProductId());
        
        product.setClosePrice(realTimePrice);
        this.financialProductService.update(product);
    }

    private void setEndPriceOnRiseFallInvest(InvestRecord investRecord) {
        
        Double realTimePrice = this.redisService.getRealTimePrice();

        this.investRecordMapper.updateEndPrice(investRecord.getId(), investRecord.getVersion(), realTimePrice);


        

        FinancialProduct product = this.financialProductService.findById(investRecord.getProductId());

        if (Objects.equals(product.getInvestStatus(), RiseFallInvestStatus.PROCESSING.getCode())) {
            
            product.setAimAmount(product.getHadInvestAmount());
            product.setInvestStatus(RiseFallInvestStatus.FULL_SCALE.getCode());
        }

        
        product.setDeadlinePrice(realTimePrice);
        this.financialProductService.update(product);
    }

    private void calculateRiseFallInterest(InvestRecord investRecord) {
        
        Double endPrice = investRecord.getEndPrice();
        
        Double closePrice = investRecord.getClosePrice();

        if (Objects.isNull(endPrice) || DoubleUtil.equal(endPrice, 0D)) {
            throw new BusException("该看涨跌投资记录没有结束时价格，需要手工处理");
        }

        Double limitRate = this.dictService.findDouble(DictType.RISE_FALL_LIMIT_RATE);


        Double investAmount = investRecord.getInvestAmount();

        
        Double rate = DoubleUtil.doubleAdd(investRecord.getAnnualRate(), investRecord.getAdditionalRate());


        Double activityRate = 0D;
        
        FinancialProduct product = this.financialProductService.findById(investRecord.getProductId());
        int riseOrFall = product.getRiseOrFall();
        if (riseOrFall == RiseOrFall.RISE.getCode()) {
            
            if (endPrice > closePrice) {
                activityRate = DoubleUtil.doubleDiv(DoubleUtil.doubleSub(endPrice, closePrice, 4), closePrice, 4);
            }
        } else {
            
            if (endPrice < closePrice) {
                activityRate = DoubleUtil.doubleDiv(DoubleUtil.doubleSub(closePrice, endPrice, 4), closePrice, 4);
            }
        }


        Double totalRate = DoubleUtil.doubleAdd(rate, activityRate);
        totalRate = limitRate > totalRate ? totalRate : limitRate;


        Double inst = DoubleUtil.doubleMul(DoubleUtil.doubleMul(investAmount, totalRate), new Double(investRecord.getInvestTerm()) );
        Double interest = DoubleUtil.doubleDiv(inst, 36500D, 2);




        
        this.incomeDetailService.addIncome(investRecord, interest, String.format("投资理财产品%s每日收益", investRecord.getProductName()));


        
        this.memberFundService.closeRiseFallInvest(investRecord, investAmount, interest);


        
        int count = this.investRecordMapper.repay(investRecord.getId(), investRecord.getVersion(), InvestRecordStatus.HAD_REPAY.getCode());
        if (count != 1) {
            throw new BusException("修改投资状态失败");
        }


    }

    private void calculateInterest(InvestRecord investRecord) {

        Double realTimePrice = this.redisService.getRealTimePrice();

        String productType = investRecord.getProductType();

        
        
        Double rate = DoubleUtil.doubleAdd(investRecord.getAnnualRate(), investRecord.getAdditionalRate());
        Double investAmount = DoubleUtil.doubleMul(realTimePrice, investRecord.getInvestGoldWeight());


        Double interest = DoubleUtil.doubleDiv(DoubleUtil.doubleMul(investAmount, rate), 36500D, 2);

        this.incomeDetailService.addIncome(investRecord, interest, String.format("投资理财产品%s每日收益", investRecord.getProductName()));

        this.memberFundService.dailyIncome(investRecord, interest);


        if (FinancialProductType.TERM_DEPOSIT.name().equals(productType)) {
            
            Date deadlineDate = investRecord.getDeadlineDate();
            if (deadlineDate.after(DateUtil.yesterday()) && deadlineDate.before(DateUtil.today())) {
                
                
                this.updateStatus(investRecord.getId(), InvestRecordStatus.HAD_REPAY);

                
                Double goldValue = DoubleUtil.doubleMul(investRecord.getInvestGoldWeight(), realTimePrice, 2);
                this.memberFundService.finishTermGold(investRecord, goldValue);

                
                this.soldDetailService.soldTermGold(investRecord, goldValue, realTimePrice);

            }
        }





        /*
        Date deadlineDate = investRecord.getDeadlineDate();
        if (deadlineDate.after(DateUtil.yesterday()) && deadlineDate.before(DateUtil.today())) {
            
            
            this.updateStatus(investRecord.getId(), InvestRecordStatus.HAD_REPAY);

            
            this.termSwitchCurrent(investRecord);


            
            this.memberFundService.termSwitchCurrent(investRecord);

        }*/

        
        this.updateVersion(investRecord.getId(), investRecord.getVersion());


    }

    
    private void termSwitchCurrent(InvestRecord investRecord) {

        FinancialProduct currentProduct = this.financialProductService.findCurrentProduct();
        Double realTimePrice = this.redisService.getRealTimePrice();

        InvestRecord currentInvestRecord = new InvestRecord();
        currentInvestRecord.setMemberId(investRecord.getMemberId());
        currentInvestRecord.setProductId(currentProduct.getId());
        currentInvestRecord.setProductName(currentProduct.getProductName());
        currentInvestRecord.setProductType(currentProduct.getProductType());
        currentInvestRecord.setInvestAmount(DoubleUtil.doubleMul(investRecord.getInvestGoldWeight(), realTimePrice, 2));
        currentInvestRecord.setInvestGoldWeight(investRecord.getInvestGoldWeight());
        currentInvestRecord.setPrice(realTimePrice);
        currentInvestRecord.setStatus(InvestRecordStatus.WAIT_REPAY.getCode());
        currentInvestRecord.setEffectDate(DateUtil.tomorrow());
        currentInvestRecord.setDeadlineDate(DateUtil.maxDay());
        currentInvestRecord.setAnnualRate(currentProduct.getAnnualRate());
        currentInvestRecord.setAdditionalRate(0D);
        currentInvestRecord.setCreateDate(new Date());
        currentInvestRecord.setUpdateDate(new Date());
        currentInvestRecord.setRemarks("定期金到期到成活期金");
        currentInvestRecord.setVersion(0);
        currentInvestRecord.setMobile(investRecord.getMobile());

        this.save(currentInvestRecord);

    }

    private void updateStatus(Integer id, InvestRecordStatus status) {
        this.investRecordMapper.updateStatus(id, status.getCode());

    }

    private void updateVersion(Integer id, int version) {
        int count = this.investRecordMapper.updateVersion(id, version);
        if (count != 1) {
            throw new BusException("您好，数据已发生变化，请重新操作");
        }

    }


}
