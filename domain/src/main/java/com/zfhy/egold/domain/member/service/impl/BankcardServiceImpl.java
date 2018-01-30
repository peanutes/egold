package com.zfhy.egold.domain.member.service.impl;

import com.google.common.collect.ImmutableMap;
import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.constant.IdTypeConstant;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.member.dao.BankcardMapper;
import com.zfhy.egold.domain.member.dto.BankDto;
import com.zfhy.egold.domain.member.dto.BankcardListDto;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.BindBankcardLog;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.BindBankcardLogService;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.redis.service.impl.RedisServiceImpl;
import com.zfhy.egold.domain.report.dto.CountDto;
import com.zfhy.egold.domain.sys.entity.Bank;
import com.zfhy.egold.domain.sys.service.BankService;
import com.zfhy.egold.domain.sys.service.DictService;
import com.zfhy.egold.gateway.payment.yibao.YibaoPaymentApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;



@Slf4j
@Service
@Transactional
public class BankcardServiceImpl extends AbstractService<Bankcard> implements BankcardService {

    @Value("${app.env}")
    private String appEnv;

    @Autowired
    private BankcardMapper bankcardMapper;

    @Autowired
    private YibaoPaymentApi yibaoPaymentApi;

    @Autowired
    private BankService bankService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DictService dictService;


    @Autowired
    private IdcardService idcardService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BindBankcardLogService bindBankcardLogService;
    @Value("${app.terminal}")
    private String appTerminal;

    @Override

    public List<BankcardListDto> list(Map<String, String> params) {
        return this.bankcardMapper.list(params);

    }

    @Cacheable("BankcardServiceImpl_findBankCardByMemberId")
    @CacheDuration(duration = 60)
    @Override
    public String findBankCardByMemberId(Integer memberId) {
        Bankcard bankcard = this.findBy("memberId", memberId);
        if (Objects.nonNull(bankcard)) {
            return bankcard.getBankCard();
        }
        return "";
    }

    @Cacheable("BankcardServiceImpl_findBankCardNameByMemberId")
    @CacheDuration(duration = 60)
    @Override
    public String findBankCardNameByMemberId(Integer memberId) {
        Bankcard bankcard = this.findBy("memberId", memberId);
        if (Objects.nonNull(bankcard)) {
            return bankcard.getBankName();
        }
        return "";
    }

    @Cacheable("BankcardServiceImpl_findBankCardObjectByMemberId")
    @CacheDuration(duration = 60)
    @Override
    public Bankcard findBankCardObjectByMemberId(Integer memberId) {
        return this.findBy("memberId", memberId);
    }

    @Override
    public BankDto bankCardCheck(String bankCardNo) {
        Map<String, String> result = this.yibaoPaymentApi.bankCardCheck(bankCardNo);


        String bankLogo = this.bankService.getBankLogo(StringUtils.trimToEmpty(result.get("bankcode")));

        this.bankService.checkAndAddBank(result.get("bankcode"), result.get("bankname"));
        return BankDto.builder()
                .bankCode(result.get("bankcode"))
                .bankName(result.get("bankname"))
                .bankLogo(bankLogo)
                .build();

    }

    @Override
    public String bindCardRequest(Integer memberId, String bankCardNo, String mobile) {


        Member member = this.memberService.findById(memberId);


        Idcard idCard = idcardService.findBy("memberId", memberId);


        if (Objects.isNull(idCard)) {
            throw new BusException(ResultCode.BUS_NO_REAL_NAME, "你好，你尚未实名认证");
        }

        
        Bankcard bankcard = this.findBy("memberId", memberId);
        if (Objects.nonNull(bankcard)) {
            throw new BusException("您好，你已绑定过银行卡，需要解绑后才能重新绑定");
        }


        Bankcard existsBankCard = this.findBy("bankCard", bankCardNo);
        if (Objects.nonNull(existsBankCard)) {
            throw new BusException("您好，该卡号已有其他人绑定，请您确认再重新输入");
        }


        
        String requestNo = this.redisService.getIdByType(IdTypeConstant.BIND_BANK_CARD);
        Map<String, String> parameterMap = ImmutableMap.<String, String>builder()
                .put("requestno", requestNo)
                .put("identityid", String.format("%s_%06d", appEnv, member.getId()))
                .put("identitytype", "USER_ID")
                .put("cardno", bankCardNo)
                .put("idcardno", idCard.getIdCard())
                .put("idcardtype", "ID")
                .put("username", member.getRealName())
                .put("phone", mobile)
                .put("advicesmstype", "MESSAGE")
                .put("customerenhancedtype", "AUTH_REMIT")
                .put("avaliabletime", "60")
                .put("callbackurl", "")
                .put("requesttime", DateUtil.toString(new Date()))
                .build();
        this.yibaoPaymentApi.bindCardRequest(parameterMap);


        this.bindBankcardLogService.addLog(memberId, bankCardNo, mobile, requestNo, member.getRealName(), idCard.getIdCard());
        return requestNo;

    }

    @Override
    public void bindCardConfirm(Integer memberId, String bindCardRequestNo, String smsCode) {

        Idcard idcard = this.idcardService.findBy("memberId", memberId);

        if (Objects.isNull(idcard)) {
            throw new BusException(ResultCode.BUS_NO_REAL_NAME, "您还没有实名认证，请先进行实名认证");
        }


        
        Bankcard bankcard = this.findBy("memberId", memberId);
        if (Objects.nonNull(bankcard)) {
            throw new BusException("您好，你已绑定过银行卡，需要解绑后才能重新绑定");
        }

        BindBankcardLog bindBankcardLog = this.bindBankcardLogService.findBy("requestNo", bindCardRequestNo);
        if (Objects.isNull(bindBankcardLog)) {
            throw new BusException("绑卡请求编号不存在");
        }

        Bankcard existsBankCard = this.findBy("bankCard", bindBankcardLog.getCardNo());
        if (Objects.nonNull(existsBankCard)) {
            throw new BusException("您好，该卡号已有其他人绑定，请您确认再重新输入");
        }


        Map<String, String> parameterMap = ImmutableMap.<String, String>builder()
                .put("requestno", bindCardRequestNo)
                .put("validatecode", smsCode)
                .build();

        Map<String, String> result = this.yibaoPaymentApi.bindCardConfirm(parameterMap);

        bindBankcardLog.setStatus(1);
        this.bindBankcardLogService.update(bindBankcardLog);


        Bank bank = this.bankService.findBy("bankCode", result.get("bankcode"));


        Bankcard bankcardEntity = new Bankcard();
        bankcardEntity.setMemberId(memberId);
        bankcardEntity.setDelFlag("0");
        bankcardEntity.setBankCard(bindBankcardLog.getCardNo());
        bankcardEntity.setCreateDate(new Date());
        bankcardEntity.setBankAccount(bindBankcardLog.getRealName());
        bankcardEntity.setBankCode(result.get("bankcode"));
        bankcardEntity.setBindMobile(bindBankcardLog.getMobile());

        if (Objects.nonNull(bank)) {
            bankcardEntity.setBankName(bank.getBankName());
            bankcardEntity.setBankLogo(bank.getBankLogo());
        }
        this.save(bankcardEntity);

        this.memberService.bindCardSuccess(memberId);

        
        if (Objects.equals(appTerminal, RedisServiceImpl.AppTerminal.web.name())) {
            MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
            memberOutPutDto.setBankcardBind(1);
            memberOutPutDto.setBankCardObject(bankcardEntity);
            memberOutPutDto.setBankCard(bankcardEntity.getBankCard());
        }
    }

    @Override
    public void unbindCard(Integer memberId) {
        this.bankcardMapper.deleteByMemberId(memberId);

    }

    @Override
    public List<CountDto> statisticBindCard(Map<String, String> params) {

        return bankcardMapper.statisticBindCard(params);
    }
}
