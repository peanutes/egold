package com.zfhy.egold.domain.order.service.impl;

import com.google.gson.Gson;
import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.HashUtil;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.FundRecordService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.dto.ProductStatus;
import com.zfhy.egold.domain.invest.dto.RiseFallInvestStatus;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.dao.OrderMapper;
import com.zfhy.egold.domain.order.dto.*;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.entity.TempOrder;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.order.service.OrderService;
import com.zfhy.egold.domain.order.service.TempOrderService;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.report.dto.ProductStatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.zfhy.egold.common.util.DateUtil.YYYY_MM_DD_HH_MM_SS;



@Service
@Transactional
@Slf4j
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FinancialProductService financialProductService;
    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FundRecordService fundRecordService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TempOrderService tempOrderService;


    @Autowired
    private MyorderService myorderService;

    @Value("${pay.callback.url}")
    private String payCallbackUrl;



    @Override
    public OrderDto submitOrder(OrderSubmitDto orderSubmitDto, String terminalType) {
        
        Member member = this.memberService.checkMember(orderSubmitDto.getMemberId());

        
        FinancialProduct product = this.financialProductService.findById(orderSubmitDto.getProductId());
        
        if (!Objects.equals(product.getStatus(), ProductStatus.ON_SELL.getCode())) {
            
            throw new BusException("您好，该理财产品已下架");
        }

        
        if (Objects.equals(product.getProductType(), FinancialProductType.RISE_FALL.name())) {
            
            if (!Objects.equals(product.getInvestStatus(), RiseFallInvestStatus.PROCESSING.getCode())) {
                
                throw new BusException("您好，该理财产品已满标，请选择其它产品");
            }

            
            Double investAmount = DoubleUtil.doubleAdd(product.getHadInvestAmount(), orderSubmitDto.getTotalAmount());
            if (investAmount > product.getAimAmount()) {
                
                throw new BusException(String.format("您好，您的投资金额超出了剩余可投金额:%s元", DoubleUtil.toString(DoubleUtil.doubleSub(product.getAimAmount(), product.getHadInvestAmount()))));
            }

        }


        
        if (Objects.equals(product.getNewUser(), "1")) {
            boolean newUser = this.memberService.isNewUser(member.getId());
            if (!newUser) {
                throw new BusException("您好，您不能参与新手标的投资！");
            }

        }

        
        Double minAmount = product.getMinAmount();
        if (Objects.nonNull(minAmount) && !Objects.equals(minAmount, 0D)) {
            if (orderSubmitDto.getTotalAmount() < minAmount) {
                throw new BusException(String.format("您好，不得低于起投金额%s", DoubleUtil.toString(minAmount)));
            }
        }

        
        Double maxAmount = product.getMaxAmount();
        if (Objects.nonNull(maxAmount) && !Objects.equals(maxAmount, 0D)) {
            if (orderSubmitDto.getTotalAmount() > maxAmount) {
                throw new BusException(String.format("您好，投资金额不得高于上限：%s", DoubleUtil.toString(maxAmount)));
            }
        }

        
        Integer cashCouponId = orderSubmitDto.getCashCouponId();
        Integer discountCouponId = orderSubmitDto.getDiscountCouponId();
        Double cashCouponAmount = 0D;
        Double disCountRate = 0D;
        if (Objects.nonNull(cashCouponId) && cashCouponId != 0) {
            MemberCoupon cashCoupon = this.memberCouponService.findById(cashCouponId);
            log.debug(">>>>>>>>>>订单：{}", new Gson().toJson(orderSubmitDto));
            log.debug(">>>>>>>>>>优惠卷：{}", new Gson().toJson(cashCoupon));
            cashCouponAmount = validateCoupon(orderSubmitDto.getTotalAmount(), product, cashCoupon, 1, "红包", member.getId());
        }
        if (Objects.nonNull(discountCouponId) && discountCouponId != 0) {
            MemberCoupon discountCoupon = this.memberCouponService.findById(discountCouponId);
            disCountRate = validateCoupon(orderSubmitDto.getTotalAmount(), product, discountCoupon, 2, "加息卷", member.getId());
        }

        
        Double currentPrice = this.redisService.getRealTimePrice();
        Double goldWeight = null;
        if (Objects.equals(product.getNewUser(), "1")) {
            
            if (!Objects.equals(orderSubmitDto.getTotalAmount(), product.getPrice())) {
                throw new BusException("您好，新手特权金每人只允许购买1克！");
            }
            goldWeight = 1D;

        } else {
            
            goldWeight = DoubleUtil.doubleDiv(orderSubmitDto.getTotalAmount(), currentPrice, 4);
            
            goldWeight = DoubleUtil.setScaleDown(goldWeight, 3);
            
            orderSubmitDto.setTotalAmount(DoubleUtil.doubleMul(goldWeight, currentPrice, 2));

        }



        Double balancePayAmount = orderSubmitDto.getBalancePayAmount();

        
        MemberFund memberFund = this.memberFundService.findBy("memberId", orderSubmitDto.getMemberId());
        if (Objects.nonNull(balancePayAmount)) {
            if (balancePayAmount > memberFund.getCashBalance()) {
                throw new BusException("余额不足");
            }
        }

        
        TempOrder tempOrder = new TempOrder();
        BeanUtils.copyProperties(orderSubmitDto, tempOrder);
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long number = this.redisService.incr(String.format(RedisConst.ORDER_SN_SERIAL, dateStr), 1L, 86400L);

        tempOrder.setOrderSn(String.format("%s%06d", dateStr, number));
        tempOrder.setCreateDate(new Date());
        tempOrder.setDelFlag("0");
        tempOrder.setCashCouponAmount(cashCouponAmount);
        tempOrder.setDiscountCouponRate(disCountRate);
        tempOrder.setGoldWeight(goldWeight);
        tempOrder.setCurrentGoldPrice(currentPrice);
        tempOrder.setFinanceProductType(product.getProductType());
        tempOrder.setCashCouponId(cashCouponId);
        tempOrder.setDiscountCouponId(discountCouponId);

        Boolean estimateFall = orderSubmitDto.getEstimateFall();
        if (Objects.nonNull(estimateFall) && estimateFall) {
            tempOrder.setEstimateFall(1);
        }else {
            tempOrder.setEstimateFall(0);
        }
        Double shouldPayAmount = DoubleUtil.doubleSub(orderSubmitDto.getTotalAmount(), cashCouponAmount, 4);

        tempOrder.setBalancePayAmount(0D);
        tempOrder.setShouldPayAmount(DoubleUtil.changeDecimal(shouldPayAmount, 2));

        tempOrder.setTerminalType(terminalType);


        if (Objects.nonNull(balancePayAmount)) {
            if (balancePayAmount >= shouldPayAmount) {
                tempOrder.setShouldPayAmount(0D);
                balancePayAmount = shouldPayAmount;
                tempOrder.setBalancePayAmount(balancePayAmount);
            } else {
                shouldPayAmount = DoubleUtil.doubleSub(shouldPayAmount, balancePayAmount, 2);
                tempOrder.setShouldPayAmount(shouldPayAmount);
                tempOrder.setBalancePayAmount(balancePayAmount);
            }
        }

        tempOrder.setStatus(OrderStatus.WAIT_PAY.getStatus());
        tempOrder.setVersion(1);
        tempOrder.setSwitchInto(0);
        this.tempOrderService.save(tempOrder);

        return new OrderDto().convertFromTempOrder(tempOrder);

    }



    
    @Override
    public OrderDto switchInto(Integer memberId, Double switchGoldWeight, Integer discountCouponId, Integer productId, String terminalType) {
        
        Member member = this.memberService.findById(memberId);
        if (Objects.equals(member.getEnable(), "1")) {
            throw new BusException("您好，您的账户已被禁用，请联系客服！");
        }

        
        MemberFund memberFund = this.memberFundService.findBy("memberId", memberId);

        if (memberFund.getCurrentGoldBalance() < switchGoldWeight) {
            throw new BusException("您好，您账户中的流动金余额不足！");
        }



        
        FinancialProduct product = this.financialProductService.findById(productId);
        if (Objects.isNull(product)) {
            throw new BusException("理财产品不存在");
        }

        if (!FinancialProductType.TERM_DEPOSIT.name().equals(product.getProductType())) {
            throw new BusException("只允许从流动金转入到定期理财产品！");
        }

        
        Double currentPrice = this.redisService.getRealTimePrice();
        Double totalAmount = DoubleUtil.doubleMul(currentPrice, switchGoldWeight, 2);


        
        MemberCoupon discountCoupon = null;
        Double cashCouponAmount = 0D;
        Double disCountRate = 0D;
        if (Objects.nonNull(discountCouponId) && discountCouponId != 0) {
            discountCoupon = this.memberCouponService.findById(discountCouponId);
            disCountRate = validateCoupon(totalAmount, product, discountCoupon, discountCoupon.getType(), "加息卷", memberId);





        }



        
        TempOrder tempOrder = new TempOrder();
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long number = this.redisService.incr(String.format(RedisConst.ORDER_SN_SERIAL, dateStr), 1L, 86400L);

        tempOrder.setOrderSn(String.format("%s%06d", dateStr, number));
        tempOrder.setCreateDate(new Date());
        tempOrder.setDelFlag("0");
        tempOrder.setCashCouponAmount(cashCouponAmount);
        tempOrder.setDiscountCouponRate(disCountRate);
        tempOrder.setGoldWeight(switchGoldWeight);
        tempOrder.setShouldPayAmount(0D);
        tempOrder.setBalancePayAmount(0D);
        tempOrder.setMemberId(memberId);
        tempOrder.setCurrentGoldPrice(currentPrice);
        tempOrder.setStatus(OrderStatus.WAIT_PAY.getStatus());
        tempOrder.setProductId(productId);
        tempOrder.setProductName(product.getProductName());
        tempOrder.setTotalAmount(totalAmount);
        tempOrder.setVersion(1);
        tempOrder.setSwitchInto(1);
        tempOrder.setFinanceProductType(product.getProductType());
        tempOrder.setCashCouponId(null);
        tempOrder.setDiscountCouponId(discountCouponId);
        tempOrder.setProductType(1);
        tempOrder.setTerminalType(terminalType);

        this.tempOrderService.save(tempOrder);

        return new OrderDto().convertFromTempOrder(tempOrder);

    }

    @Override
    public OrderSubmitResultDto confirmPay(Integer memberId, String orderSn, String payPwd, String terminalId) {
        Member member = this.memberService.findById(memberId);
        TempOrder tempOrder = this.tempOrderService.findBy("orderSn", orderSn);
        if (Objects.isNull(tempOrder)) {
            throw new BusException("订单不存在，请您确认");
        }

        if (Objects.equals(tempOrder.getStatus(), OrderStatus.EXPIRED_PAY.getStatus())) {
            throw new BusException("您好，订单已过期，请重新发起");
        }

        if (StringUtils.isBlank(member.getDealPwd())) {
            throw new BusException(ResultCode.BUS_NO_DEAL_PWD, "您好，您还没有设置交易密码，请先设置");
        }

        FinancialProduct product = this.financialProductService.findById(tempOrder.getProductId());
        if (OrderStatus.WAIT_PAY.getStatus() != tempOrder.getStatus()) {
            throw new BusException("订单状态不正确，请联系客服妹妹");
        }

        
        String dealPwd = member.getDealPwd();
        if (!Objects.equals(dealPwd, HashUtil.sha1(String.join("", payPwd, member.getSalt())))) {
            throw new BusException("支付密码不正确");
        }


        
        if (Objects.equals(product.getProductType(), FinancialProductType.RISE_FALL.name())) {
            
            if (!Objects.equals(product.getInvestStatus(), RiseFallInvestStatus.PROCESSING.getCode())) {
                
                throw new BusException("您好，该理财产品已满标，请选择其它产品");
            }

            
            Double investAmount = DoubleUtil.doubleAdd(product.getHadInvestAmount(), tempOrder.getTotalAmount());
            if (investAmount > product.getAimAmount()) {
                
                throw new BusException(String.format("您好，您的投资金额超出了剩余可投金额:%s元", DoubleUtil.toString(DoubleUtil.doubleSub(product.getAimAmount(), product.getHadInvestAmount()))));
            }

        }


        
        if (Objects.equals(product.getNewUser(), "1")) {
            boolean newUser = this.memberService.isNewUser(member.getId());
            if (!newUser) {
                throw new BusException("您好，您不能参与新手标的投资！");
            }

        }

        
        Double minAmount = product.getMinAmount();
        if (Objects.nonNull(minAmount) && !Objects.equals(minAmount, 0D)) {
            if (DoubleUtil.lessOrEqual(tempOrder.getTotalAmount(), minAmount)) {
                throw new BusException(String.format("您好，不得低于起投金额%s", DoubleUtil.toString(minAmount)));
            }
        }

        
        Double maxAmount = product.getMaxAmount();
        if (Objects.nonNull(maxAmount) && !Objects.equals(maxAmount, 0D)) {
            if (tempOrder.getTotalAmount() > maxAmount) {
                throw new BusException(String.format("您好，投资金额不得高于上限：%s", DoubleUtil.toString(maxAmount)));
            }
        }

        
        Integer cashCouponId = tempOrder.getCashCouponId();
        Integer discountCouponId = tempOrder.getDiscountCouponId();
        Double cashCouponAmount = 0D;
        Double disCountRate = 0D;
        if (Objects.nonNull(cashCouponId) && cashCouponId != 0) {
            MemberCoupon cashCoupon = this.memberCouponService.findById(cashCouponId);

            log.debug(">>>>>>>>>>订单2：{}", new Gson().toJson(tempOrder));
            log.debug(">>>>>>>>>>优惠卷2：{}", new Gson().toJson(cashCoupon));
            cashCouponAmount = validateCoupon(tempOrder.getTotalAmount(), product, cashCoupon, 1, "红包", member.getId());
            
            cashCoupon.setStatus(3);
            cashCoupon.setUpdateDate(new Date());
            this.memberCouponService.update(cashCoupon);
        }
        if (Objects.nonNull(discountCouponId) && discountCouponId != 0) {
            MemberCoupon discountCoupon = this.memberCouponService.findById(discountCouponId);
            disCountRate = validateCoupon(tempOrder.getTotalAmount(), product, discountCoupon, 2, "加息卷", member.getId());
            
            discountCoupon.setStatus(3);
            discountCoupon.setUpdateDate(new Date());
            this.memberCouponService.update(discountCoupon);
        }

        Order order = this.convertFrom(tempOrder);
        order.setStatus(OrderStatus.CONFIRMED_PAY.getStatus());
        order.setRequestPayTime(new Date());

        this.save(order);

        
        this.memberFundService.buyGold(order);


        Integer switchInto = order.getSwitchInto();
        if (Objects.equals(switchInto, 1)) {
            
            this.myorderService.addGoldSwitch(product, order);

            
            MemberFund memberFund = this.memberFundService.findBy("memberId", memberId);

            if (memberFund.getCurrentGoldBalance() < order.getGoldWeight()) {
                throw new BusException("您好，您账户中的流动金余额不足！");
            }
            
            this.investRecordService.soldCurrentGold(memberId, false, order.getGoldWeight());
            this.memberFundService.switchInto(order, memberFund.getVersion());
        } else {

            this.myorderService.addBuyGold(product, order);
        }

        String payRequestNo = "";
        if (DoubleUtil.equal(tempOrder.getShouldPayAmount(), 0D)) {
            this.paymentService.paySuccess(order);
            order.setStatus(OrderStatus.PAY_SUCCESS.getStatus());
        } else {
            PaymentDto paymentDto = PaymentDto.builder()
                    .memberId(memberId)
                    .payAmount(order.getShouldPayAmount())
                    .paySn(order.getOrderSn())
                    .productName(order.getProductName())
                    .rechargeOrPay(RechargeOrPay.PAY)
                    .build();
            // 调用充值请求接口
            payRequestNo = this.paymentService.payRequest(paymentDto, terminalId, payCallbackUrl);
        }

        tempOrder.setStatus(order.getStatus());
        this.tempOrderService.update(tempOrder);

        OrderSubmitResultDto orderSubmitResultDto = new OrderSubmitResultDto();
        BeanUtils.copyProperties(order, orderSubmitResultDto);
        orderSubmitResultDto.setPayRequestNo(payRequestNo);

        return orderSubmitResultDto;
    }



    private Order convertFrom(TempOrder tempOrder) {

        Order order = new Order();
        BeanUtils.copyProperties(tempOrder, order);
        order.setId(null);
        return order;
    }

    @Override
    public void updateStatus(Integer id, Integer version, int orderStatus) {
        int count = this.orderMapper.updateStatus(id, version, orderStatus);
        if (count != 1) {
            throw new BusException("更新订单状态失败");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> findExpired(Integer expiredTime) {
        return this.orderMapper.findExpired(expiredTime);
    }

    @Override
    public void expiredOrder(Order order) {

        
        this.updateStatus(order.getId(), order.getVersion(), OrderStatus.EXPIRED_PAY.getStatus());

        
        this.myorderService.expiredMyorder(String.format("order_%d", order.getId()));



        
        this.memberCouponService.fallback(order);

        
        this.memberFundService.fallback(order);

    }

    @Override
    public void updateStatusByOrderSn(String orderSn, OrderStatus newStatus, OrderStatus oldStatus) {
        this.orderMapper.updateStatusByOrderSn(orderSn, newStatus.getStatus(), oldStatus.getStatus());

    }

    @Override
    public Order findByOrderSn(String orderSn) {
        return this.findBy("orderSn", orderSn);
    }

    @Override
    public List<Order> list(Map<String, String> params) {

        return this.orderMapper.list(params);
    }

    @Override
    public List<ProductStatisticsDto> productBuyerStatistics() {
        return this.orderMapper.productBuyerStatistics();
    }


    private Double validateCoupon(Double totalAmount, FinancialProduct product, MemberCoupon coupon, int couponType, String couponName, Integer memberId) {

        if (Objects.isNull(coupon)) {
            throw new BusException("红包不存在");
        }
        if (!Objects.equals(coupon.getMemberId(), memberId)) {
            throw new BusException("该优惠卷不属于该会员");
        }

        if (coupon.getStatus() != 1) {
            throw new BusException("该优惠卷已使用！");
        }

        if (coupon.getType() != couponType) {
            throw new BusException(String.format("不是%s类型的优惠卷", couponName));
        }

        if (coupon.getBeginTime().after(new Date())) {
            throw new BusException(String.format("您好，这张优惠卷还没生效，请在%s后再使用", DateUtil.toString(coupon.getBeginTime(), YYYY_MM_DD_HH_MM_SS)));
        }


        if (coupon.getEndTime().before(new Date())) {
            throw new BusException("您好，您这张优惠卷已过期，需要更多优惠，请联系客服妹妹！");
        }


        if (DoubleUtil.lessOrEqual(totalAmount, coupon.getInvestAmountMin())) {

            
            throw new BusException(String.format("您好，您至少需要投资%s元才可以使用该%s！", DoubleUtil.toString(coupon.getInvestAmountMin()), couponName));

        }

        if (!(product.getTerm() >= coupon.getInvestDealineMin())) {
            throw new BusException(String.format("您好，投资期限大于或等于%d天的理财产品才允许使用该%s！", coupon.getInvestDealineMin(), couponName));
        }

        String productType = coupon.getProductType();


        if (!Objects.equals("0", productType) && Objects.nonNull(productType)) {
            String[] productTypes = productType.split(",");
            List<String> productTypeList = Arrays.asList(productTypes);
            if (!productTypeList.contains(product.getProductType())) {
                throw new BusException(String.format("您好，该%s不能用于这种类型的理财产品，如需要帮忙，请联系客服人员", couponName));
            }

        }

        return coupon.getCouponAmount();
    }


}
