package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.domain.fund.dao.WithdrawGoldMapper;
import com.zfhy.egold.domain.fund.entity.WithdrawGold;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.fund.service.WithdrawGoldService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class WithdrawGoldServiceImpl extends AbstractService<WithdrawGold> implements WithdrawGoldService{
    @Autowired
    private WithdrawGoldMapper withdrawGoldMapper;

}
