package com.zfhy.egold.api.web.order;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.constant.AppEnvConst;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.order.dto.*;
import com.zfhy.egold.domain.order.entity.Myorder;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.order.service.OrderService;
import com.zfhy.egold.domain.order.service.TempOrderService;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/order/order")
@Api(value = "订单",tags = "OrderController", description = "订单接口")
@Slf4j
@Validated
public class OrderController {
    @Resource
    private OrderService orderService;

    @Autowired
    private RedisService redisService;


    @Autowired
    private MyorderService myorderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TempOrderService tempOrderService;

    @Value("${app.env}")
    private String appEnv;


    
    @PostMapping("/orderDetail")
    public Result<OrderDto> orderDetail(
            @ApiParam(name = "orderSn", value = "订单编码", required = true) @NotBlank @RequestParam String orderSn,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        Order order = orderService.findBy("orderSn", orderSn);

        if (Objects.isNull(order)) {
            throw new BusException("您好，订单不存在，请确认");
        }

        if (!Objects.equals(order.getMemberId(), member.getId())) {
            throw new BusException("您好，你没有权限操作该订单，请联系客服人员");
        }

        return ResultGenerator.genSuccessResult(new OrderDto().convertFrom(order));
    }

    
    @PostMapping("/submitOrder")
    public Result<OrderDto> submitOrder(
            @ApiParam(name = "orderSubmitDto", value = "订单信息", required = true)
            @ModelAttribute @Valid OrderSubmitDto orderSubmitDto,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        if (!Objects.equals(member.getId(), orderSubmitDto.getMemberId())) {
            log.error("会员id与token中的不一致");
            throw new BusException("你没有权限，请联系客服");
        }


        OrderDto orderDto = this.orderService.submitOrder(orderSubmitDto, sysParameter.getTerminalType());
        return ResultGenerator.genSuccessResult(orderDto);
    }


    
    @PostMapping("/switchInto")
    public Result<OrderDto> switchInto(
            @ApiParam(name = "switchGoldWeight", value = "转入重量（克）", required = true)
            @NotNull(message = "转入重量不能为空") @RequestParam Double switchGoldWeight,
            @ApiParam(name = "discountCouponId", value = "加息卷id", required = false)
            @RequestParam(required = false) Integer discountCouponId,
            @ApiParam(name = "productId", value = "要购买的产品id", required = true)
            @NotNull(message = "产品id不能为空") @RequestParam Integer productId,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        OrderDto orderDto = this.orderService.switchInto(member.getId(), switchGoldWeight, discountCouponId, productId, sysParameter.getTerminalType());

        return ResultGenerator.genSuccessResult(orderDto);
    }


    
    @PostMapping("/confirmPay")
    public Result<OrderSubmitResultDto> confirmPay(
            @ApiParam(name = "orderSn", value = "订单号", required = true)
            @NotNull @RequestParam String orderSn,
            @ApiParam(name = "payPwd", value = "支付密码", required = true)
            @NotNull @RequestParam String payPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        OrderSubmitResultDto orderDto =  this.orderService.confirmPay(member.getId(), orderSn, payPwd, sysParameter.getTerminalId());

        return ResultGenerator.genSuccessResult(orderDto);
    }

    
    @PostMapping("/validatePaySmsCode")
    public Result<String> validatePaySmsCode(
            @ApiParam(name = "payRequestNo", value = "支付请求号", required = true)
            @NotBlank @RequestParam String payRequestNo,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true)
            @NotNull @RequestParam String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        this.paymentService.validatePaySmsCode(member.getId(), payRequestNo, smsCode);

        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/validatePaySmsCode4Mall")
    public Result<String> validatePaySmsCode4Mall(
            @ApiParam(name = "payRequestNo", value = "支付请求号", required = true)
            @NotBlank @RequestParam String payRequestNo,
            @ApiParam(name = "smsCode", value = "短信验证码", required = true)
            @NotNull @RequestParam String smsCode,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken4Mall(sysParameter.getToken());

        this.paymentService.validatePaySmsCode(member.getId(), payRequestNo, smsCode);

        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/myOrderList")
    public Result<Page<MyorderDto>> myOrderList
            (@ApiParam(name = "myorderStatus", value = "状态,PROCESSING=进行中，FINISH=已完成，EXPIRED=已过期", required = false)
             @RequestParam(required = false) MyorderStatus myorderStatus,
             @ApiParam(name = "myorderType", value = "类型，BUY_GOLD=买金，SALE_GOLD=卖金，STORE_GOLD=存金，" +
                     "WITHDRAW_GOLD=提金，RECHARGE=充值，WITHDRAW=提现，GOLD_MALL=黄金商城，RISE_FALL=看涨跌", required = false)
             @RequestParam(required = false) MyorderType myorderType,
             @ApiParam(name = "page", value = "页数", required = true)
             @RequestParam @NotNull(message = "页数不允许为空") Integer page,
             @ApiParam(name = "size", value = "每页条数", required = true)
             @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
             @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        PageHelper.startPage(page, size);
        Condition condition = new Condition(Myorder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("memberId", member.getId());
        if (Objects.nonNull(myorderStatus)) {
            criteria.andEqualTo("myorderStatus", myorderStatus.getCode());
        }

        if (Objects.nonNull(myorderType)) {
            criteria.andEqualTo("myorderType", myorderType.getCode());
        } else {
            criteria.andNotIn("myorderType", Arrays.asList(MyorderType.RECHARGE.getCode(), MyorderType.WITHDRAW.getCode()));
        }

        condition.setOrderByClause("create_date desc");


        List<Myorder> myorders = this.myorderService.findByCondition(condition);

        PageInfo<Myorder> investRecordPageInfo = new PageInfo<>(myorders);

        Page<MyorderDto> myorderDtoPage = new Page<>();
        BeanUtils.copyProperties(investRecordPageInfo, myorderDtoPage);
        List<MyorderDto> dtoList = myorders.stream().map(ele -> new MyorderDto().convertFrom(ele)).collect(Collectors.toList());
        myorderDtoPage.setList(dtoList);


        return ResultGenerator.genSuccessResult(myorderDtoPage);
    }


    
    @PostMapping("/myorderDetail")
    public Result<MyorderDetailDto> myorderDetail
            (@ApiParam(name = "myorderId", value = "我的订单id", required = true)
             @RequestParam @NotNull Integer myorderId,
             @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        MyorderDetailDto myorderDetailDto = this.myorderService.getMyorderDetail(myorderId);

        return ResultGenerator.genSuccessResult(myorderDetailDto);
    }

    
    @PostMapping("/modifyOrderAmount")
    public Result<String> modifyOrderAmount(
            @ApiParam(name = "orderSn", value = "订单号", required = true)
            @RequestParam @NotNull(message = "订单号不能为空") String orderSn,
            @ApiParam(name = "amount", value = "金额", required = true)
            @RequestParam @NotNull(message = "金额不能为空") Double amount) {

        if (Objects.equals(appEnv, AppEnvConst.prd.name())) {
            
            return ResultGenerator.genSuccessResult();
        }

        this.tempOrderService.modifyOrderAmount(orderSn, amount);

        return ResultGenerator.genSuccessResult();
    }













}
