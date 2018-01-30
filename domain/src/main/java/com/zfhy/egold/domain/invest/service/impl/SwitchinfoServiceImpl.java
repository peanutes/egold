package com.zfhy.egold.domain.invest.service.impl;

import com.zfhy.egold.domain.invest.dao.SwitchinfoMapper;
import com.zfhy.egold.domain.invest.entity.Switchinfo;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.invest.service.SwitchinfoService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;



@Service
@Transactional
@Slf4j
public class SwitchinfoServiceImpl extends AbstractService<Switchinfo> implements SwitchinfoService{
    @Autowired
    private SwitchinfoMapper switchinfoMapper;

    public void saveSwitchinfo(Integer memberId, Integer orderId, Double weight) {
        Switchinfo switchinfo = new Switchinfo();
        switchinfo.setStatus(0);
        switchinfo.setMemberId(memberId);
        switchinfo.setOrderId(orderId);
        switchinfo.setGoldWeight(weight);
        switchinfo.setCreateDate(new Date());
        switchinfo.setDelFlag("0");
        switchinfo.setUpdateDate(new Date());
        this.save(switchinfo);
    }

}
