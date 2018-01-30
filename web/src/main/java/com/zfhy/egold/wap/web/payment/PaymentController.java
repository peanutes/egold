package com.zfhy.egold.wap.web.payment;

import com.google.gson.Gson;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.payment.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@RestController
@RequestMapping("/payment")
@Api(value = "PaymentController",tags = "PaymentController", description = "支付")
@Slf4j
@Validated
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    
    @RequestMapping(value = "/yibaoPayCallback", method = {RequestMethod.POST})
    public void yibaoPayCallback(
            @ApiParam(name = "data", value = "业务参数", required = true) @NotBlank @RequestParam
            String data,
            @ApiParam(name = "encryptkey", value = "key", required = true) @NotBlank @RequestParam
            String encryptkey, HttpServletResponse rps) throws IOException {

        this.paymentService.yibaoPayCallback(data, encryptkey);

        log.info("易宝支付回调结果：SUCCESS");

        PrintWriter writer = rps.getWriter();
        writer.write("SUCCESS");
        writer.flush();


        

    }
    
    @RequestMapping(value = "/yibaoWithdrawGoldPayCallback", method = {RequestMethod.POST})
    public void yibaoWithdrawGoldPayCallback(
            @ApiParam(name = "data", value = "业务参数", required = true) @NotBlank @RequestParam
            String data,
            @ApiParam(name = "encryptkey", value = "key", required = true) @NotBlank @RequestParam
            String encryptkey, HttpServletResponse rps) throws IOException {

        this.paymentService.yibaoWithdrawGoldPayCallback(data, encryptkey);

        log.info("易宝提金支付回调结果：SUCCESS");

        PrintWriter writer = rps.getWriter();
        writer.write("SUCCESS");
        writer.flush();

    }

    
    @RequestMapping(value = "/yibaoPcPayCallback", method = {RequestMethod.POST, RequestMethod.GET})
    public void yibaoPcPayCallback(HttpServletRequest request, HttpServletResponse rps) throws IOException {


        log.info(">>>>>>>pc 支付接口回调>>>>>{}", new Gson().toJson(request.getParameterMap()));

        String method = request.getMethod();
        log.info("http请求方法》》》》》{}", method);



        request.setCharacterEncoding("GBK");
        System.out.println("pathInfo:"+request.getPathInfo());
        System.out.println("requestURI:"+request.getRequestURI());

        String p1_MerId				= StringUtils.trim(request.getParameter("p1_MerId"));
        String r0_Cmd               = StringUtils.trim(request.getParameter("r0_Cmd"));
        String r1_Code              = StringUtils.trim(request.getParameter("r1_Code"));
        String r2_TrxId             = StringUtils.trim(request.getParameter("r2_TrxId"));
        String r3_Amt               = StringUtils.trim(request.getParameter("r3_Amt"));
        String r4_Cur               = StringUtils.trim(request.getParameter("r4_Cur"));
        String r5_Pid               = StringUtils.trim(request.getParameter("r5_Pid"));
        String r6_Order             = StringUtils.trim(request.getParameter("r6_Order"));
        String r7_Uid               = StringUtils.trim(request.getParameter("r7_Uid"));
        String r8_MP                = StringUtils.trim(request.getParameter("r8_MP"));
        String r9_BType             = StringUtils.trim(request.getParameter("r9_BType"));
        String rb_BankId            = StringUtils.trim(request.getParameter("rb_BankId"));
        String ro_BankOrderId       = StringUtils.trim(request.getParameter("ro_BankOrderId"));
        String rp_PayDate           = StringUtils.trim(request.getParameter("rp_PayDate"));
        String rq_CardNo            = StringUtils.trim(request.getParameter("rq_CardNo"));
        String ru_Trxtime           = StringUtils.trim(request.getParameter("ru_Trxtime"));
        String rq_SourceFee         = StringUtils.trim(request.getParameter("rq_SourceFee"));
        String rq_TargetFee         = StringUtils.trim(request.getParameter("rq_TargetFee"));
        String hmac		            = StringUtils.trim(request.getParameter("hmac"));
        String hmac_safe            = StringUtils.trim(request.getParameter("hmac_safe"));

        String[] strArr	= {p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType };

        boolean hmacIsCorrect = paymentService.verifyCallbackHmac(strArr, hmac);
        boolean hmacsafeIsCorrect = paymentService.verifyCallbackHmacSafe(strArr, hmac_safe);

        log.info(hmacIsCorrect+"&&&&"+hmacsafeIsCorrect);

        if(hmacIsCorrect && hmacsafeIsCorrect) {
            if(r9_BType.equals("2")) {
                
                this.paymentService.pcRechargeCallback(r1_Code, r3_Amt, r6_Order);
            }
        } else {
            throw new BusException("pc充值回调签名不正确");
        }








        log.info("易宝PC支付回调结果：SUCCESS");

        PrintWriter writer = rps.getWriter();
        writer.write("SUCCESS");
        writer.flush();

    }


    
    @RequestMapping(value = "/yibaoMallPayCallback", method = {RequestMethod.POST})
    public void yibaoMallPayCallback(
            @ApiParam(name = "data", value = "业务参数", required = true) @NotBlank @RequestParam
            String data,
            @ApiParam(name = "encryptkey", value = "key", required = true) @NotBlank @RequestParam
            String encryptkey, HttpServletResponse rps) throws IOException {

        this.paymentService.yibaoMallPayCallback(data, encryptkey);

        log.info("易宝PC支付回调结果：SUCCESS");

        PrintWriter writer = rps.getWriter();
        writer.write("SUCCESS");
        writer.flush();

    }











}
