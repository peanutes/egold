package com.zfhy.egold.domain.invest.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.invest.dao.IncomeDetailMapper;
import com.zfhy.egold.domain.invest.dto.IncomeDailyDto;
import com.zfhy.egold.domain.invest.entity.IncomeDetail;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.IncomeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;



@Service
@Transactional
@Slf4j
public class IncomeDetailServiceImpl extends AbstractService<IncomeDetail> implements IncomeDetailService{
    @Autowired
    private IncomeDetailMapper incomeDetailMapper;

    
    @Override
    @Cacheable("IncomeDetailServiceImpl_findYesterdayIncome")
    @CacheDuration(duration = 1200L)
    public Double findYesterdayIncome(Integer memberId) {
        return this.incomeDetailMapper.findYesterdayIncome(memberId);
    }

    
    @Cacheable("IncomeDetailServiceImpl_findAllIncome")
    @CacheDuration(duration = 1200L)
    @Override
    public Double findAllIncome(Integer memberId) {
        return this.incomeDetailMapper.findAllIncome(memberId);
    }

    @Override
    public void addIncome(InvestRecord investRecord, Double interest, String remark) {

        IncomeDetail incomeDetail = new IncomeDetail();
        incomeDetail.setInvestId(investRecord.getId());
        incomeDetail.setMemberId(investRecord.getMemberId());
        incomeDetail.setIncome(interest);
        incomeDetail.setIncomeTime(new Date());
        incomeDetail.setCreateDate(new Date());
        incomeDetail.setUpdateDate(new Date());
        incomeDetail.setDelFlag("0");
        incomeDetail.setRemarks(remark);
        incomeDetail.setIncomeDate(new Date());


        this.save(incomeDetail);
    }

    @Override
    public List<IncomeDailyDto> dailyIncome(Integer memberId) {
        return this.incomeDetailMapper.dailyIncom(memberId);
    }

    @Override
    @CacheDuration(duration = 300L)
    @Cacheable("IncomeDetailServiceImpl_sumByMemberId")
    public Double sumByMemberId(Integer memberId) {
        return this.incomeDetailMapper.sumByMemberId(memberId);

    }

    @Override
    public Double sumInvestIncome(Integer memberId) {

        return this.incomeDetailMapper.sumInvestIncome(memberId);
    }

}
