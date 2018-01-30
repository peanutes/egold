package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dao.BankcardUnbindApplicationMapper;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.BankcardUnbindApplication;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.member.service.BankcardUnbindApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class BankcardUnbindApplicationServiceImpl extends AbstractService<BankcardUnbindApplication> implements BankcardUnbindApplicationService{
    @Autowired
    private BankcardUnbindApplicationMapper bankcardUnbindApplicationMapper;

    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private MemberService memberService;

    @Override
    public void applyUnbindBankCard(Integer memberId, String idcardFrontImg, String idcardBackImg) {


        Member member = this.memberService.findById(memberId);
        Idcard idcard = this.idcardService.findBy("memberId", memberId);
        Bankcard bankcard = this.bankcardService.findBy("memberId", memberId);
        if (Objects.isNull(member)) {
            throw new BusException("会员不存在");
        }

        if (Objects.isNull(idcard)) {
            throw new BusException("您还没有实名认证！");
        }

        if (Objects.isNull(bankcard)) {
            throw new BusException("您还没有绑定银行卡，不允许解绑！");
        }

        Condition condition = new Condition(BankcardUnbindApplication.class);
        condition.createCriteria().andEqualTo("delFlag", "0")
                .andEqualTo("memberId", memberId)
                .andEqualTo("status", 0);

        List<BankcardUnbindApplication> applications = super.findByCondition(condition);
        if (CollectionUtils.isNotEmpty(applications)) {
            throw new BusException("您之前提交的解绑申请正在审批中，请耐心等待");
        }

        BankcardUnbindApplication application = new BankcardUnbindApplication();
        application.setCreateDate(new Date());
        application.setDelFlag("0");
        application.setIdcardFrontImg(idcardFrontImg);
        application.setIdcardBackImg(idcardBackImg);
        application.setMemberId(memberId);
        application.setOriginBankCard(bankcard.getBankCard());
        application.setRealName(idcard.getRealName());
        application.setMobile(member.getMobilePhone());
        application.setStatus(0);

        super.save(application);
    }

    @Override
    public void approved(Integer id) {
        BankcardUnbindApplication application = this.findById(id);
        application.setStatus(1);
        this.update(application);

        
        this.memberService.unbindCard(application.getMemberId());

        
        this.bankcardService.unbindCard(application.getMemberId());
    }
}
