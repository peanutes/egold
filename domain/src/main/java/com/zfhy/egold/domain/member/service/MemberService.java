package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.domain.member.dto.BankcardDto;
import com.zfhy.egold.domain.member.dto.MemberAccountDto;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.PcAccountOverView;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.report.dto.CountDto;

import java.util.List;
import java.util.Map;



public interface MemberService  extends Service<Member> {

    Member login(String userName, String password);

    Member getMemberByAccountName(String userName);

    Member getMemberByMemberId(Integer memberId);

    Member register(String mobile, String password, String smsCode, String refereeMobile);

    void forgetPwd(String mobile, String password, String smsCode, String realName, String idCardNo);

    boolean isNewUser(Integer id);

    MemberAccountDto getAccountOverview(Integer memberId);

    void realNameAuth(Integer memberId, String realName, String idCard);

    BankcardDto myBankcard(Integer memberId);

    Boolean isRegister(String mobile);

    void updatePwd(Integer memberId, String password, String originPassword);

    void settingDealPwd(Integer memberId, String dealPwd);

    List<Member> findInvitation(Integer referee);

    void checkDealPwd(Integer id, String dealPwd);

    Member checkMember(Integer id);

    void deleteByMemberId(Integer id);

    void bindCardSuccess(Integer id);

    void unbindCard(Integer memberId);

    void validateDealPwd(Integer id, String dealPwd);

    void changeMobile(Integer id, String newMobile);

    Member findByMobile(String mobile);

    public MemberOutPutDto getMemberOutPutDto(SysParameterWithoutToken sysParameterWithoutToken, Member member, String ip);

    List<PcAccountOverView> getPcAccountOverview(Integer memberId);

    void bindWechat(Integer memberId);

    List<CountDto> statisticRegister(Map<String, String> params);

    void modifyDealPwd(Integer id, String oldPwd, String dealPwd);

    void forgetDealPwd(String mobile, String password, String smsCode);
}
