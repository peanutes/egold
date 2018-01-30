package com.zfhy.egold.domain.fund.service.impl;

import com.zfhy.egold.common.constant.IdTypeConstant;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.fund.dao.RechargeMapper;
import com.zfhy.egold.domain.fund.dto.RechargeStatus;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.service.RechargeService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.dto.PaymentDto;
import com.zfhy.egold.domain.order.dto.RechargeOrPay;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
@Transactional
@Slf4j
public class RechargeServiceImpl extends AbstractService<Recharge> implements RechargeService{
    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MyorderService myorderService;

    @Value("${pay.callback.url}")
    private String payCallbackUrl;


    @Override
    public String recharge(Integer memberId, Double rechargeAmount, String dealPwd, String terminalId) {
        this.memberService.checkDealPwd(memberId, dealPwd);

        

        Recharge recharge = new Recharge();
        recharge.setMemberId(memberId);
        recharge.setUpdateDate(new Date());
        recharge.setCreateDate(new Date());
        recharge.setDelFlag("0");
        String payNo = this.redisService.getIdByType(IdTypeConstant.RECHARGE_PAY_NO);
        recharge.setPayNo(payNo);
        recharge.setRechargeAmount(rechargeAmount);
        recharge.setRechargeFee(0D);
        recharge.setRechargeTime(new Date());
        recharge.setRechargeType("1");
        recharge.setStatus(RechargeStatus.CONFIRMED_PAY.getStatus());

        this.save(recharge);


        myorderService.addRecharge(recharge);

        PaymentDto paymentDto = PaymentDto.builder()
                .rechargeOrPay(RechargeOrPay.RECHARGE)
                .productName("充值")
                .paySn(payNo)
                .payAmount(rechargeAmount)
                .memberId(memberId)
                .build();

        String payRequestNo = this.paymentService.payRequest(paymentDto, terminalId, payCallbackUrl);

        recharge.setPayRequestNo(payRequestNo);

        this.update(recharge);


        return payRequestNo;

    }

    @Override
    public void updateStatusByPayNo(String paySn, RechargeStatus newStatus, RechargeStatus oldStatus) {
        this.rechargeMapper.updateStatusByPayNo(paySn, newStatus, oldStatus);
    }

    @Override
    public Recharge findByPayNo(String paySn) {

        return this.findBy("payNo", paySn);
    }

    @Override
    public void rechargeSuccess(Recharge recharge) {
        int count = this.rechargeMapper.rechargeSuccess(recharge.getId(), RechargeStatus.PAY_SUCCESS.getStatus());
        if (count != 1) {
            throw new BusException("充值记录状态不正确，有可能已回调或者已失败");
        }
    }

    @Override
    public String pcRecharge(Integer memberId, Double rechargeAmount, String bankChannelId) {

        

        Recharge recharge = new Recharge();
        recharge.setMemberId(memberId);
        recharge.setUpdateDate(new Date());
        recharge.setCreateDate(new Date());
        recharge.setDelFlag("0");
        String payNo = this.redisService.getIdByType(IdTypeConstant.RECHARGE_PAY_NO);
        recharge.setPayNo(payNo);
        recharge.setRechargeAmount(rechargeAmount);
        recharge.setRechargeFee(0D);
        recharge.setRechargeTime(new Date());
        recharge.setRechargeType("1");
        recharge.setStatus(RechargeStatus.CONFIRMED_PAY.getStatus());

        this.save(recharge);


        myorderService.addRecharge(recharge);

        PaymentDto paymentDto = PaymentDto.builder()
                .rechargeOrPay(RechargeOrPay.RECHARGE)
                .productName("PC网银充值")
                .paySn(payNo)
                .payAmount(rechargeAmount)
                .memberId(memberId)
                .build();


        return this.paymentService.pcRecharge(paymentDto, bankChannelId);

    }

    @Override
    public List<Recharge> list(Map<String, String> params) {
        return this.rechargeMapper.list(params);

    }
}
