package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.domain.member.dao.BindBankcardLogMapper;
import com.zfhy.egold.domain.member.entity.BindBankcardLog;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.member.service.BindBankcardLogService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;



@Service
@Transactional
@Slf4j
public class BindBankcardLogServiceImpl extends AbstractService<BindBankcardLog> implements BindBankcardLogService{
    @Autowired
    private BindBankcardLogMapper bindBankcardLogMapper;

    @Override
    public void addLog(Integer memberId, String bankCardNo, String mobile, String requestNo, String realName, String idCard) {
        BindBankcardLog bindBankcardLog = new BindBankcardLog();
        bindBankcardLog.setDelFlag("0");
        bindBankcardLog.setCreateDate(new Date());
        bindBankcardLog.setCardNo(bankCardNo);
        bindBankcardLog.setMemberId(memberId);
        bindBankcardLog.setMobile(mobile);
        bindBankcardLog.setRequestNo(requestNo);
        bindBankcardLog.setRealName(realName);
        bindBankcardLog.setIdcardNo(idCard);
        bindBankcardLog.setStatus(0);
        bindBankcardLog.setUpdateDate(new Date());
        this.save(bindBankcardLog);

    }
}
