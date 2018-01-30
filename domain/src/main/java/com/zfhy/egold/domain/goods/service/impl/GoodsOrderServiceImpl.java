package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.constant.RedisConst;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.goods.dao.GoodsOrderMapper;
import com.zfhy.egold.domain.goods.dto.GoodsOrderDto;
import com.zfhy.egold.domain.goods.dto.SpuType;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.domain.goods.service.GoodsInvoiceService;
import com.zfhy.egold.domain.goods.service.GoodsOrderService;
import com.zfhy.egold.domain.goods.service.SkuService;
import com.zfhy.egold.domain.goods.service.SpuService;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.domain.member.service.AddressService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.order.dto.OrderStatus;
import com.zfhy.egold.domain.order.dto.PaymentDto;
import com.zfhy.egold.domain.order.dto.RechargeOrPay;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.payment.service.PaymentService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class GoodsOrderServiceImpl extends AbstractService<GoodsOrder> implements GoodsOrderService {
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DictService dictService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @Value("${withdrawGoldPay.callback.url}")
    private String withdrawGoldPayCallbackUrl;


    @Value("${mallPay.callback.url}")
    private String mallPayCallbackUrl;


    @Autowired
    private MyorderService myorderService;

    @Autowired
    private GoodsInvoiceService goodsInvoiceService;



    @Override

    public GoodsOrderDto submitOrder(Integer memberId, Integer skuId, Integer num, Double balanceAmount, Integer addressId, String dealPwd, String terminalId, Integer invoiceId) {
        MemberFund memberFund = this.memberFundService.findByMemberId(memberId);
        Sku sku = this.skuService.findById(skuId);

        Spu spu = this.spuService.findById(sku.getSpuId());


        Address address = this.addressService.findById(addressId);
        if (Objects.isNull(address)) {
            throw new BusException("您好，收货地址不存在");
        }


        
        this.checkOrder(memberFund, sku, num, balanceAmount, dealPwd);

        
        GoodsOrder goodsOrder = createOrder(memberId, num, balanceAmount, sku, spu, address);

        Double goldWeight = DoubleUtil.doubleMul(sku.getSpec(), new Double(num));

        
        this.myorderService.addWithdrawGold(sku, goodsOrder, goldWeight);

        
        this.memberFundService.withdrawGold(memberFund, goodsOrder);
        
        String payRequestNo = "";
        if (DoubleUtil.equal(goodsOrder.getShouldPay(), 0D)) {
            
            
            this.paymentService.paySuccess(goodsOrder);
            goodsOrder.setStatus(OrderStatus.PAY_SUCCESS.getStatus());

        } else {
            
            PaymentDto paymentDto = PaymentDto.builder()
                    .memberId(memberId)
                    .payAmount(goodsOrder.getShouldPay())
                    .paySn(goodsOrder.getOrderSn())
                    .productName(goodsOrder.getGoodsName())
                    .rechargeOrPay(RechargeOrPay.WITHDRAW_GOLD_PAY)
                    .build();
            payRequestNo = this.paymentService.payRequest(paymentDto, terminalId, withdrawGoldPayCallbackUrl);

        }

        GoodsOrderDto goodsOrderDto = new GoodsOrderDto().convertFrom(goodsOrder);
        goodsOrderDto.setPayRequestNo(payRequestNo);


        
        this.goodsInvoiceService.relateOrder(goodsOrder, invoiceId, SpuType.WITHDRAW_GOLD);



        return goodsOrderDto;
    }

    @Override
    public void updatePaySuccessStatus(Integer id) {
        int count = this.goodsOrderMapper.paySuccess(id);
        if (count != 1) {
            throw new BusException("您好，操作失败，请重试");
        }
    }

    @Override
    public GoodsOrder findByOrderSn(String orderSn) {
        return this.findBy("orderSn", orderSn);

    }

    @Override
    public void payFail(GoodsOrder goodsOrder) {
        int count = this.goodsOrderMapper.payFail(goodsOrder.getId(), OrderStatus.PAY_FAIL.getStatus());
        if (count != 1) {
            throw new BusException("订单状态已发生改变，请不要重复回滚");
        }
    }

    @Override
    public void updateStatusByOrderSn(String orderSn, OrderStatus newStatus, OrderStatus oldStatus) {
        this.goodsOrderMapper.updateStatusByOrderSn(orderSn, newStatus.getStatus(), oldStatus.getStatus());
    }

    @Override
    public List<GoodsOrder> findExpired(Integer payExpiredSec) {
        return this.goodsOrderMapper.findExpired(payExpiredSec);
    }

    @Override
    public void expiredOrder(GoodsOrder goodsOrder) {
        
        this.updateExpiredStatus(goodsOrder.getId(), goodsOrder.getVersion(), OrderStatus.EXPIRED_PAY.getStatus());

        
        this.myorderService.expiredMyorder(String.format("gorder_%d", goodsOrder.getId()));

        
        this.memberFundService.rollbackWithdrawGold(goodsOrder);

    }

    @Override
    public GoodsOrderDto submitMallOrder(Integer memberId, Integer skuId, Integer num, Double balanceAmount, Integer addressId, String dealPwd, String terminalId, Integer invoiceId) {
        MemberFund memberFund = this.memberFundService.findByMemberId(memberId);
        Sku sku = this.skuService.findById(skuId);

        Spu spu = this.spuService.findById(sku.getSpuId());


        Address address = this.addressService.findById(addressId);
        if (Objects.isNull(address)) {
            throw new BusException("您好，收货地址不存在");
        }


        Double expressFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE);
        Double insuranceFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE);

        Double orderAmount = DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(expressFee, insuranceFee), DoubleUtil.doubleMul(sku.getPrice(), new Double(num)), 2);

        if (orderAmount < balanceAmount) {
            balanceAmount = orderAmount;
        }

        
        this.checkMallOrder(memberFund, sku, num, balanceAmount, dealPwd);

        
        GoodsOrder goodsOrder = createMallOrder(memberId, num, balanceAmount, sku, spu, address);


        
        this.myorderService.addMallOrder(goodsOrder, sku);

        
        if (DoubleUtil.notEqual(balanceAmount, 0D)) {
            this.memberFundService.subCashBalance(memberId, memberFund.getVersion(), balanceAmount);
        }
        
        String payRequestNo = "";
        if (DoubleUtil.equal(goodsOrder.getShouldPay(), 0D)) {
            
            
            this.paymentService.paySuccess(goodsOrder);
            goodsOrder.setStatus(OrderStatus.PAY_SUCCESS.getStatus());

        } else {
            
            PaymentDto paymentDto = PaymentDto.builder()
                    .memberId(memberId)
                    .payAmount(goodsOrder.getShouldPay())
                    .paySn(goodsOrder.getOrderSn())
                    .productName(goodsOrder.getGoodsName())
                    .rechargeOrPay(RechargeOrPay.MALL_PAY)
                    .build();
            payRequestNo = this.paymentService.payRequest(paymentDto, terminalId, mallPayCallbackUrl);

        }

        GoodsOrderDto goodsOrderDto = new GoodsOrderDto().convertFrom(goodsOrder);
        goodsOrderDto.setPayRequestNo(payRequestNo);


        
        this.goodsInvoiceService.relateOrder(goodsOrder, invoiceId, SpuType.WITHDRAW_GOLD);
        return goodsOrderDto;
    }

    @Override
    public List<GoodsOrder> list(Map<String, String> params) {
        return this.goodsOrderMapper.list(params);
    }

    private GoodsOrder createMallOrder(Integer memberId, Integer num, Double balanceAmount, Sku sku, Spu spu, Address address) {
        Double expressFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE);
        Double insuranceFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE);


        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setUpdateDate(new Date());
        goodsOrder.setCreateDate(new Date());
        goodsOrder.setDelFlag("0");
        goodsOrder.setAddress(String.join("", StringUtils.trimToEmpty(address.getProvince())
                , StringUtils.trimToEmpty(address.getCity()), StringUtils.trimToEmpty(address.getCountry())
                , StringUtils.trimToEmpty(address.getTown()), StringUtils.trimToEmpty(address.getAddress())));

        goodsOrder.setAddressId(address.getId());
        goodsOrder.setAddressReceiver(address.getReceiver());
        goodsOrder.setAddressReceiverMobile(address.getReceiverTel());
        goodsOrder.setExpressFee(expressFee);
        goodsOrder.setInsuranceFee(insuranceFee);
        goodsOrder.setManMadeFee(0D);
        goodsOrder.setGoodsName(spu.getGoodsName());
        goodsOrder.setGoodsNumber(num);
        goodsOrder.setMemberId(memberId);
        goodsOrder.setOrderAmount(DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(expressFee, insuranceFee), DoubleUtil.doubleMul(sku.getPrice(), new Double(num)), 2));
        goodsOrder.setBalanceAmount(balanceAmount);
        goodsOrder.setShouldPay(DoubleUtil.doubleSub(goodsOrder.getOrderAmount(), balanceAmount, 2));
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long number = this.redisService.incr(String.format(RedisConst.GOOD_ORDER_SN_SERIAL, dateStr), 1L, 86400L);
        goodsOrder.setOrderSn(String.format("m%s%06d", dateStr, number));
        goodsOrder.setPrice(sku.getPrice());
        goodsOrder.setVersion(1);
        goodsOrder.setSkuId(sku.getId());
        goodsOrder.setSupId(sku.getSpuId());
        goodsOrder.setStatus(OrderStatus.CONFIRMED_PAY.getStatus());
        goodsOrder.setGoldWeight(DoubleUtil.doubleMul(sku.getSpec(), new Double(num), 3));

        this.save(goodsOrder);
        return goodsOrder;

    }

    private void checkMallOrder(MemberFund memberFund, Sku sku, Integer num, Double balanceAmount, String dealPwd) {
        
        this.memberService.checkDealPwd(memberFund.getMemberId(), dealPwd);
        
        if (sku.getStock() < num) {
            throw new BusException("您好，库存不足，请联系客服");
        }

        if (DoubleUtil.notEqual(balanceAmount, 0D)) {
            if (memberFund.getCashBalance() < balanceAmount) {
                throw new BusException("您好，你的余额不足");
            }
        }

    }

    private void updateExpiredStatus(Integer id, Integer version, int status) {
        int count = this.goodsOrderMapper.updateExpiredStatus(id, version, status);
        if (count != 1) {
            throw new BusException("订单已发生变化，设置过期失账");
        }
    }

    public GoodsOrder createOrder(Integer memberId, Integer num, Double balanceAmount, Sku sku, Spu spu, Address address) {


        Double expressFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE);
        Double insuranceFee = this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE);
        Double manMadeFee = DoubleUtil.doubleMul(DoubleUtil.doubleMul(sku.getFee(), new Double(num)), sku.getSpec(), 2);


        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setUpdateDate(new Date());
        goodsOrder.setCreateDate(new Date());
        goodsOrder.setDelFlag("0");
        goodsOrder.setAddress(String.join("", StringUtils.trimToEmpty(address.getProvince())
                , StringUtils.trimToEmpty(address.getCity()), StringUtils.trimToEmpty(address.getCountry())
                , StringUtils.trimToEmpty(address.getTown()), StringUtils.trimToEmpty(address.getAddress())));

        goodsOrder.setAddressId(address.getId());
        goodsOrder.setAddressReceiver(address.getReceiver());
        goodsOrder.setAddressReceiverMobile(address.getReceiverTel());
        goodsOrder.setExpressFee(expressFee);
        goodsOrder.setInsuranceFee(insuranceFee);
        goodsOrder.setManMadeFee(manMadeFee);
        goodsOrder.setGoodsName(spu.getGoodsName());
        goodsOrder.setGoodsNumber(num);
        goodsOrder.setMemberId(memberId);
        goodsOrder.setOrderAmount(DoubleUtil.doubleAdd(DoubleUtil.doubleAdd(expressFee, insuranceFee), manMadeFee, 2));
        goodsOrder.setBalanceAmount(balanceAmount);
        goodsOrder.setShouldPay(DoubleUtil.doubleSub(goodsOrder.getOrderAmount(), balanceAmount, 2));
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long number = this.redisService.incr(String.format(RedisConst.GOOD_ORDER_SN_SERIAL, dateStr), 1L, 86400L);
        goodsOrder.setOrderSn(String.format("G%s%06d", dateStr, number));
        goodsOrder.setPrice(this.redisService.getRealTimePrice());
        goodsOrder.setVersion(1);
        goodsOrder.setSkuId(sku.getId());
        goodsOrder.setSupId(sku.getSpuId());
        goodsOrder.setStatus(OrderStatus.CONFIRMED_PAY.getStatus());
        goodsOrder.setGoldWeight(DoubleUtil.doubleMul(sku.getSpec(), new Double(num), 3));

        this.save(goodsOrder);
        return goodsOrder;
    }

    private void checkOrder(MemberFund memberFund, Sku sku, Integer num, Double balanceAmount, String dealPayPwd) {

        
        this.memberService.checkDealPwd(memberFund.getMemberId(), dealPayPwd);
        
        if (sku.getStock() < num) {
            throw new BusException("您好，库存不足，请联系客服");
        }


        
        Double orderGoldWeight = DoubleUtil.doubleMul(sku.getSpec(), new Double(num));

        if (memberFund.getCurrentGoldBalance() < orderGoldWeight) {
            throw new BusException("您好，你的活期金余额不足");
        }

        if (DoubleUtil.notEqual(balanceAmount, 0D)) {
            if (memberFund.getCashBalance() < balanceAmount) {
                throw new BusException("您好，你的余额不足");
            }
        }

    }
}
