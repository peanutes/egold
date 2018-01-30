package com.zfhy.egold.domain.member.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.constant.SmsTplType;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.*;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.service.IncomeDetailService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.dao.MemberMapper;
import com.zfhy.egold.domain.member.dto.BankcardDto;
import com.zfhy.egold.domain.member.dto.MemberAccountDto;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.PcAccountOverView;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.*;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.redis.service.impl.RedisServiceImpl;
import com.zfhy.egold.domain.report.dto.CountDto;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.BankService;
import com.zfhy.egold.domain.sys.service.DictService;
//import com.zfhy.egold.gateway.idcardauth.IdCardAuthApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;



@Service
@Transactional
@Slf4j
public class MemberServiceImpl extends AbstractService<Member> implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private HuanxinUserService huanxinUserService;

    @Autowired
    private DictService dictService;

    @Autowired
    private InvestRecordService investRecordService;


    @Autowired
    private IncomeDetailService incomeDetailService;

    @Autowired
    private IdcardService idcardService;

//
//    @Autowired
//    private IdCardAuthApi idCardAuthApi;


    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private BankService bankService;


    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private AddressService addressService;

    @Value("${app.terminal}")
    private String appTerminal;


    @Override
    @Transactional(readOnly = true)
    public Member login(String userName, String password) {

        Condition condition = new Condition(Member.class);
        condition.or().andEqualTo("account", userName);
        condition.or().andEqualTo("mobilePhone", userName);
        condition.or().andEqualTo("email", userName);

        List<Member> members = super.findByCondition(condition);

        if (CollectionUtils.isEmpty(members)) {
            log.info("没有对应的用户:{}", userName);

            throw new BusException("用户名或者密码错误");
        }

        Member member = members.get(0);

        String sha1Pass = HashUtil.sha1(String.join("", password, member.getSalt()));

        if (!Objects.equals(sha1Pass, member.getPassword())) {
            log.info("密码不正确");
            throw new BusException("用户名或者密码错误");
        }

        if ("1".equalsIgnoreCase(member.getEnable())) {
            throw new BusException("你好，您的账号被禁用，请联系客服！");
        }


        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberByAccountName(String userName) {

        Condition condition = new Condition(Member.class);
        condition.or().andEqualTo("account", userName);
        condition.or().andEqualTo("mobilePhone", userName);
        condition.or().andEqualTo("email", userName);

        List<Member> members = super.findByCondition(condition);

        if (CollectionUtils.isEmpty(members)) {
            log.info("没有对应的用户:{}", userName);

            throw new BusException("用户名错误");
        }

        return members.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberByMemberId(Integer memberId) {

        Condition condition = new Condition(Member.class);
        condition.or().andEqualTo("id", memberId);

        List<Member> members = super.findByCondition(condition);

        if (CollectionUtils.isEmpty(members)) {
            log.info("没有对应的用户id:{}", memberId);

            throw new BusException("用户id错误");
        }

        return members.get(0);
    }

    @Override
    public Member register(String mobile, String password, String smsCode, String refereeMobile) {
        String code = this.redisService.getSmsCode(mobile, SmsTplType.REGISTER);

        if (!Objects.equals(smsCode, code)) {
            throw new BusException("验证码不正确，请重新输入");
        }


        Member condition = new Member();
        condition.setMobilePhone(mobile);


        Member entity = this.memberMapper.selectOne(condition);

        if (Objects.nonNull(entity)) {
            throw new BusException("该手机已注册，请直接登陆");
        }


        String salt = StrFunUtil.genRandom(6);
        String sha1Pass = HashUtil.sha1(String.join("", password, salt));

        Member member = new Member();
        member.setMobilePhone(mobile);
        member.setAccount("");
        member.setCreateDate(new Date());
        member.setEnable("0");
        member.setPassword(sha1Pass);
        member.setSalt(salt);
        
        member.setDealPwd(sha1Pass);
        member.setDelFlag("0");
        member.setUpdateDate(new Date());



        if (StringUtils.isNotBlank(refereeMobile)) {
            Member referee = this.findBy("mobilePhone", refereeMobile);
            if (Objects.nonNull(referee)) {
                member.setReferee(referee.getId());
            }
        }
        this.memberMapper.insert(member);


        
        this.huanxinUserService.register(member.getId(), mobile);


        
        this.memberFundService.iniMemberFunAccount(member.getId());


        return member;


    }

    @Override
    public void forgetPwd(String mobile, String password, String smsCode, String realName, String idCardNo) {
        Member condition = new Member();
        condition.setMobilePhone(mobile);

        Member member = this.memberMapper.selectOne(condition);

        if (Objects.isNull(member)) {
            throw new BusException("该手机号尚未注册");
        }
        String sessionSmsCode = this.redisService.getSmsCode(mobile, SmsTplType.UPDATE_PWD);

        if (!Objects.equals(smsCode, sessionSmsCode)) {
            throw new BusException("验证码不正确，请确认");
        }

        Idcard idcard = this.idcardService.findBy("memberId", member.getId());
        if (Objects.nonNull(idcard)) {
            if (StringUtils.isNotBlank(realName) && !Objects.equals(idcard.getRealName(), realName)) {
                throw new BusException("您好，您填写的真实姓名不正确");
            }

            if (StringUtils.isNotBlank(idCardNo) && !Objects.equals(idcard.getIdCard(), idCardNo)) {
                throw new BusException("您好，您填写的身份证不正确");
            }

        }

        String shaPass = HashUtil.sha1(String.join("", password, member.getSalt()));
        member.setPassword(shaPass);


        this.memberMapper.updateByPrimaryKey(member);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNewUser(Integer memberId) {

        
        Long duration = this.dictService.findLongByType(DictType.NEW_USER_DURATION);
        Member member = super.findById(memberId);
        Date registerDate = member.getCreateDate();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(registerDate.toInstant(), ZoneId.systemDefault());
        long hadRegisterDays = localDateTime.until(LocalDateTime.now(), ChronoUnit.DAYS);

        if (hadRegisterDays > duration) {
            return false;
        }

        
        boolean hadInvestNewUserProduct = this.investRecordService.hadInvestNewUserProduct(memberId);

        return !hadInvestNewUserProduct;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberAccountDto getAccountOverview(Integer memberId) {

        MemberAccountDto memberAccountDto = new MemberAccountDto();
        MemberFund memberFund = this.memberFundService.findBy("memberId", memberId);
        if (Objects.isNull(memberFund)) {
            return memberAccountDto;
        }
        memberAccountDto.setEnableBalance(DoubleUtil.changeDecimal(memberFund.getCashBalance(), 2));
        Double goldBalance = DoubleUtil.doubleAdd(memberFund.getTermGoldBalance(), memberFund.getCurrentGoldBalance(), 3);
        memberAccountDto.setGoldBalance(goldBalance);

        memberAccountDto.setInvestBalance(memberFund.getInvestBalance());


        Double realTimePrice = this.redisService.getRealTimePrice();
        
        Double goldAmount = DoubleUtil.doubleMul(realTimePrice, goldBalance);

        
        Double totalAssets = DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(goldAmount, memberFund.getCashBalance()), memberFund.getInvestBalance(), 2);
        memberAccountDto.setTotalAssets(totalAssets);

        memberAccountDto.setGoldBalanceAmount(goldAmount);


        
        Double yesterdayIncome = this.incomeDetailService.findYesterdayIncome(memberId);
        memberAccountDto.setYesterdayProfit(DoubleUtil.changeDecimal(yesterdayIncome == null ? 0D : yesterdayIncome, 2));


        
        Double totalProfit = this.incomeDetailService.findAllIncome(memberId);

        memberAccountDto.setTotalProfit(DoubleUtil.changeDecimal(totalProfit == null ? 0D : totalProfit, 2));


        memberAccountDto.setCurrentGoldBalance(memberFund.getCurrentGoldBalance());
        
        Double currentGoldBalanceFall = this.investRecordService.findCurrentGoldBalanceFall(memberId);

        memberAccountDto.setCurrentGoldBalanceFall(currentGoldBalanceFall == null ? 0D : currentGoldBalanceFall);
        memberAccountDto.setCurrentGoldBalanceRise(DoubleUtil.doubleSub(memberFund.getCurrentGoldBalance(), currentGoldBalanceFall, 3));

        memberAccountDto.setTermGoldBalance(memberFund.getTermGoldBalance());


        
        Member member = this.findById(memberId);
        if (Objects.nonNull(member)) {
            memberAccountDto.setBankcardBind(member.getBankcardBind());
        }

        return memberAccountDto;


    }

    @Override
    public void realNameAuth(Integer memberId, String realName, String idCardNo) {
        realName = realName.trim();
        idCardNo = idCardNo.trim();


        Idcard idcardEntity = this.idcardService.findBy("idCard", idCardNo);

        if (Objects.nonNull(idcardEntity)) {
            throw new BusException("身份证已注册过，请确认！");
        }

        Idcard idcardEntity2 = this.idcardService.findBy("memberId", memberId);
        if (Objects.nonNull(idcardEntity2)) {
            throw new BusException("您已经实名认证过！");
        }

        int idCardLen = idCardNo.length();
        if (idCardLen != 15 && idCardLen != 18) {
            throw new BusException("身份证位数不对，请确认！");
        }


        String gender = "1";
        String birthday = "";
        if (idCardLen == 15) {
            
            String genderNo = idCardNo.substring(14);
            if (Integer.parseInt(genderNo) % 2 == 0) {
                gender = "0";
            }
            birthday = idCardNo.substring(6, 12);
        }

        if (idCardLen == 18) {
            
            String genderNo = idCardNo.substring(16, 17);
            if (Integer.parseInt(genderNo) % 2 == 0) {
                gender = "0";
            }
            birthday = idCardNo.substring(6, 14);
        }

        String result = IdCardValidateUtil.chekIdCard(idCardNo);
        if (!Objects.equals("", result)) {
            throw new BusException("无效的身份证号码，请确认后再输入！");
        }


//        this.idCardAuthApi.verifyIdCard(realName, idCardNo, "");


        Member member = super.findById(memberId);
        

        Idcard idcard = new Idcard();
        idcard.setCreateDate(new Date());
        idcard.setDelFlag("0");
        idcard.setMemberId(memberId);
        idcard.setRealName(realName);
        idcard.setIdCard(idCardNo);
        idcard.setGender(gender);
        idcard.setBirthday(birthday);
        idcard.setTel(member.getMobilePhone());

        this.idcardService.save(idcard);

        member.setRealNameValid(1);
        member.setUpdateDate(new Date());
        member.setRealName(realName);

        super.update(member);

        
        if (Objects.equals(appTerminal, RedisServiceImpl.AppTerminal.web.name())) {
            
            MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
            if (null != memberOutPutDto) {
                Member member1 = this.getMemberByMemberId(memberId);
                memberOutPutDto.setRealNameValid(member1.getRealNameValid());
                memberOutPutDto.setRealName(member1.getRealName());
                
                RequestUtil.getHttpServletSession().setAttribute("memberOutPutDto", memberOutPutDto);
            }

        }
    }


    @Override
    public BankcardDto myBankcard(Integer memberId) {
        Bankcard bankcard = this.bankcardService.findBy("memberId", memberId);
        if (Objects.isNull(bankcard)) {
            return null;
        }

        BankcardDto bankcardDto = new BankcardDto().convertFrom(bankcard);

        String tip = this.dictService.findStringByType(DictType.MY_BANK_SECURE_TIP);
        bankcardDto.setSecureTip(tip);


        if (StringUtils.isBlank(bankcardDto.getBankLogoImg())) {

            String bankLogo = this.bankService.getBankLogo(bankcard.getBankCode());

            bankcardDto.setBankLogoImg(bankLogo);

        }


        return bankcardDto;

    }

    @Override
    public Boolean isRegister(String mobile) {
        Member member = super.findBy("mobilePhone", mobile);

        return Objects.nonNull(member);
    }

    @Override
    public void updatePwd(Integer memberId, String password, String originPassword) {
        Member member = this.findById(memberId);
        String sha1Pass = HashUtil.sha1(String.join("", originPassword, member.getSalt()));

        if (!Objects.equals(sha1Pass, member.getPassword())) {
            log.info("密码不正确");
            throw new BusException("原密码错误");
        }

        member.setPassword(HashUtil.sha1(String.join("", password, member.getSalt())));

        member.setUpdateDate(new Date());
        this.update(member);

    }

    @Override
    public void settingDealPwd(Integer memberId, String dealPwd) {
        Member member = this.findById(memberId);
        if (Objects.isNull(member)) {
            throw new BusException("该会员不存在");
        }
        member.setDealPwd(HashUtil.sha1(String.join("", dealPwd, member.getSalt())));
        member.setUpdateDate(new Date());
        this.update(member);

    }

    @Override
    public List<Member> findInvitation(Integer referee) {
        Condition condition = new Condition(Member.class);

        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("referee", referee);

        return this.findByCondition(condition);
    }

    @Override
    public void checkDealPwd(Integer id, String dealPwd) {
        Member member = this.findById(id);
        if (Objects.equals(member.getEnable(), "1")) {
            throw new BusException("您好，此会员账户已禁用，请联系客服");
        }
        if (StringUtils.isBlank(member.getDealPwd())) {
            throw new BusException(ResultCode.BUS_NO_DEAL_PWD, "您好，您还没有设置交易密码，请先设置！");
        }
        String sha1DealPwd = HashUtil.sha1(String.join("", dealPwd, member.getSalt()));
        if (!Objects.equals(sha1DealPwd, member.getDealPwd())) {
            throw new BusException("交易密码不正确");
        }

    }

    @Override
    public Member checkMember(Integer id) {
        Member member = this.findById(id);
        if (Objects.isNull(member)) {
            throw new BusException("您好，账号不存在，请联系客服");
        }
        if (Objects.equals(member.getEnable(), "1")) {
            throw new BusException("您好，此会员账户已禁用，请联系客服");
        }

        return member;
    }

    @Override
    public void deleteByMemberId(Integer id) {
        Idcard idcard = this.idcardService.findBy("memberId", id);
        if (Objects.nonNull(idcard)) {
            this.idcardService.deleteById(idcard.getId());
        }

        Bankcard bankcard = this.bankcardService.findBy("memberId", id);
        if (Objects.nonNull(bankcard)) {
            this.bankcardService.deleteById(bankcard.getId());
        }

        this.addressService.deleteByMemberId(id);
        this.deleteById(id);
    }

    @Override
    public void bindCardSuccess(Integer id) {
        Member member = this.findById(id);
        member.setBankcardBind(1);
        member.setUpdateDate(new Date());
        this.update(member);


    }

    @Override
    public void unbindCard(Integer memberId) {
        this.memberMapper.unbindCard(memberId);
    }

    @Override
    public void validateDealPwd(Integer id, String dealPwd) {

        Member member = this.findById(id);
        String pwd = HashUtil.sha1(String.join("", dealPwd, member.getSalt()));

        if (!Objects.equals(pwd, member.getDealPwd())) {
            throw new BusException("您好，交易密码不正确，请重新输入");
        }
    }

    @Override
    public void changeMobile(Integer id, String newMobile) {
        Member member = this.findById(id);

        member.setMobilePhone(newMobile);
        member.setUpdateDate(new Date());
        this.update(member);
    }

    @Override
    public Member findByMobile(String mobile) {
        return this.findBy("mobilePhone", mobile);
    }


    public MemberOutPutDto getMemberOutPutDto(SysParameterWithoutToken sysParameterWithoutToken, Member member, String ip) {

        
        String token = this.redisService.createAndSetMemberToken(member);

        
        this.loginLogService.log(member, sysParameterWithoutToken.getTerminalType(), sysParameterWithoutToken.getTerminalId(), ip);

        MemberOutPutDto memberOutPutDto = new MemberOutPutDto().convertFrom(member);
        memberOutPutDto.setToken(token);

        ThreadUtil.getThreadPollProxy().execute(()->this.updateTerminal(member, sysParameterWithoutToken));


        
        this.huanxinUserService.getOrRegister(memberOutPutDto, member.getId());

        
        boolean isNewUser = this.isNewUser(member.getId());
        memberOutPutDto.setNewUser(isNewUser);

        MemberAccountDto memberAccountDto = this.getAccountOverview(member.getId());
        memberOutPutDto.setMemberAccountDto(memberAccountDto);

        if (Objects.equals(memberOutPutDto.getBankcardBind(), 1)) {
            
            memberOutPutDto.setBankCard(this.bankcardService.findBankCardByMemberId(member.getId()));
            memberOutPutDto.setBankCardObject(this.bankcardService.findBankCardObjectByMemberId(member.getId()));
        }

        
        if (Objects.equals(appTerminal, RedisServiceImpl.AppTerminal.web.name())) {
            
            HttpSession session = RequestUtil.getHttpServletSession();
            session.setAttribute("memberOutPutDto", memberOutPutDto);
        }

        return memberOutPutDto;
    }

    private void updateTerminal(Member member, SysParameterWithoutToken sysParameterWithoutToken) {
        member.setTerminalType(sysParameterWithoutToken.getTerminalType());
        member.setTerminalId(sysParameterWithoutToken.getTerminalId());
        member.setUpdateDate(new Date());

        this.update(member);
    }

    @Override
    public List<PcAccountOverView> getPcAccountOverview(Integer memberId) {
        MemberFund memberFund = this.memberFundService.findBy("memberId", memberId);
        if (Objects.isNull(memberFund)) {
            return Lists.newArrayList();
        }
        Double goldBalance = DoubleUtil.doubleAdd(memberFund.getTermGoldBalance(), memberFund.getCurrentGoldBalance(), 3);


        Double realTimePrice = this.redisService.getRealTimePrice();
        
        Double goldAmount = DoubleUtil.doubleMul(realTimePrice, goldBalance);
        
        Double investBalance = memberFund.getInvestBalance();
        
        Double cashBalance = memberFund.getCashBalance();


        Double totalAmount = DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(cashBalance, investBalance), goldAmount);


        if (DoubleUtil.equal(totalAmount, 0D)) {
            PcAccountOverView cash = PcAccountOverView.builder()
                    .name("现金余额 0.00元")
                    .value(33.33)
                    .build();

            PcAccountOverView gold = PcAccountOverView.builder()
                    .name("黄金资产 0.00元")
                    .value(33.33)
                    .build();

            PcAccountOverView invest = PcAccountOverView.builder()
                    .name("投资资产 0.00元")
                    .value(33.33)
                    .build();

            return Arrays.asList(cash, gold, invest);
        }


        PcAccountOverView cashAccount = PcAccountOverView.builder()
                .name(String.format("现金余额 %s元", DoubleUtil.toString(cashBalance)))
                .value(DoubleUtil.doubleDiv(cashBalance, totalAmount, 4) * 100)
                .build();


        PcAccountOverView goldAccount = PcAccountOverView.builder()
                .name(String.format("黄金资产 %s元", DoubleUtil.toString(goldAmount)))
                .value(DoubleUtil.doubleDiv(goldAmount, totalAmount, 4) * 100)
                .build();

        PcAccountOverView investAccount = PcAccountOverView.builder()
                .name(String.format("理财账户 %s元", DoubleUtil.toString(investBalance)))
                .value(DoubleUtil.doubleDiv(investBalance, totalAmount, 4) * 100)
                .build();


        return Arrays.asList(cashAccount, goldAccount, investAccount);

    }

    @Override
    public void bindWechat(Integer memberId) {
        this.memberMapper.bindWechat(memberId);
    }

    @Override
    public List<CountDto> statisticRegister(Map<String, String> params) {

        return memberMapper.statisticRegister(params);
    }

    @Override
    public void modifyDealPwd(Integer memberId, String oldPwd, String dealPwd) {
        Member member = this.findById(memberId);

        String shaPwd = HashUtil.sha1(String.join("", oldPwd, member.getSalt()));

        if (!Objects.equals(member.getDealPwd(), shaPwd)) {
            throw new BusException("您好,原密码错误");
        }

        member.setDealPwd(HashUtil.sha1(String.join("", dealPwd, member.getSalt())));
        this.update(member);

    }

    @Override
    public void forgetDealPwd(String mobile, String password, String smsCode) {
        Member condition = new Member();
        condition.setMobilePhone(mobile);

        Member member = this.memberMapper.selectOne(condition);

        if (Objects.isNull(member)) {
            throw new BusException("该手机号尚未注册");
        }
        String sessionSmsCode = this.redisService.getSmsCode(mobile, SmsTplType.UPDATE_PWD);

        if (!Objects.equals(smsCode, sessionSmsCode)) {
            throw new BusException("验证码不正确，请确认");
        }


        String shaPass = HashUtil.sha1(String.join("", password, member.getSalt()));
        member.setDealPwd(shaPass);


        this.memberMapper.updateByPrimaryKey(member);
    }

    public static void main(String[] args) {
        System.out.println(ZoneId.systemDefault());
    }
}
