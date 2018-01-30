package com.zfhy.egold.wap.web.fund;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.dto.*;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.fund.service.RechargeService;
import com.zfhy.egold.domain.fund.service.WithdrawService;
import com.zfhy.egold.domain.invest.dto.IncomeDailyDto;
import com.zfhy.egold.domain.invest.dto.IncomeDto;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.IncomeDetailService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/fund/")
@Api(value = "FundController",tags = "FundController", description = "资金")
@Slf4j
@Validated
public class FundController {
    @Resource
    private FundRecordService fundRecordService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IncomeDetailService incomeDetailService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberFundService memberFundService;


    @Autowired
    private InvestRecordService investRecordService;

    
    @PostMapping("/confirmWithdraw")
    public Result<WithdrawConfirmDto> confirmWithdraw(
            @ApiParam(name = "withdrawAmount", value = "提现金额", required = true)
            @RequestParam @NotNull(message = "提现金额不能为空") @DecimalMin(value = "0.01", message = "提现金额需要大于1分钱")
            Double withdrawAmount,
            @ApiParam(name = "dealPwd", value = "交易密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "交易密码不能为空") @Length(min = 6, max = 16, message = "交易密码不能小于6位数")
            String dealPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        Withdraw withdraw = this.withdrawService.confirmWithdraw(member.getId(), withdrawAmount, dealPwd);

        WithdrawConfirmDto dto = WithdrawConfirmDto.builder()
                .fee(String.format("%s元", DoubleUtil.toString(withdraw.getWithdrawFee())))
                .withdrawAmount(String.format("%s元", DoubleUtil.toString(withdraw.getWithdrawAmount())))
                .payAmount(String.format("%s元", DoubleUtil.toString(withdraw.getPayAmount())))
                .receiveTime(DateUtil.toString(new Date(), DateUtil.YYYY_MM_DD))
                .build();


        return ResultGenerator.genSuccessResult(dto);
    }


    
    @PostMapping("/incomeDetail")
    public Result<IncomeDto> incomeDetail(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        PageHelper.startPage(page, size);
        PageHelper.orderBy("income_date desc");
        List<IncomeDailyDto> incomeDailyDtos = this.incomeDetailService.dailyIncome(member.getId());
        PageInfo<IncomeDailyDto> incomeDailyDtoPageInfo = new PageInfo<>(incomeDailyDtos);

        Page<IncomeDailyDto> incomeDailyDtoPage = new Page<>();
        BeanUtils.copyProperties(incomeDailyDtoPageInfo, incomeDailyDtoPage);
        incomeDailyDtoPage.setList(incomeDailyDtos);

        Double totalSum = incomeDetailService.sumByMemberId(member.getId());

        IncomeDto incomeDto = IncomeDto.builder()
                .incomeDailyDtoPage(incomeDailyDtoPage)
                .totalSum(totalSum)
                .build();


        return ResultGenerator.genSuccessResult(incomeDto);
    }


    
    @PostMapping("/rechargeDetail")
    public Result<Page<RechargeDetailDto>> rechargeDetail(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        PageHelper.startPage(page, size);
        List<FundRecord> fundRecords = this.fundRecordService.findByOperateType(member.getId(), FundOperateType.RECHARGE);


        PageInfo<FundRecord> fundRecordPageInfo = new PageInfo<>(fundRecords);

        Page<RechargeDetailDto> rechargeDetailDtoPage = new Page<>();
        BeanUtils.copyProperties(fundRecordPageInfo, rechargeDetailDtoPage);

        List<RechargeDetailDto> rechargeDetailDtos = fundRecords.stream().map(RechargeDetailDto::convertFrom).collect(Collectors.toList());
        rechargeDetailDtoPage.setList(rechargeDetailDtos);


        return ResultGenerator.genSuccessResult(rechargeDetailDtoPage);
    }


    
    @PostMapping("/withdrawDetail")
    public Result<Page<RechargeDetailDto>> withdrawDetail(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        PageHelper.startPage(page, size);
        List<FundRecord> fundRecords = this.fundRecordService.findByOperateType(member.getId(), FundOperateType.WITHDRAW);


        PageInfo<FundRecord> fundRecordPageInfo = new PageInfo<>(fundRecords);

        Page<RechargeDetailDto> rechargeDetailDtoPage = new Page<>();
        BeanUtils.copyProperties(fundRecordPageInfo, rechargeDetailDtoPage);

        List<RechargeDetailDto> rechargeDetailDtos = fundRecords.stream().map(RechargeDetailDto::convertFrom).collect(Collectors.toList());
        rechargeDetailDtoPage.setList(rechargeDetailDtos);


        return ResultGenerator.genSuccessResult(rechargeDetailDtoPage);
    }


    
    @PostMapping("/currentDepositRecord")
    public Result<Page<GoldTradeRecordDto>> currentDepositRecord(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {

        return goldTradeRecord(page, size, sysParameter.getToken(), FundAccount.CURRENT);
    }


    
    @PostMapping("/termDepositRecord")
    public Result<Page<TermGoldTradeRecordDto>> termDepositRecord(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {
        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        PageHelper.startPage(page, size);

        List<InvestRecord> investRecords = this.investRecordService.findTermGold(member.getId());


        PageInfo<InvestRecord> investRecordPageInfo = new PageInfo<>(investRecords);

        Page<TermGoldTradeRecordDto> goldTradeRecordDtoPage = new Page<>();
        BeanUtils.copyProperties(investRecordPageInfo, goldTradeRecordDtoPage);

        List<TermGoldTradeRecordDto> goldTradeRecordDtoList = investRecords.stream().map(e -> TermGoldTradeRecordDto.convertFrom(e)).collect(Collectors.toList());
        goldTradeRecordDtoPage.setList(goldTradeRecordDtoList);


        return ResultGenerator.genSuccessResult(goldTradeRecordDtoPage);
    }

    private Result<Page<GoldTradeRecordDto>> goldTradeRecord(Integer page, Integer size,  String token, FundAccount fundAccount) {
        MemberSession member = this.redisService.checkAndGetMemberToken(token);

        Double realTimePrice = this.redisService.getRealTimePrice();


        PageHelper.startPage(page, size);
        List<FundRecord> fundRecords = this.fundRecordService.findByAccount(member.getId(), fundAccount);


        PageInfo<FundRecord> fundRecordPageInfo = new PageInfo<>(fundRecords);

        Page<GoldTradeRecordDto> goldTradeRecordDtoPage = new Page<>();
        BeanUtils.copyProperties(fundRecordPageInfo, goldTradeRecordDtoPage);

        List<GoldTradeRecordDto> goldTradeRecordDtoList = fundRecords.stream().map(e -> GoldTradeRecordDto.convertFrom(e, realTimePrice)).collect(Collectors.toList());
        goldTradeRecordDtoPage.setList(goldTradeRecordDtoList);


        return ResultGenerator.genSuccessResult(goldTradeRecordDtoPage);
    }


    
    @PostMapping("/recharge")
    public Result<String> recharge(
            @ApiParam(name = "rechargeAmount", value = "充值金额", required = true)
            @RequestParam @NotNull(message = "充值金额不能为空") @DecimalMin(value = "0.0099", message = "充值金额需要大于等于0.01")
            Double rechargeAmount,
            @ApiParam(name = "dealPwd", value = "交易密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "交易密码不能为空") @Length(min = 6, max = 16, message = "交易密码不能小于6位数")
            String dealPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        String requestNo = this.rechargeService.recharge(member.getId(), rechargeAmount, dealPwd, sysParameter.getTerminalId());

        return ResultGenerator.genSuccessResult(requestNo);
    }


    
    @PostMapping("/validateRechargeSmsCode")
    public Result<String> validateRechargeSmsCode(
            @ApiParam(name = "rechargeRequestNo", value = "充值请求号", required = true)
            @NotBlank(message = "充值请求号不能为空") @RequestParam String rechargeRequestNo,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true)
            @NotNull(message = "短信验证码不能为空") @RequestParam String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        this.paymentService.validatePaySmsCode(member.getId(), rechargeRequestNo, smsCode);

        return ResultGenerator.genSuccessResult();
    }


    
    @PostMapping("/goldAssets")
    public Result<GoldAssetsDto> goldAssets(@ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        GoldAssetsDto goldAssetsDto = this.memberFundService.goldAssets(member.getId());

        return ResultGenerator.genSuccessResult(goldAssetsDto);
    }


    
    @PostMapping("/myInvestSummary")
    public Result<MyInvestSummaryDto> myInvestSummary(@ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        MyInvestSummaryDto myInvestDto = this.investRecordService.myInvestSummary(member.getId());

        return ResultGenerator.genSuccessResult(myInvestDto);
    }

    
    @PostMapping("/myInvest")
    public Result<Page<MyInvestDto>> myInvest(
            @ApiParam(name = "investStatus", value = "状态(ON_INVEST=在投，FINISH=已到期)", required = true)
            @RequestParam @NotNull(message = "状态不允许为空") InvestStatus investStatus,
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        PageHelper.startPage(page, size);
        List<MyInvestDto> investRecords = this.investRecordService.myInvest(member.getId(), investStatus);


        PageInfo<MyInvestDto> myInvestDtoPageInfo = new PageInfo<>(investRecords);

        Page<MyInvestDto> myInvestDtoPage = new Page<>();
        BeanUtils.copyProperties(myInvestDtoPageInfo, myInvestDtoPage);

        myInvestDtoPage.setList(investRecords);


        return ResultGenerator.genSuccessResult(myInvestDtoPage);
    }


    
    @PostMapping("/fundRecords")
    public Result<Page<AllTypeFundRecordDto>> fundRecords(
            @ApiParam(name = "fundAccount", value = "账户,为空的话查询所有(CASH=现金，CURRENT=活期金，TERM=定期金，INVEST=投资账户)", required = false)
            @RequestParam(required = false) FundAccount fundAccount,
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {


        if (fundAccount == null) {
            fundAccount = FundAccount.CASH;
        }
        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        PageHelper.startPage(page, size);
        List<FundRecord> fundRecords = this.fundRecordService.findByAccount(member.getId(), fundAccount);


        PageInfo<FundRecord> investRecordPageInfo = new PageInfo<>(fundRecords);

        Page<AllTypeFundRecordDto> allTypeFundRecordDtoPage = new Page<>();
        BeanUtils.copyProperties(investRecordPageInfo, allTypeFundRecordDtoPage);
        List<AllTypeFundRecordDto> allTypeFundRecordDtos =  fundRecords.stream().map(AllTypeFundRecordDto::convertFrom).collect(Collectors.toList());


        allTypeFundRecordDtoPage.setList(allTypeFundRecordDtos);


        return ResultGenerator.genSuccessResult(allTypeFundRecordDtoPage);
    }


    
    @RequestMapping(value = "/pcRecharge", method = {RequestMethod.POST})
    public Result<String> pcRecharge(
            @ApiParam(name = "bankChannelId", value = "银行", required = true)
            @NotBlank(message = "请先选择银行") @RequestParam
            String bankChannelId,
            @ApiParam(name = "rechargeAmount", value = "充值金额", required = true)
            @NotNull(message = "充值金额不允许为空") @RequestParam
            Double rechargeAmount,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        String rechargeUrl = this.rechargeService.pcRecharge(member.getId(), rechargeAmount, bankChannelId);


        return ResultGenerator.genSuccessResult(rechargeUrl);
    }















}
