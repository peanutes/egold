package com.zfhy.egold.domain.sys.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.sys.dao.BankMapper;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.entity.Bank;
import com.zfhy.egold.domain.sys.service.BankService;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;



@Service
@Transactional
@Slf4j
public class BankServiceImpl extends AbstractService<Bank> implements BankService{
    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private DictService dictService;

    @CacheDuration(duration = 360)
    @Cacheable("BankServiceImpl_getBankLogo")
    @Override
    public String getBankLogo(String bankCode) {

        Bank bank = this.findBy("bankCode", bankCode);

        String bankLogo;
        if (Objects.isNull(bank) || StringUtils.isBlank(bank.getBankLogo())) {
            bankLogo = this.dictService.findStringByType(DictType.DEFAULT_BANK_LOGO);
        } else {
            bankLogo = bank.getBankLogo();
        }
        return bankLogo;
    }

    @Override
    public void checkAndAddBank(String bankCode, String bankName) {
        Bank bank = this.findBy("bankCode", bankCode);
        if (Objects.isNull(bank)) {
            Bank bankEntity = new Bank();
            bankEntity.setBankCode(bankCode);
            bankEntity.setBankName(bankName);
            this.save(bankEntity);
        }
    }

}
