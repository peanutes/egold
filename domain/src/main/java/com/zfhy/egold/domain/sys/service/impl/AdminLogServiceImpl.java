package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.domain.sys.dao.AdminLogMapper;
import com.zfhy.egold.domain.sys.entity.AdminLog;
import com.zfhy.egold.common.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.sys.service.AdminLogService;

import org.springframework.beans.factory.annotation.Autowired;



@Service
@Transactional
public class AdminLogServiceImpl extends AbstractService<AdminLog> implements AdminLogService{
    @Autowired
    private AdminLogMapper adminLogMapper;

}
