package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.domain.sys.entity.Sms;
import com.zfhy.egold.common.core.Service;



public interface SmsService  extends Service<Sms> {

    String send(String mobile, SmsTplType smsTplType, String terminalType, String ip);
}
