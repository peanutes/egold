package com.zfhy.egold.domain.fund.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MemberFundMapper extends Mapper<MemberFund> {


    @Update("update fund_member_fund set cash_balance=cash_balance-#{param3}, version=version+1 where member_id=#{param1} and cash_balance>=#{param3}")
    int subCashBalance(Integer memberId, Integer version, Double balancePayAmount);

    @Update("update fund_member_fund set current_gold_balance=current_gold_balance-#{param2}, version=version+1 where member_id=#{param1} and version=#{param3}")
    int switchInto(Integer memberId, Double goldWeight, Integer version);

    @Update("update fund_member_fund set term_gold_balance=term_gold_balance+#{param2} where member_id=#{param1}")
    void addTermGoldBalance(Integer memberId, Double goldWeight);

    @Update("update fund_member_fund set current_gold_balance=current_gold_balance+#{param2} where member_id=#{param1}")
    void addCurrentGoldBalance(Integer memberId, Double goldWeight);

    @Update("update fund_member_fund set current_gold_balance=current_gold_balance-${param3}, cash_balance=cash_balance+#{param4}, version=version+1 where id=#{param1} and version=#{param2}")
    int saleGold(Integer id, Integer version, Double goldWeight, Double amount);


    @Update("update fund_member_fund set cash_balance=cash_balance+#{param3}, version=version+1 where id=#{param1} and version=#{param2}")
    int fallBackCashBalance(Integer id, Integer version, Double balancePayAmount);

    @Update("update fund_member_fund set term_gold_balance=term_gold_balance-#{param3}, current_gold_balance=current_gold_balance+#{param3}, version=version+1 where id=#{param1} and version=#{param2}")
    int termSwitchCurrent(Integer id, Integer version, Double investGoldWeight);

    @Update("update fund_member_fund set invest_balance=invest_balance+#{param2} where member_id=#{param1}")
    void addInvestBalance(Integer memberId, Double addAmount);

    @Update("update fund_member_fund set invest_balance=invest_balance-#{param2}, cash_balance=cash_balance+#{param3} where member_id=#{param1}")
    void closeRiseFallInvest(Integer memberId, Double investAmount, Double totalAmount);

    @Update("update fund_member_fund set cash_balance=cash_balance+#{param2} where member_id=#{param1}")
    void rechargeSuccess(Integer memberId, Double rechargeAmount);

    @Update("update fund_member_fund set cash_balance=cash_balance+#{param2} where member_id=#{param1}")
    void addCashBalance(Integer memberId, Double amount);

    @Update("update fund_member_fund set cash_balance=cash_balance-#{param3}, current_gold_balance=current_gold_balance-#{param4}, version=version+1 where id=#{param1} and version=#{param2}")
    int subCashBalanceAndCurrentGold(Integer id, Integer version, Double balanceAmount, Double goldWeight);

    @Update("update fund_member_fund set current_gold_balance=current_gold_balance-#{param3}, version=version+1 where id=#{param1} and version=#{param2}")
    int subCurrentGoldBalance(Integer id, Integer version, Double goldWeight);

    @Update("update fund_member_fund set cash_balance=cash_balance+#{param2}, current_gold_balance=current_gold_balance+#{param3} where member_id=#{param1}")
    void rollBackWithdrawGold(Integer memberId, double cashBalance, Double goldWight);

    @Update("update fund_member_fund set cash_balance=cash_balance+#{param2} where member_id=#{param1}")
    void rllBackMallPay(Integer memberId, Double balanceAmount);

    List<MemberFund> list(Map<String, String> params);

    @Update("update fund_member_fund set term_gold_balance=term_gold_balance-#{param2}, cash_balance=cash_balance+#{param3}, version=version+1 where member_id=#{param1} ")
    void finishTermGold(Integer memberId, Double investGoldWeight, Double goldValue);
}
