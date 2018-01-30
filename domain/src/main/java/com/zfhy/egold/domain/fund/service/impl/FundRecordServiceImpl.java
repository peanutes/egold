package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dao.FundRecordMapper;
import com.zfhy.egold.domain.fund.dto.FundAccount;
import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.sys.dto.CouponUseScene;
import com.zfhy.egold.domain.sys.entity.CouponConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class FundRecordServiceImpl extends AbstractService<FundRecord> implements FundRecordService{
    @Autowired
    private FundRecordMapper fundRecordMapper;

    @Override
    public void addBuyGoldRecord(Order order, String account) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setAccount(account);
        fundRecord.setMemberId(order.getMemberId());
        fundRecord.setOperateMoney(0D);
        fundRecord.setGoldWeight(order.getGoldWeight());
        fundRecord.setOperateId(order.getId());
        fundRecord.setProductName(order.getProductName());
        fundRecord.setDisplayLabel(String.format("+%s克", DoubleUtil.toScal4(order.getGoldWeight())));
        fundRecord.setOperateType(FundOperateType.BUYGOLD.getCode());
        fundRecord.setCreateDate(new Date());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setDelFlag("0");
        if (Objects.equals(order.getFinanceProductType(), FinancialProductType.TERM_DEPOSIT.name())) {
            fundRecord.setRemarks("买定期金");
        }

        if (Objects.equals(order.getFinanceProductType(), FinancialProductType.CURRENT_DEPOSIT.name())) {
            if (order.getEstimateFall() != null && order.getEstimateFall() == 1) {
                
                fundRecord.setRemarks("活期金买跌");
            } else {
                fundRecord.setRemarks("活期金买涨");
            }
        }
        fundRecord.setInvestPrice(order.getCurrentGoldPrice());
        this.save(fundRecord);

    }

    @Override
    public void withdraw(Integer memberId, Double withdrawAmount, Integer withdrawId) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setMemberId(memberId);
        fundRecord.setOperateMoney(0D);
        fundRecord.setGoldWeight(null);
        fundRecord.setOperateId(withdrawId);
        fundRecord.setProductName(null);
        fundRecord.setDisplayLabel(String.format("-%s元", DoubleUtil.toString(withdrawAmount)));
        fundRecord.setOperateType(FundOperateType.WITHDRAW.getCode());
        fundRecord.setOperateMoney(withdrawAmount);
        fundRecord.setCreateDate(new Date());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setRemarks("提现");

        this.save(fundRecord);

    }

    @Override
    public void saleGold(SoldDetail soldDetail) {
        FundRecord saleGold = new FundRecord();
        FundRecord addCash = new FundRecord();
        FundRecord addFee = new FundRecord();

        saleGold.setMemberId(soldDetail.getMemberId());
        saleGold.setAccount(FundAccount.CURRENT.name());
        saleGold.setGoldWeight(soldDetail.getGoldWeight());
        saleGold.setProductName(soldDetail.getProductName());
        saleGold.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(soldDetail.getGoldWeight())));
        saleGold.setOperateType(FundOperateType.SELLGOLD.getCode());
        saleGold.setCreateDate(new Date());
        saleGold.setUpdateDate(new Date());
        saleGold.setOperateId(soldDetail.getId());
        saleGold.setDelFlag("0");
        Integer estimateFall = soldDetail.getEstimateFall();
        if (Objects.nonNull(estimateFall) && estimateFall == 1) {
            saleGold.setRemarks("卖活期金(看跌)");
        } else {
            saleGold.setRemarks("卖活期金（看涨）");
        }


        addCash.setMemberId(soldDetail.getMemberId());
        addCash.setAccount(FundAccount.CASH.name());
        addCash.setGoldWeight(0D);
        addCash.setOperateMoney(soldDetail.getTotalAmount());
        addCash.setProductName(soldDetail.getProductName());
        addCash.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(soldDetail.getTotalAmount())));
        addCash.setOperateType(FundOperateType.SELLGOLD.getCode());
        addCash.setCreateDate(new Date());
        addCash.setUpdateDate(new Date());
        addCash.setOperateId(soldDetail.getId());
        addCash.setDelFlag("0");
        addCash.setRemarks("卖活期金收入");



        addFee.setMemberId(soldDetail.getMemberId());
        addFee.setAccount(FundAccount.CASH.name());
        addFee.setGoldWeight(0D);
        addFee.setOperateMoney(soldDetail.getFee());
        addFee.setProductName(soldDetail.getProductName());
        addFee.setDisplayLabel(String.format("-%s元", DoubleUtil.toString(soldDetail.getFee())));
        addFee.setOperateType(FundOperateType.SELLGOLD.getCode());
        addFee.setCreateDate(new Date());
        addFee.setUpdateDate(new Date());
        addFee.setOperateId(soldDetail.getId());
        addFee.setDelFlag("0");
        addFee.setRemarks("卖活期金手续费");


        this.save(saleGold);
        this.save(addCash);

        if (soldDetail.getFee() != null && DoubleUtil.notEqual(soldDetail.getFee(), 0D)) {
            this.save(addFee);
        }

    }

    @Override
    public void fallBackOrder(Order order) {
        
        Double balancePayAmount = order.getBalancePayAmount();
        if (Objects.nonNull(balancePayAmount) && DoubleUtil.notEqual(balancePayAmount, 0D)) {
            FundRecord fundRecord = new FundRecord();
            fundRecord.setMemberId(order.getMemberId());
            fundRecord.setOperateMoney(balancePayAmount);
            fundRecord.setAccount(FundAccount.CASH.name());
            fundRecord.setGoldWeight(0D);
            fundRecord.setProductName(order.getProductName());
            fundRecord.setOperateId(order.getId());
            fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(balancePayAmount)));
            fundRecord.setOperateType(FundOperateType.ORDER_EXPIRED.getCode());
            fundRecord.setCreateDate(new Date());
            fundRecord.setUpdateDate(new Date());
            fundRecord.setDelFlag("0");
            fundRecord.setRemarks("订单过期，回退余额");
            this.save(fundRecord);
        }


    }

    @Override
    public void termSwitchCurrent(InvestRecord investRecord) {
        FundRecord termGold = new FundRecord();
        FundRecord currentGold = new FundRecord();

        termGold.setMemberId(investRecord.getMemberId());
        termGold.setAccount(FundAccount.TERM.name());
        termGold.setGoldWeight(investRecord.getInvestGoldWeight());
        termGold.setProductName(investRecord.getProductName());
        termGold.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(investRecord.getInvestGoldWeight())));
        termGold.setOperateType(FundOperateType.TERM_EXPIRED.getCode());
        termGold.setCreateDate(new Date());
        termGold.setUpdateDate(new Date());
        termGold.setOperateId(investRecord.getId());
        termGold.setDelFlag("0");
        termGold.setRemarks("定期金到期,转成活期金");



        termGold.setMemberId(investRecord.getMemberId());
        termGold.setAccount(FundAccount.CURRENT.name());
        termGold.setGoldWeight(investRecord.getInvestGoldWeight());
        termGold.setProductName(investRecord.getProductName());
        termGold.setDisplayLabel(String.format("+%s克", DoubleUtil.toScal4(investRecord.getInvestGoldWeight())));
        termGold.setOperateType(FundOperateType.TERM_SWITCH_INTO.getCode());
        termGold.setCreateDate(new Date());
        termGold.setUpdateDate(new Date());
        termGold.setOperateId(investRecord.getId());
        termGold.setDelFlag("0");
        termGold.setRemarks("定期金到期,转成活期金");

        this.save(Arrays.asList(termGold, currentGold));

    }

    @Override
    public List<FundRecord> findByOperateType(Integer memberId, FundOperateType operateType) {
        Condition condition = new Condition(FundRecord.class);

        condition.createCriteria()
                .andEqualTo("memberId", memberId)
                .andEqualTo("operateType", operateType.getCode());
        condition.setOrderByClause("create_date desc");
        return this.findByCondition(condition);
    }

    @Override
    public List<FundRecord> findByAccount(Integer memberId, FundAccount fundAccount) {
        Condition condition = new Condition(FundRecord.class);

        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("memberId", memberId);

        if (Objects.nonNull(fundAccount)) {
            criteria.andEqualTo("account", fundAccount.name());
        }

        condition.setOrderByClause("create_date desc");
        return this.findByCondition(condition);

    }

    @Override
    public void closeRiseFallInvest(InvestRecord investRecord, Double investAmount, Double interest) {
        
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(investAmount)));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(investRecord.getMemberId());
        fundRecord.setOperateId(investRecord.getId());
        fundRecord.setProductName(investRecord.getProductName());
        fundRecord.setOperateMoney(investAmount);
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.RISE_FALL_EXPIRED.getCode());
        fundRecord.setRemarks("看涨跌到期");
        this.save(fundRecord);

        FundRecord fundRecord2 = new FundRecord();
        fundRecord2.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(investRecord.getInvestGoldWeight())));
        fundRecord2.setCreateDate(new Date());
        fundRecord2.setDelFlag("0");
        fundRecord2.setGoldWeight(investRecord.getInvestGoldWeight());
        fundRecord2.setMemberId(investRecord.getMemberId());
        fundRecord2.setOperateId(investRecord.getId());
        fundRecord2.setProductName(investRecord.getProductName());
        fundRecord2.setOperateMoney(0D);
        fundRecord2.setUpdateDate(new Date());
        fundRecord2.setAccount(FundAccount.INVEST.name());
        fundRecord2.setOperateType(FundOperateType.RISE_FALL_EXPIRED.getCode());
        fundRecord2.setRemarks("看涨跌到期");
        this.save(fundRecord2);





    }

    
    @Override
    public void recharge(Recharge recharge) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setRemarks("充值");
        fundRecord.setUpdateDate(new Date());
        fundRecord.setOperateMoney(recharge.getRechargeAmount());
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setProductName("充值");
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(recharge.getRechargeAmount())));
        fundRecord.setMemberId(recharge.getMemberId());
        fundRecord.setOperateType(FundOperateType.RECHARGE.getCode());
        fundRecord.setRemarks("充值金额");

        this.save(fundRecord);

    }

    
    @Override
    public void cashBalanceBuyGold(Order order) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("-%s元", DoubleUtil.toString(order.getBalancePayAmount())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(order.getMemberId());
        fundRecord.setOperateId(order.getId());
        fundRecord.setProductName(order.getProductName());
        fundRecord.setOperateMoney(order.getBalancePayAmount());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.BUYGOLD.getCode());
        fundRecord.setRemarks("买金");
        this.save(fundRecord);
    }

    
    @Override
    public void currentSwitchIntoTerm(Order order) {
        
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(order.getGoldWeight())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setOperateType(FundOperateType.BUYGOLD.getCode());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(order.getGoldWeight());
        fundRecord.setMemberId(order.getMemberId());
        fundRecord.setOperateId(order.getId());
        fundRecord.setProductName(order.getProductName());
        fundRecord.setOperateMoney(0D);
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CURRENT.name());
        fundRecord.setRemarks("活期金转定期金");
        fundRecord.setInvestPrice(order.getCurrentGoldPrice());
        this.save(fundRecord);
    }

    @Override
    public void fallbackWithdraw(Withdraw withdrawEntity) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(withdrawEntity.getWithdrawAmount())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(withdrawEntity.getMemberId());
        fundRecord.setOperateId(withdrawEntity.getId());
        fundRecord.setProductName("提现退款");
        fundRecord.setOperateMoney(withdrawEntity.getWithdrawAmount());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.WITHDRAW.getCode());
        fundRecord.setRemarks("提现审核不通过退款");
        this.save(fundRecord);
    }

    @Override
    public void withdrawGold(GoodsOrder goodsOrder) {

        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("-%s元", DoubleUtil.toString(goodsOrder.getBalanceAmount())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(goodsOrder.getMemberId());
        fundRecord.setOperateId(goodsOrder.getId());
        fundRecord.setProductName(goodsOrder.getGoodsName());
        fundRecord.setOperateMoney(goodsOrder.getBalanceAmount());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.WITHDRAW_GOLD.getCode());
        fundRecord.setRemarks("提金余额支付");
        this.save(fundRecord);


        
        FundRecord fundRecord2 = new FundRecord();
        fundRecord2.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(goodsOrder.getGoldWeight())));
        fundRecord2.setCreateDate(new Date());
        fundRecord2.setOperateType(FundOperateType.WITHDRAW_GOLD.getCode());
        fundRecord2.setDelFlag("0");
        fundRecord2.setGoldWeight(goodsOrder.getGoldWeight());
        fundRecord2.setMemberId(goodsOrder.getMemberId());
        fundRecord2.setOperateId(goodsOrder.getId());
        fundRecord2.setProductName(goodsOrder.getGoodsName());
        fundRecord2.setOperateMoney(0D);
        fundRecord2.setUpdateDate(new Date());
        fundRecord2.setAccount(FundAccount.CURRENT.name());
        fundRecord2.setRemarks("提金");
        fundRecord.setInvestPrice(goodsOrder.getPrice());
        this.save(fundRecord2);




    }

    @Override
    public void rollBackWithdrawGold(GoodsOrder goodsOrder) {

        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(goodsOrder.getBalanceAmount())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(goodsOrder.getMemberId());
        fundRecord.setOperateId(goodsOrder.getId());
        fundRecord.setProductName(goodsOrder.getGoodsName());
        fundRecord.setOperateMoney(goodsOrder.getBalanceAmount());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.WITHDRAW_GOLD.getCode());
        fundRecord.setRemarks("提金余额支付失败回退");
        this.save(fundRecord);


        
        FundRecord fundRecord2 = new FundRecord();
        fundRecord2.setDisplayLabel(String.format("+%s克", DoubleUtil.toScal4(goodsOrder.getGoldWeight())));
        fundRecord2.setCreateDate(new Date());
        fundRecord2.setOperateType(FundOperateType.WITHDRAW_GOLD.getCode());
        fundRecord2.setDelFlag("0");
        fundRecord2.setGoldWeight(goodsOrder.getGoldWeight());
        fundRecord2.setMemberId(goodsOrder.getMemberId());
        fundRecord2.setOperateId(goodsOrder.getId());
        fundRecord2.setProductName(goodsOrder.getGoodsName());
        fundRecord2.setOperateMoney(0D);
        fundRecord2.setUpdateDate(new Date());
        fundRecord2.setAccount(FundAccount.CURRENT.name());
        fundRecord2.setRemarks("提金支付失败回退");
        this.save(fundRecord2);

    }

    @Override
    public void rollBackMallPay(GoodsOrder goodsOrder) {

        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(goodsOrder.getBalanceAmount())));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(goodsOrder.getMemberId());
        fundRecord.setOperateId(goodsOrder.getId());
        fundRecord.setProductName(goodsOrder.getGoodsName());
        fundRecord.setOperateMoney(goodsOrder.getBalanceAmount());
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.WITHDRAW_GOLD.getCode());
        fundRecord.setRemarks("商城余额支付失败回退");
        this.save(fundRecord);

    }

    @Override
    public void cashAward(Integer memberId, CouponConfig config, FundOperateType operateType, String awardRemark, Double couponAmount) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(couponAmount)));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(memberId);
        fundRecord.setOperateId(config.getId());
        fundRecord.setProductName(CouponUseScene.getLabelByCode(config.getUseScene()));
        fundRecord.setOperateMoney(couponAmount);
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(operateType.getCode());
        fundRecord.setRemarks(awardRemark);
        this.save(fundRecord);

    }

    
    @Override
    public Double queryCommission(Integer inviterId) {

        Double commission = this.fundRecordMapper.queryCommission(inviterId, FundOperateType.INVITER_CASH_AWARD.getCode());

        return Objects.isNull(commission) ? 0D : commission;


    }

    @Override
    public void income(InvestRecord investRecord, Double interest) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(interest)));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(investRecord.getMemberId());
        fundRecord.setOperateId(investRecord.getId());
        fundRecord.setProductName(investRecord.getProductName());
        fundRecord.setOperateMoney(interest);
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.INTEREST_INCOME.getCode());
        fundRecord.setRemarks(investRecord.getProductName()+"利息收入");
        this.save(fundRecord);

    }

    @Override
    public void finishTermGold(InvestRecord investRecord, Double goldValue) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setDisplayLabel(String.format("+%s元", DoubleUtil.toString(goldValue)));
        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        fundRecord.setGoldWeight(null);
        fundRecord.setMemberId(investRecord.getMemberId());
        fundRecord.setOperateId(investRecord.getId());
        fundRecord.setProductName(investRecord.getProductName());
        fundRecord.setOperateMoney(goldValue);
        fundRecord.setUpdateDate(new Date());
        fundRecord.setAccount(FundAccount.CASH.name());
        fundRecord.setOperateType(FundOperateType.TERM_EXPIRED.getCode());
        fundRecord.setRemarks("定期金到期");
        this.save(fundRecord);


        FundRecord fundRecord2 = new FundRecord();
        fundRecord2.setDisplayLabel(String.format("-%s克", DoubleUtil.toScal4(investRecord.getInvestGoldWeight())));
        fundRecord2.setCreateDate(new Date());
        fundRecord2.setDelFlag("0");
        fundRecord2.setGoldWeight(investRecord.getInvestGoldWeight());
        fundRecord2.setMemberId(investRecord.getMemberId());
        fundRecord2.setOperateId(investRecord.getId());
        fundRecord2.setProductName(investRecord.getProductName());
        fundRecord2.setOperateMoney(0D);
        fundRecord2.setUpdateDate(new Date());
        fundRecord2.setAccount(FundAccount.TERM.name());
        fundRecord2.setOperateType(FundOperateType.TERM_EXPIRED.getCode());
        fundRecord2.setRemarks("定期金到期");
        this.save(fundRecord2);


    }


}
