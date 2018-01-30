package com.zfhy.egold.domain.fund.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.fund.dto.FundAccount;
import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.sys.entity.CouponConfig;

import java.util.List;



public interface FundRecordService  extends Service<FundRecord> {

    void addBuyGoldRecord(Order order, String account);

    void withdraw(Integer memberId, Double withdrawAmount, Integer withdrawId);

    void saleGold(SoldDetail soldDetail);

    void fallBackOrder(Order order);

    void termSwitchCurrent(InvestRecord investRecord);

    List<FundRecord> findByOperateType(Integer memberId, FundOperateType operateType);

    List<FundRecord> findByAccount(Integer memberId, FundAccount fundAccount);

    void closeRiseFallInvest(InvestRecord investRecord, Double investAmount, Double interest);


    void recharge(Recharge recharge);


    void cashBalanceBuyGold(Order order);

    void currentSwitchIntoTerm(Order order);

    void fallbackWithdraw(Withdraw withdrawEntity);

    void withdrawGold(GoodsOrder goodsOrder);

    void rollBackWithdrawGold(GoodsOrder goodsOrder);

    void rollBackMallPay(GoodsOrder goodsOrder);

    void cashAward(Integer memberId, CouponConfig config, FundOperateType operateType, String awardRemark, Double couponAmount);

    Double queryCommission(Integer inviterId);

    void income(InvestRecord investRecord, Double interest);

    void finishTermGold(InvestRecord investRecord, Double goldValue);

}
