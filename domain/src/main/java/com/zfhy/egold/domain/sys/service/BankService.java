package com.zfhy.egold.domain.sys.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.sys.entity.Bank;



public interface BankService  extends Service<Bank> {

    String getBankLogo(String bankCode);

    void checkAndAddBank(String bankCode, String bankName);

}
