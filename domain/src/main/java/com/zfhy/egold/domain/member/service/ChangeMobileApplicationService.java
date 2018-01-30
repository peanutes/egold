package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.entity.ChangeMobileApplication;
import com.zfhy.egold.common.core.Service;



public interface ChangeMobileApplicationService  extends Service<ChangeMobileApplication> {

    void applyChangeMobile(Integer memberId, String mobilePhone, String newMobile, String smsCode, String idcardHandImg, String idcardFrontImg, String idcardBackImg, String bankCardImg);

    void applyChangeMobileWithSms(Integer memberId, String idCardNo, String realName, String newMobile, String smsCode);

}
