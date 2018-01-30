package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.domain.member.dao.LoginLogMapper;
import com.zfhy.egold.domain.member.dto.LoginStaticDto;
import com.zfhy.egold.domain.member.entity.LoginLog;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.member.service.LoginLogService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
@Transactional
public class LoginLogServiceImpl extends AbstractService<LoginLog> implements LoginLogService{
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void log(Member member, String terminalType, String terminalId, String ip) {

        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(ip);
        loginLog.setCreateDate(new Date());
        loginLog.setMemberId(member.getId());
        loginLog.setTerminalId(terminalId);
        loginLog.setTerminalType(terminalType);
        loginLog.setLoginTime(new Date());

        loginLogMapper.insert(loginLog);

    }

    @Override
    public List<LoginStaticDto> statisticLogin(Map<String, String> params) {
        return this.loginLogMapper.statisticLogin(params);

    }
}
