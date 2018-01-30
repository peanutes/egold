package com.zfhy.egold.domain.fund.service;

import com.zfhy.egold.domain.fund.dto.FundOperateType;
import com.zfhy.egold.domain.fund.dto.GoldAssetsDto;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.sys.entity.CouponConfig;

import java.util.List;
import java.util.Map;



public interface MemberFundService  extends Service<MemberFund> {

    void iniMemberFunAccount(Integer memberId);

    void subCashBalance(Integer memberId, Integer version, Double balancePayAmount);

    void switchInto(Order order, Integer version);

    void addTermGoldBalance(Integer memberId, Double goldWeight);

    void addCurrentGoldBalance(Integer memberId, Double goldWeight);

    MemberFund checkCurrentGoldBalance(Integer memberId, Double goldWeight);

    void saleGold(Integer id, Integer version,  SoldDetail soldDetail);

    void fallback(Order order);

    void termSwitchCurrent(InvestRecord investRecord);

    void addInvestBalance(Integer memberId, Double addAmount);

    void closeRiseFallInvest(InvestRecord investRecord, Double investAmount, Double allInterest);

    void rechargeSuccess(Recharge recharge);

    GoldAssetsDto goldAssets(Integer memberId);

    MemberFund findByMemberId(Integer memberId);

    void buyGold(Order order);

    void fallBackWithdraw(Withdraw withdrawEntity);

    void addCashBalance(Integer memberId, Double withdrawAmount);

    void withdrawGold(MemberFund memberFund, GoodsOrder goodsOrder);

    void rollbackWithdrawGold(GoodsOrder goodsOrder);

    void rollbackMallPay(GoodsOrder goodsOrder);

    List<MemberFund> list(Map<String, String> params);

    void cashAward(Integer memberId, CouponConfig config, FundOperateType operateType, String awardRemark, Double couponAmount);

    void dailyIncome(InvestRecord investRecord, Double interest);

    void finishTermGold(InvestRecord investRecord, Double goldValue);





}
