package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.dto.LoginStaticDto;
import com.zfhy.egold.domain.member.entity.LoginLog;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.member.entity.Member;

import java.util.List;
import java.util.Map;



public interface LoginLogService  extends Service<LoginLog> {

    void log(Member member, String terminalType, String terminalId, String ip);

    List<LoginStaticDto> statisticLogin(Map<String, String> params);

}
