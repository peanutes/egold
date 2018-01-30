package com.zfhy.egold.domain.member.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.member.dto.LoginStaticDto;
import com.zfhy.egold.domain.member.entity.LoginLog;

import java.util.List;
import java.util.Map;

public interface LoginLogMapper extends Mapper<LoginLog> {
    List<LoginStaticDto> statisticLogin(Map<String, String> params);

}
