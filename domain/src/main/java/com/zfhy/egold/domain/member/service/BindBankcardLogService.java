package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.entity.BindBankcardLog;
import com.zfhy.egold.common.core.Service;



public interface BindBankcardLogService  extends Service<BindBankcardLog> {


    void addLog(Integer memberId, String bankCardNo, String mobile, String requestNo, String realName, String idCard);
}
