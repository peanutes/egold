package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.entity.BankcardUnbindApplication;
import com.zfhy.egold.common.core.Service;



public interface BankcardUnbindApplicationService  extends Service<BankcardUnbindApplication> {

    void applyUnbindBankCard(Integer memberId, String idcardFrontImg, String idcardBackImg);

    void approved(Integer id);
}
