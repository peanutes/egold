package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.domain.fund.dao.SaveGoldMapper;
import com.zfhy.egold.domain.fund.entity.SaveGold;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.fund.service.SaveGoldService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class SaveGoldServiceImpl extends AbstractService<SaveGold> implements SaveGoldService{
    @Autowired
    private SaveGoldMapper saveGoldMapper;

}
