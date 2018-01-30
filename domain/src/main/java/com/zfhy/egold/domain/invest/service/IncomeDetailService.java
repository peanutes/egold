package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.invest.dto.IncomeDailyDto;
import com.zfhy.egold.domain.invest.entity.IncomeDetail;
import com.zfhy.egold.domain.invest.entity.InvestRecord;

import java.util.List;



public interface IncomeDetailService  extends Service<IncomeDetail> {

    Double findYesterdayIncome(Integer memberId);


    Double findAllIncome(Integer memberId);

    void addIncome(InvestRecord investRecord, Double interest, String remark);


    List<IncomeDailyDto> dailyIncome(Integer memberId);

    Double sumByMemberId(Integer memberId);

    Double sumInvestIncome(Integer memberId);

}
