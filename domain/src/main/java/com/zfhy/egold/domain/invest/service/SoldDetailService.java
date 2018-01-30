package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.common.core.Service;



public interface SoldDetailService  extends Service<SoldDetail> {

    SoldDetail saleGold(Integer memberId, Double goldWeight);

    int countSoldTime(Integer memberId);

    SoldDetail confirmSaleGold(Integer memberId, Double goldWeight, String dealPwd, Boolean estimateFall);

    void soldTermGold(InvestRecord investRecord, Double goldValue, Double realTimePrice);

    Double historyBalance(Integer memberId);

}
