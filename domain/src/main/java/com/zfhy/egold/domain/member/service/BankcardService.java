package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.member.dto.BankDto;
import com.zfhy.egold.domain.member.dto.BankcardListDto;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.report.dto.CountDto;

import java.util.List;
import java.util.Map;



public interface BankcardService  extends Service<Bankcard> {

    List<BankcardListDto> list(Map<String, String> params);

    String findBankCardByMemberId(Integer memberId);

    String findBankCardNameByMemberId(Integer memberId);

    Bankcard findBankCardObjectByMemberId(Integer memberId);

    BankDto bankCardCheck(String bankCardNo);

    String bindCardRequest(Integer memberId, String bankCardNo, String mobile);

    void bindCardConfirm(Integer memberId, String bindCardRequestNo, String smsCode);

    void unbindCard(Integer memberId);

    List<CountDto> statisticBindCard(Map<String, String> params);
}
