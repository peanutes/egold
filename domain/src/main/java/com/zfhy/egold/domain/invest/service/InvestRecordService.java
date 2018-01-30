package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.domain.fund.dto.AvgBuyPriceDto;
import com.zfhy.egold.domain.fund.dto.InvestStatus;
import com.zfhy.egold.domain.fund.dto.MyInvestDto;
import com.zfhy.egold.domain.fund.dto.MyInvestSummaryDto;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.order.dto.CurrentGoldAvgPrice;
import com.zfhy.egold.domain.order.entity.Order;

import java.util.List;



public interface InvestRecordService  extends Service<InvestRecord> {

    boolean hadInvestNewUserProduct(Integer memberId);

    List<InvestRecord> buyHistory(Integer id);

    void switchIntoTerm(Integer memberId, Double switchGoldWeight, Integer orderId);

    InvestRecord addInvestRecord(Order order);

    int countInvest(Integer memberId);

    List<InvestRecord> saleGold(Integer memberId, Double saleGoldWeight);


    int countInvestNewUser();

    List<InvestRecord> findNoPayInvestList();

    void calculateInterest(List<InvestRecord> investRecordList);

    List<InvestRecord> findNoPayRiseFallInvestList();

    void calculateRiseFallInterest(List<InvestRecord> investRecordList);

    boolean isFirstInvest(Integer memberId);

    List<InvestRecord> findTermGold(Integer memberId);

    MyInvestSummaryDto myInvestSummary(Integer memberId);

    List<MyInvestDto> myInvest(Integer memberId, InvestStatus investStatus);

    List<InvestRecord> findDeadLineInvestList();

    void setEndPriceOnRiseFallInvest(List<InvestRecord> investRecordList);

    List<InvestRecord> findShouldCloseInvestList();


    void setClosePriceOnRiseFallInvest(List<InvestRecord> investRecordList);

    Double findCurrentGoldBalanceFall(Integer memberId);

    CurrentGoldAvgPrice getCurrentGoldAvgPrice(Integer memberId);

    Double getCurrentGoldFallPrice(Integer memberId, Integer estimateFall);

    AvgBuyPriceDto getAvgBuyPrice(Integer memberId);

    void soldCurrentGold(Integer memberId, Boolean estimateFall, Double goldWeight);
}
