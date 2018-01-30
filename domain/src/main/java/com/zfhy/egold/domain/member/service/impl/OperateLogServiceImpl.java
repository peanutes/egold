package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.domain.member.dao.OperateLogMapper;
import com.zfhy.egold.domain.member.entity.OperateLog;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.member.service.OperateLogService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
@Slf4j
public class OperateLogServiceImpl extends AbstractService<OperateLog> implements OperateLogService{
    @Autowired
    private OperateLogMapper operateLogMapper;

}
