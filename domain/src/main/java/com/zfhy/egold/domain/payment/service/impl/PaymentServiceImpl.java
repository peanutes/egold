package com.zfhy.egold.domain.payment.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.sun.deploy.association.utility.AppConstants;
import com.zfhy.egold.common.aspect.ann.AfterInvest;
import com.zfhy.egold.common.constant.AppEnvConst;
import com.zfhy.egold.common.constant.IdTypeConstant;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.ThreadUtil;
import com.zfhy.egold.domain.fund.dto.FundAccount;
import com.zfhy.egold.domain.fund.dto.RechargeStatus;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.fund.service.RechargeService;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.service.GoodsOrderService;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.dto.RiseFallInvestStatus;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.order.dto.OrderStatus;
import com.zfhy.egold.domain.order.dto.PaymentDto;
import com.zfhy.egold.domain.order.dto.PaymentLogStatus;
import com.zfhy.egold.domain.order.dto.RechargeOrPay;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.entity.PaymentLog;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.order.service.OrderService;
import com.zfhy.egold.domain.order.service.PaymentLogService;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.gateway.payment.yibao.YibaoPaymentApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private FundRecordService fundRecordService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private FinancialProductService financialProductService;

    @Autowired
    private YibaoPaymentApi yibaoPaymentApi;

    @Autowired
    private RedisService redisService;
    @Autowired
    private PaymentLogService paymentLogService;


    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private MyorderService myorderService;



    @Value("${app.env}")
    private String appEnv;



    @Autowired
    private GoodsOrderService goodsOrderService;







    @AfterInvest
    @Override
    public void paySuccess(Order order) {
        
        this.orderService.updateStatus(order.getId(), order.getVersion(), OrderStatus.PAY_SUCCESS.getStatus());
        
        String financeProductType = order.getFinanceProductType();

        if (Objects.equals(financeProductType, FinancialProductType.TERM_DEPOSIT.name())) {
            
            
            this.memberFundService.addTermGoldBalance(order.getMemberId(), order.getGoldWeight());

            
            this.fundRecordService.addBuyGoldRecord(order, FundAccount.TERM.name());


        }

        if (Objects.equals(financeProductType, FinancialProductType.CURRENT_DEPOSIT.name())) {
            
            
            this.memberFundService.addCurrentGoldBalance(order.getMemberId(), order.getGoldWeight());

            
            this.fundRecordService.addBuyGoldRecord(order, FundAccount.CURRENT.name());


        }

        if (Objects.equals(financeProductType, FinancialProductType.RISE_FALL.name())) {
            
            this.memberFundService.addInvestBalance(order.getMemberId(), order.getTotalAmount());


            FinancialProduct product = this.financialProductService.findById(order.getProductId());

            Double hadInvestAmount = DoubleUtil.doubleAdd(product.getHadInvestAmount(), order.getTotalAmount());

            if (hadInvestAmount > product.getAimAmount()) {
                throw new BusException("您好，超过了发售的可投金额");
            }


            int investStatus = product.getInvestStatus();

            if (DoubleUtil.equal(hadInvestAmount, product.getAimAmount())) {
                
                investStatus = RiseFallInvestStatus.FULL_SCALE.getCode();
            }

            this.financialProductService.updateInvestRiseFall(product.getId(), product.getVersion(), hadInvestAmount, investStatus);

        }

        
        this.investRecordService.addInvestRecord(order);

        
        this.myorderService.finish(String.format("order_%d", order.getId()));





    }


    @Override
    public void paySuccess(GoodsOrder order) {
        
        this.goodsOrderService.updatePaySuccessStatus(order.getId());

        
        this.myorderService.finish(String.format("morder_%d", order.getId()));
    }

    
    @Override
    public void yibaoWithdrawGoldPayCallback(String data, String encryptKey) {

        Map<String, String> result = this.yibaoPaymentApi.decryptCallbackData(data, encryptKey);
        log.info(">>>>>>>商城支付回调:{}", new Gson().toJson(result));

        if (Objects.isNull(result)) {
            return;
        }



        String requestno = result.get("requestno");
        String yborderid = result.get("yborderid");
        String amount = result.get("amount");
        String status = result.get("status");
        String errormsg = result.get("errormsg");
        PaymentLog paymentLog = this.paymentLogService.findByRequestNo(requestno);
        if (Objects.isNull(paymentLog)) {
            throw new BusException("支付记录不存在");
        }

        String paySn = paymentLog.getPaySn();
        GoodsOrder goodsOrder = this.goodsOrderService.findByOrderSn(paySn);

        if (Objects.isNull(goodsOrder)) {
            throw new BusException("订单不存在");
        }

        if (OrderStatus.finishStatus().contains(goodsOrder.getStatus())) {
            
            throw new BusException(String.format("订单:%s,状态为%d, 已经处理过，请不要重复处理", goodsOrder.getOrderSn(), goodsOrder.getStatus()));
        }


        if (Objects.equals(status, "PAY_SUCCESS")) {
             
            goodsOrder.setStatus(OrderStatus.PAY_SUCCESS.getStatus());
            this.goodsOrderService.update(goodsOrder);

            myorderService.finish(String.format("gorder_%d", goodsOrder.getId()));
            paymentLog.setStatus(PaymentLogStatus.PAY_SUCCESS.getCode());
            paymentLog.setRemarks(String.format("实际支付金额： %s元", amount));
            this.paymentLogService.update(paymentLog);
        } else {
            

            this.memberFundService.rollbackWithdrawGold(goodsOrder);


            
            paymentLog.setErrroMsg(errormsg);
            paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
            ThreadUtil.getThreadPollProxy().execute(() -> paymentLogService.update(paymentLog));


            
            this.goodsOrderService.payFail(goodsOrder);
        }

    }

    
    @Override
    public void yibaoMallPayCallback(String data, String encryptKey) {

        Map<String, String> result = this.yibaoPaymentApi.decryptCallbackData(data, encryptKey);
        log.info(">>>>>>>商城支付回调:{}", new Gson().toJson(result));

        if (Objects.isNull(result)) {
            return;
        }



        String requestno = result.get("requestno");
        String yborderid = result.get("yborderid");
        String amount = result.get("amount");
        String status = result.get("status");
        String errormsg = result.get("errormsg");
        PaymentLog paymentLog = this.paymentLogService.findByRequestNo(requestno);
        if (Objects.isNull(paymentLog)) {
            throw new BusException("支付记录不存在");
        }

        String paySn = paymentLog.getPaySn();
        GoodsOrder goodsOrder = this.goodsOrderService.findByOrderSn(paySn);

        if (Objects.isNull(goodsOrder)) {
            throw new BusException("订单不存在");
        }

        if (OrderStatus.finishStatus().contains(goodsOrder.getStatus())) {
            
            throw new BusException(String.format("订单:%s,状态为%d, 已经处理过，请不要重复处理", goodsOrder.getOrderSn(), goodsOrder.getStatus()));
        }


        if (Objects.equals(status, "PAY_SUCCESS")) {
            
            goodsOrder.setStatus(OrderStatus.PAY_SUCCESS.getStatus());
            this.goodsOrderService.update(goodsOrder);

            myorderService.finish(String.format("morder_%d", goodsOrder.getId()));
            paymentLog.setStatus(PaymentLogStatus.PAY_SUCCESS.getCode());
            paymentLog.setRemarks(String.format("实际支付金额： %s元", amount));
            this.paymentLogService.update(paymentLog);
        } else {
            
            this.memberFundService.rollbackMallPay(goodsOrder);


            
            paymentLog.setErrroMsg(errormsg);
            paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
            ThreadUtil.getThreadPollProxy().execute(() -> paymentLogService.update(paymentLog));


            
            this.goodsOrderService.payFail(goodsOrder);
        }

    }

    @Override
    public boolean verifyCallbackHmac(String[] strArr, String hmac) {
        return this.yibaoPaymentApi.verifyCallbackHmac(strArr, hmac);
    }

    @Override
    public boolean verifyCallbackHmacSafe(String[] strArr, String hmac_safe) {
        return this.yibaoPaymentApi.verifyCallbackHmac_safe(strArr, hmac_safe);
    }

    @Override
    public void pcRechargeCallback(String resultStatus, String rechargeAmount, String payRequestNo) {
        PaymentLog paymentLog = this.paymentLogService.findByRequestNo(payRequestNo);
        if (Objects.isNull(paymentLog)) {
            throw new BusException("支付日志记录不存在");
        }

        String paySn = paymentLog.getPaySn();
        Recharge rechargeRecord = this.rechargeService.findByPayNo(paySn);

        if (Objects.isNull(rechargeRecord)) {
            throw new BusException("充值记录不存在");
        }

        if (Objects.equals(RechargeStatus.PAY_SUCCESS.getStatus(), rechargeRecord.getStatus())) {
            
            throw new BusException(String.format("充值记录:%s,状态为%d, 已经处理过，请不要重复处理", rechargeRecord.getPayNo(), rechargeRecord.getStatus()));
        }


        if (!Objects.equals(resultStatus, "1")) {
            
            rechargeRecord.setStatus(RechargeStatus.PAY_FAIL.getStatus());
            this.rechargeService.update(rechargeRecord);

            paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
            return;
        }

        if (DoubleUtil.notEqual(new Double(rechargeAmount), rechargeRecord.getRechargeAmount())) {
            throw new BusException("充值金额不一致");
        }


        
        rechargeRecord.setStatus(RechargeStatus.PAY_SUCCESS.getStatus());
        this.rechargeService.update(rechargeRecord);

        paymentLog.setStatus(PaymentLogStatus.PAY_SUCCESS.getCode());
        this.paymentLogService.update(paymentLog);

        this.myorderService.finish(String.format("recharge_%d", rechargeRecord.getId()));

        this.memberFundService.rechargeSuccess(rechargeRecord);










    }


    @Override
    public void payFail(Order order) {


    }

    
    @Override
    public String payRequest(PaymentDto paymentDto, String terminalId, String callbackurl) {


        Bankcard bankcard = this.bankcardService.findBy("memberId", paymentDto.getMemberId());
        if (Objects.isNull(bankcard) || StringUtils.isBlank(bankcard.getBankCard())) {
            throw new BusException(ResultCode.BUS_NO_BIND_CARD, "您好，你还没有绑定银行卡");
        }
        String bankCardNo = bankcard.getBankCard();

        if (bankCardNo.length() < 6) {
            throw new BusException("您的银行卡信息号不正确");
        }

        String requestNo = savPaymentLog(paymentDto, bankcard.getBankName(), bankCardNo);




        Map<String, String> parameterMap = ImmutableMap.<String, String>builder()
                .put("requestno", requestNo)
                .put("identityid", String.format("%s_%06d", appEnv, paymentDto.getMemberId()))
                .put("identitytype", "USER_ID")
                .put("cardtop", bankCardNo.substring(0, 6))
                .put("cardlast", bankCardNo.substring(bankCardNo.length() - 4))
                .put("amount", DoubleUtil.toString(paymentDto.getPayAmount()))
                .put("advicesmstype", "MESSAGE")
                .put("avaliabletime", "30")
                .put("productname", String.format("支付产品:%s", paymentDto.getProductName()))
                .put("callbackurl", callbackurl)
                .put("requesttime", DateUtil.toString(new Date()))
                .put("terminalid", terminalId)
                .put("registtime", DateUtil.toString(bankcard.getCreateDate()))



                .put("lastloginterminalid", terminalId)
                .put("issetpaypwd", "1")
                .put("free1", "")
                .put("free2", "")
                .put("free3", "")
                .build();
        if(appEnv.equals(AppEnvConst.dev)){
            Map<String, String> result = this.yibaoPaymentApi.bindPayRequest(parameterMap);
        }
        return requestNo;

    }

    public String savPaymentLog(PaymentDto paymentDto, String bankName, String bankCardNo) {
        String requestNo = this.redisService.getIdByType(IdTypeConstant.PAY_REQUEST);

        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setUpdateDate(new Date());
        paymentLog.setStatus(PaymentLogStatus.WAIT_SMS_VALIDATE.getCode());
        paymentLog.setAmount(paymentDto.getPayAmount());
        paymentLog.setDelFlag("0");
        paymentLog.setMemberId(paymentDto.getMemberId());
        paymentLog.setPayRequestNo(requestNo);
        paymentLog.setPaySn(paymentDto.getPaySn());
        paymentLog.setRechargeOrPay(paymentDto.getRechargeOrPay().getCode());
        paymentLog.setCreateDate(new Date());
        paymentLog.setBankName(bankName);
        paymentLog.setBankCard(bankCardNo);
        this.paymentLogService.save(paymentLog);
        return requestNo;
    }

    @Override
    public void yibaoPayCallback(String data, String encryptKey) {

        Map<String, String> result = this.yibaoPaymentApi.decryptCallbackData(data, encryptKey);
        log.info(">>>>>>>支付回调:{}", new Gson().toJson(result));

        if (Objects.isNull(result)) {
            return;
        }

        String requestno = result.get("requestno");
        String yborderid = result.get("yborderid");
        String amount = result.get("amount");
        String status = result.get("status");
        String errormsg = result.get("errormsg");
        PaymentLog paymentLog = this.paymentLogService.findByRequestNo(requestno);

        if (Objects.equals(status, "PAY_SUCCESS")) {
            

            Integer rechargeOrPay = paymentLog.getRechargeOrPay();


            if (Objects.equals(rechargeOrPay, RechargeOrPay.PAY.getCode())) {
                
                Order order = this.orderService.findByOrderSn(paymentLog.getPaySn());

                if (Objects.isNull(order)) {
                    throw new BusException("订单不存在");
                }

                Double shouldPayAmount = order.getShouldPayAmount();
                if (DoubleUtil.notEqual(shouldPayAmount, new Double(amount))) {
                    
                    paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
                    paymentLog.setErrroMsg(String.format("支付金额不一致，订单支付金额：%s,实际支付金额:%s", DoubleUtil.toString(shouldPayAmount), amount));
                } else {
                    paymentLog.setStatus(PaymentLogStatus.PAY_SUCCESS.getCode());
                }

                
                ThreadUtil.getThreadPollProxy().execute(() -> paymentLogService.update(paymentLog));

                if (PaymentLogStatus.PAY_SUCCESS.getCode() == paymentLog.getStatus()) {
                    
                    this.paySuccess(order);

                }
            } else if (Objects.equals(rechargeOrPay, RechargeOrPay.RECHARGE.getCode())) {
                
                Recharge recharge = this.rechargeService.findByPayNo(paymentLog.getPaySn());

                if (Objects.isNull(recharge)) {
                    throw new BusException("充值记录不存在");
                }

                Double rechargeAmount = recharge.getRechargeAmount();
                if (DoubleUtil.notEqual(rechargeAmount, new Double(amount))) {
                    
                    paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
                    paymentLog.setErrroMsg(String.format("支付金额不一致，充什支付金额：%s,实际支付金额:%s", DoubleUtil.toString(rechargeAmount), amount));
                } else {
                    paymentLog.setStatus(PaymentLogStatus.PAY_SUCCESS.getCode());

                }

                
                ThreadUtil.getThreadPollProxy().execute(() -> paymentLogService.update(paymentLog));


                if (PaymentLogStatus.PAY_SUCCESS.getCode() == paymentLog.getStatus()) {
                    
                    this.rechargeSuccess(recharge);
                    myorderService.finish(String.format("recharge_%d", recharge.getId()));
                }

            }

        } else {
            
            
            paymentLog.setStatus(PaymentLogStatus.PAY_FAIL.getCode());
            paymentLog.setErrroMsg(String.format("支付失败，易宝返回状态为：%s，错误信息为：%s", status, errormsg));
            this.paymentLogService.update(paymentLog);


            
            Integer rechargeOrPay = paymentLog.getRechargeOrPay();


            if (Objects.equals(rechargeOrPay, RechargeOrPay.PAY.getCode())) {
                
                Order order = this.orderService.findByOrderSn(paymentLog.getPaySn());

                if (Objects.isNull(order)) {
                    throw new BusException("订单不存在");
                }


                order.setStatus(OrderStatus.PAY_FAIL.getStatus());

                
                this.payFail(order);
                
                this.orderService.update(order);

            } else if (Objects.equals(rechargeOrPay, RechargeOrPay.RECHARGE.getCode())) {

                
                Recharge recharge = this.rechargeService.findByPayNo(paymentLog.getPaySn());

                if (Objects.isNull(recharge)) {
                    throw new BusException("充值记录不存在");
                }

                recharge.setStatus(RechargeStatus.PAY_FAIL.getStatus());
                this.rechargeService.update(recharge);

            }


        }




    }

    private void rechargeSuccess(Recharge recharge) {
        
        this.rechargeService.rechargeSuccess(recharge);



        
        this.memberFundService.rechargeSuccess(recharge);

    }

    @Override
    public void validatePaySmsCode(Integer memberId, String payRequestNo, String smsCode) {

        PaymentLog paymentLog = this.paymentLogService.findBy("payRequestNo", payRequestNo);
        if (Objects.isNull(paymentLog)) {
            throw new BusException("支付请求编码不存在");
        }


        Map<String, String> parameterMap = ImmutableMap.<String, String>builder()
                .put("requestno", payRequestNo)
                .put("validatecode", smsCode)
                .build();


        try {
            if(appEnv.equals(AppEnvConst.dev)) {
                Map<String, String> result = this.yibaoPaymentApi.bindPayConfirm(parameterMap);
            }

            
            ThreadUtil.getThreadPollProxy().execute(() -> this.paymentLogService.updateWaitCallback(payRequestNo, PaymentLogStatus.WAIT_CALLBACK, ""));

            Integer rechargeOrPay = paymentLog.getRechargeOrPay();

            if (Objects.equals(RechargeOrPay.PAY.getCode(), rechargeOrPay)) {
                
                
                this.orderService.updateStatusByOrderSn(paymentLog.getPaySn(), OrderStatus.PAY_PROCESSING, OrderStatus.CONFIRMED_PAY);

            } else if (Objects.equals(RechargeOrPay.RECHARGE.getCode(), rechargeOrPay)) {
                
                this.rechargeService.updateStatusByPayNo(paymentLog.getPaySn(), RechargeStatus.PAY_PROCESSING, RechargeStatus.CONFIRMED_PAY);
            } else if (Objects.equals(RechargeOrPay.WITHDRAW_GOLD_PAY.getCode(), rechargeOrPay)) {
                
                this.goodsOrderService.updateStatusByOrderSn(paymentLog.getPaySn(), OrderStatus.PAY_PROCESSING, OrderStatus.CONFIRMED_PAY);

            }



        } catch (Throwable throwable) {
            log.error("短信码确认支付异常", throwable);

            
            ThreadUtil.getThreadPollProxy().execute(() -> this.paymentLogService.updatePaymentStatus(payRequestNo, PaymentLogStatus.VALIDATE_SMS_FAIL, throwable.getMessage()));

            throw new BusException(throwable.getMessage());

        }


        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            log.error("等待结果",e);
        }

        PaymentLog paymentReslut = this.paymentLogService.findBy("payRequestNo", payRequestNo);

        Integer status = paymentReslut.getStatus();
        if (Objects.nonNull(status) && status == PaymentLogStatus.PAY_FAIL.getCode()) {
            
            log.info("支付回调结果为：{}", new Gson().toJson(paymentReslut));
            String errroMsg = paymentReslut.getErrroMsg();
            if (StringUtils.isBlank(errroMsg)) {
                errroMsg = "您好！支付失败";
            }
            throw new BusException(errroMsg);
        }

    }

    @Override
    public String pcRecharge(PaymentDto paymentDto, String bankChannelId) {
        String requestNo = savPaymentLog(paymentDto, "", "");

        Map<String, String> parameters = ImmutableMap.<String, String>builder()
                .put("p2_Order", requestNo)
                .put("pd_FrpId", bankChannelId)
                .put("p3_Amt", DoubleUtil.toString(paymentDto.getPayAmount()))
                .build();


        return this.yibaoPaymentApi.getPayURL(parameters);

    }
}
