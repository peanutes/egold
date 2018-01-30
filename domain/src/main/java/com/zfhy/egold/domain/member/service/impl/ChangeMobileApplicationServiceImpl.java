package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dao.ChangeMobileApplicationMapper;
import com.zfhy.egold.domain.member.dto.ChangeMobileStatus;
import com.zfhy.egold.domain.member.entity.ChangeMobileApplication;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.ChangeMobileApplicationService;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class ChangeMobileApplicationServiceImpl extends AbstractService<ChangeMobileApplication> implements ChangeMobileApplicationService{
    @Autowired
    private ChangeMobileApplicationMapper changeMobileApplicationMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private RedisService redisService;

    @Override
    public void applyChangeMobile(Integer memberId, String oleMobile, String newMobile, String smsCode, String idcardHandImg, String idcardFrontImg, String idcardBackImg, String bankCardImg) {

        
        String sessionSmsCode = this.redisService.getSmsCode(newMobile, SmsTplType.CHANGE_MOBILE);

        if (!Objects.equals(sessionSmsCode, smsCode)) {
            throw new BusException("您好，验证码错误，请确认后再输入");
        }


        ChangeMobileApplication changeMobileApplication = new ChangeMobileApplication();
        changeMobileApplication.setStatus(ChangeMobileStatus.WAIT_AUDIT.getCode());
        changeMobileApplication.setCreateDate(new Date());
        changeMobileApplication.setDelFlag("0");
        changeMobileApplication.setUpdateDate(new Date());
        changeMobileApplication.setBankCardImg(bankCardImg);
        changeMobileApplication.setIdcardBackImg(idcardBackImg);
        changeMobileApplication.setIdcardFrontImg(idcardFrontImg);
        changeMobileApplication.setIdcardHandImg(idcardHandImg);
        changeMobileApplication.setNewMobile(newMobile);
        changeMobileApplication.setOldMobile(oleMobile);
        changeMobileApplication.setMemberId(memberId);

        this.save(changeMobileApplication);
    }

    @Override
    public void applyChangeMobileWithSms(Integer memberId, String idCardNo, String realName, String newMobile, String smsCode) {
        Member member = this.memberService.findById(memberId);

        if (!Objects.equals(realName, member.getRealName())) {
            throw new BusException("您好，真实姓名不正确，请重新输入");
        }

        Idcard idcard = idcardService.findByMemberId(memberId);

        if (Objects.isNull(idcard)) {
            throw new BusException("您好，您还没有进行实名认证");
        }


        if (!Objects.equals(idCardNo, idcard.getIdCard())) {
            throw new BusException("您好，身份证号填写错误，请重新输入");
        }


        
        String sessionSmsCode = this.redisService.getSmsCode(newMobile, SmsTplType.CHANGE_MOBILE);

        if (!Objects.equals(sessionSmsCode, smsCode)) {
            throw new BusException("您好，验证码错误，请确认后再输入");
        }


        this.memberService.changeMobile(memberId, newMobile);



    }
}
