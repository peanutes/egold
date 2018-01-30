package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.domain.fund.dao.WithdrawMapper;
import com.zfhy.egold.domain.fund.dto.WithdrawStatus;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.fund.service.WithdrawService;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class WithdrawServiceImpl extends AbstractService<Withdraw> implements WithdrawService{
    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private DictService dictService;

    @Autowired
    private FundRecordService fundRecordService;

    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private MyorderService myorderService;





    @Override
    public Withdraw confirmWithdraw(Integer memberId, Double withdrawAmount, String dealPwd) {

        
        
        Member member = this.memberService.findById(memberId);
        if (Objects.equals(member.getEnable(), "1")) {
            throw new BusException("您好，此会员账户已禁用，请联系客服");
        }

        if (!Objects.equals(member.getBankcardBind(), 1)) {
            throw new BusException("您好，您尚未绑定银行卡，请先绑卡");
        }

        
        String shaDealPwd = HashUtil.sha1(String.join("", dealPwd, member.getSalt()));
        if (!Objects.equals(shaDealPwd, member.getDealPwd())) {
            throw new BusException("交易密码错误");
        }



        
        MemberFund memberFund = this.memberFundService.findBy("memberId", memberId);
        if (memberFund.getCashBalance() < withdrawAmount) {
            throw new BusException("您好，您的余额不足");
        }

        Bankcard bankcard = this.bankcardService.findBy("memberId", memberId);
        if (Objects.isNull(bankcard)) {
            throw new BusException("您好，您尚未绑定银行卡，请先绑卡");
        }

        Double withdrawFee = this.dictService.findDouble(DictType.WITHDRAW_FEE);
        Integer freeTimes = this.dictService.findIntByType(DictType.WITHDRAW_FREE_TIME);

        int hadWithdrawTimes = countByMemberId(memberId);


        
        Double fee = 0d;
        if (hadWithdrawTimes >= freeTimes) {
            fee = withdrawFee;
            if (withdrawAmount <= fee) {
                throw new BusException("提现金额太小，不足以支付手续费");
            }

        }


        
        Withdraw withdraw = new Withdraw();
        withdraw.setUpdateDate(new Date());
        withdraw.setMemberId(memberId);
        withdraw.setStatus(WithdrawStatus.CONFIRMED.getCode());
        withdraw.setApplyTime(new Date());
        withdraw.setCreateDate(new Date());
        withdraw.setDelFlag("0");
        withdraw.setWithdrawAmount(withdrawAmount);
        withdraw.setPayAmount(DoubleUtil.doubleSub(withdrawAmount, fee, 2));
        withdraw.setWithdrawFee(fee);
        withdraw.setWithdrawAccount(bankcard.getBankCard());
        withdraw.setMobile(member.getMobilePhone());
        withdraw.setRealName(member.getRealName());
        withdraw.setBankName(bankcard.getBankName());
        withdraw.setBankCardId(bankcard.getId());
        withdraw.setBankNo(bankcard.getBankCode());

        this.save(withdraw);


        
        this.myorderService.addWithdraw(withdraw);



        
        this.memberFundService.subCashBalance(memberId, memberFund.getVersion(), withdraw.getWithdrawAmount());

        
        this.fundRecordService.withdraw(memberId, withdraw.getWithdrawAmount(), withdraw.getId());



        return withdraw;


    }

    @Override
    public void updateAndReleaseAmount(Withdraw withdraw) {
        if (Objects.equals(withdraw.getStatus(), WithdrawStatus.PAYED.getCode())) {
            this.update(withdraw);
        } else if (Objects.equals(withdraw.getStatus(), WithdrawStatus.DENY.getCode())){

            Withdraw withdrawEntity = this.findById(withdraw.getId());
            if (Objects.isNull(withdrawEntity)) {
                throw new BusException("提现记录不存在");
            }

            if (!Objects.equals(withdrawEntity.getStatus(), WithdrawStatus.CONFIRMED.getCode())) {
                throw new BusException("状态不正确");
            }

            this.memberFundService.fallBackWithdraw(withdrawEntity);

            this.update(withdraw);
        }

    }

    @Override
    public Double findOnWithdraw(Integer memberId) {

        return this.withdrawMapper.findOnWithdraw(memberId);

    }

    private int countByMemberId(Integer memberId) {

        LocalDate now = LocalDate.now();

        Condition condition = new Condition(Withdraw.class);
        condition.createCriteria()
                .andEqualTo("memberId", memberId)
                .andEqualTo("delFlag", "0")

                .andBetween("applyTime", LocalDate.of(now.getYear(), now.getMonth(), 1), LocalDateTime.now());
        return this.withdrawMapper.selectCountByCondition(condition);

    }
}
