package com.zfhy.egold.domain.order.service.impl;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.order.dao.MyorderMapper;
import com.zfhy.egold.domain.order.dto.MyorderAttrDto;
import com.zfhy.egold.domain.order.dto.MyorderDetailDto;
import com.zfhy.egold.domain.order.dto.MyorderStatus;
import com.zfhy.egold.domain.order.dto.MyorderType;
import com.zfhy.egold.domain.order.entity.Myorder;
import com.zfhy.egold.domain.order.entity.MyorderAttr;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.service.MyorderAttrService;
import com.zfhy.egold.domain.order.service.MyorderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zfhy.egold.common.util.DateUtil.afterDays;
import static com.zfhy.egold.common.util.DateUtil.YYYY_MM_DD;
import static com.zfhy.egold.common.util.DateUtil.tomorrow;



@Service
@Transactional
@Slf4j
public class MyorderServiceImpl extends AbstractService<Myorder> implements MyorderService{
    @Autowired
    private MyorderMapper myorderMapper;

    @Autowired
    private MyorderAttrService myorderAttrService;


    @Override
    public MyorderDetailDto getMyorderDetail(Integer id) {

        Myorder myorder = this.findById(id);

        if (Objects.isNull(myorder)) {
            throw new BusException("我的订单不存在");
        }

        MyorderDetailDto myorderDetailDto = new MyorderDetailDto().convertFrom(myorder);


        List<MyorderAttr> myorderAttrs = this.myorderAttrService.findByMyorderId(id);
        List<MyorderAttrDto> myorderAttrDtoList = myorderAttrs.stream().map(e -> new MyorderAttrDto().convertFrom(e)).collect(Collectors.toList());


        myorderDetailDto.setMyorderAttrDtos(myorderAttrDtoList);
        return myorderDetailDto;
    }

    @Override
    public void addMyorder(Myorder myorder, List<MyorderAttr> myorderAttrs) {
        this.save(myorder);

        this.myorderAttrService.batchInsert(myorder.getId(), myorderAttrs);

    }

    @Override
    public void addSaleGold(SoldDetail soldDetail) {
        Myorder myorder = Myorder.builder().memberId(soldDetail.getMemberId())
                .productName(soldDetail.getProductName())
                .myorderType(MyorderType.SALE_GOLD.getCode())

                .createDate(new Date())
                .currentPrice(soldDetail.getPrice())
                .delFlag("0")
                .goldWeight(soldDetail.getGoldWeight())
                .myorderStatus(MyorderStatus.FINISH.getCode())
                .orderTotalAmount(soldDetail.getTotalAmount())
                .relateOrderId(String.format("sold_%d", soldDetail.getId()))
                .build();

        MyorderAttr totalAmountAttr = genMyorderAttr(1, "订单总额", String.format("￥%s", DoubleUtil.toString(soldDetail.getTotalAmount())));
        MyorderAttr feeAttr = genMyorderAttr(2, "手续费", String.format("￥%s", DoubleUtil.toString(soldDetail.getFee())));
        MyorderAttr incomeAttr = genMyorderAttr(4, "实际收入", String.format("￥%s", DoubleUtil.toString(soldDetail.getIncomeAmount())));

        List<MyorderAttr> myorderAttrs = Arrays.asList(totalAmountAttr, feeAttr, incomeAttr);


        this.addMyorder(myorder, myorderAttrs);
    }

    @Override
    public void addWithdraw(Withdraw withdraw) {

        Myorder myorder = Myorder.builder().memberId(withdraw.getMemberId())
                .productName("提现")
                .myorderType(MyorderType.WITHDRAW.getCode())

                .createDate(new Date())

                .delFlag("0")

                .myorderStatus(MyorderStatus.FINISH.getCode())
                .orderTotalAmount(withdraw.getWithdrawAmount())
                .relateOrderId(String.format("withdraw_%d", withdraw.getId()))
                .build();

        MyorderAttr totalAmountAttr = genMyorderAttr(1, "订单总额", String.format("￥%s", DoubleUtil.toString(withdraw.getWithdrawAmount())));
        MyorderAttr feeAttr = genMyorderAttr(2, "手续费", String.format("￥%s", DoubleUtil.toString(withdraw.getWithdrawFee())));
        MyorderAttr incomeAttr = genMyorderAttr(4, "提现金额", String.format("￥%s", DoubleUtil.toString(withdraw.getPayAmount())));

        List<MyorderAttr> myorderAttrs = Arrays.asList(totalAmountAttr, feeAttr, incomeAttr);


        this.addMyorder(myorder, myorderAttrs);

    }

    @Override
    public void addRecharge(Recharge recharge) {
        Myorder myorder = Myorder.builder().memberId(recharge.getMemberId())
                .productName("充值")
                .myorderType(MyorderType.RECHARGE.getCode())

                .createDate(new Date())

                .delFlag("0")

                .myorderStatus(MyorderStatus.PROCESSING.getCode())
                .orderTotalAmount(recharge.getRechargeAmount())
                .relateOrderId(String.format("recharge_%d", recharge.getId()))
                .build();

        MyorderAttr totalAmountAttr = genMyorderAttr(1, "订单总额", String.format("￥%s", DoubleUtil.toString(recharge.getRechargeAmount())));
        MyorderAttr feeAttr = genMyorderAttr(2, "手续费", String.format("￥%s", "0.00"));

        List<MyorderAttr> myorderAttrs = Arrays.asList(totalAmountAttr, feeAttr);


        this.addMyorder(myorder, myorderAttrs);
    }

    @Override
    public void addGoldSwitch(FinancialProduct product, Order order) {
        Myorder myorder = Myorder.builder().memberId(order.getMemberId())
                .productName("流动金转定期金")
                .myorderType(MyorderType.GOLD_SWITCH.getCode())
                .annualRate(product.getAnnualRate())
                .createDate(new Date())
                .currentPrice(order.getCurrentGoldPrice())
                .delFlag("0")
                .goldWeight(order.getGoldWeight())
                .myorderStatus(MyorderStatus.PROCESSING.getCode())
                .orderTotalAmount(order.getTotalAmount())
                .relateOrderId(String.format("order_%d", order.getId()))
                .build();

        MyorderAttr totalAmountAttr = genMyorderAttr(1, "订单总额", String.format("￥%s", DoubleUtil.toString(order.getTotalAmount())));
        MyorderAttr cachCouponAttr = genMyorderAttr(2, "黄金重量", String.format("￥%s", DoubleUtil.toScal4(order.getGoldWeight())));


        List<MyorderAttr> myorderAttrs = Lists.newArrayList();
        myorderAttrs.addAll(Arrays.asList(totalAmountAttr, cachCouponAttr));

        if (Objects.equals(FinancialProductType.TERM_DEPOSIT.name(), order.getFinanceProductType())) {
            
            MyorderAttr baeinDateAttr = genMyorderAttr(6, "计息日", DateUtil.toString(tomorrow(), YYYY_MM_DD));
            MyorderAttr endDateAttr = genMyorderAttr(7, "到期日", DateUtil.toString(afterDays(tomorrow(), product.getTerm()), YYYY_MM_DD));

            myorderAttrs.add(baeinDateAttr);
            myorderAttrs.add(endDateAttr);

        }

        if (Objects.equals(FinancialProductType.RISE_FALL.name(), product.getProductType())) {
            
            myorder.setMyorderType(MyorderType.RISE_FALL.getCode());

            MyorderAttr baeinDateAttr = genMyorderAttr(6, "计息日", DateUtil.toString(product.getDeadLineDate(), YYYY_MM_DD));
            MyorderAttr endDateAttr = genMyorderAttr(7, "到期日", DateUtil.toString(afterDays(product.getDeadLineDate(), product.getTerm()), YYYY_MM_DD));



            myorderAttrs.add(baeinDateAttr);
            myorderAttrs.add(endDateAttr);
        }
        this.addMyorder(myorder, myorderAttrs);

    }

    @Override
    public void expiredMyorder(String relateOrderId) {

        Myorder myorder = this.findBy("relateOrderId", relateOrderId);
        if (Objects.isNull(myorder)) {
            return;
        }
        myorder.setMyorderStatus(MyorderStatus.EXPIRED.getCode());
        this.update(myorder);

    }

    @Override
    public void addBuyGold(FinancialProduct product, Order order) {
        String productName = order.getProductName();
        String productType = product.getProductType();
        if (Objects.equals(productType, FinancialProductType.CURRENT_DEPOSIT.name())) {
            
            Integer estimateFall = order.getEstimateFall();
            if (Objects.nonNull(estimateFall) && estimateFall == 1) {
                productName = productName + "买跌";
            } else {
                productName = productName + "买涨";
            }


        }
        Myorder myorder = Myorder.builder().memberId(order.getMemberId())
                .productName(productName)
                .myorderType(MyorderType.BUY_GOLD.getCode())
                .annualRate(product.getAnnualRate())
                .createDate(new Date())
                .currentPrice(order.getCurrentGoldPrice())
                .delFlag("0")
                .goldWeight(order.getGoldWeight())
                .myorderStatus(MyorderStatus.PROCESSING.getCode())
                .orderTotalAmount(order.getTotalAmount())
                .relateOrderId(String.format("order_%d", order.getId()))
                .estimateFall(order.getEstimateFall())
                .build();

        MyorderAttr totalAmountAttr = genMyorderAttr(1, "订单总额", String.format("￥%s", DoubleUtil.toString(order.getTotalAmount())));
        MyorderAttr cachCouponAttr = genMyorderAttr(2, "红包支付", String.format("-￥%s", DoubleUtil.toString(order.getCashCouponAmount())));
        MyorderAttr realPayAttr = genMyorderAttr(3, "实际支付", String.format("￥%s", DoubleUtil.toString(DoubleUtil.doubleAdd(order.getShouldPayAmount(), order.getCashCouponAmount()))));
        MyorderAttr balancePayAttr = genMyorderAttr(4, "余额支付", String.format("￥%s", DoubleUtil.toString(order.getBalancePayAmount())));
        MyorderAttr onLinePayAttr = genMyorderAttr(5, "在线支付", String.format("￥%s", DoubleUtil.toString(order.getShouldPayAmount())));

        List<MyorderAttr> myorderAttrs = Lists.newArrayList();
        myorderAttrs.addAll(Arrays.asList(totalAmountAttr, cachCouponAttr, realPayAttr, balancePayAttr, onLinePayAttr));

        if (Objects.equals(FinancialProductType.TERM_DEPOSIT.name(), product.getProductType())) {
            
            MyorderAttr beginDateAttr = genMyorderAttr(6, "计息日", DateUtil.toString(tomorrow(), YYYY_MM_DD));
            MyorderAttr endDateAttr = genMyorderAttr(7, "到期日", DateUtil.toString(afterDays(tomorrow(), product.getTerm()), YYYY_MM_DD));

            myorderAttrs.add(beginDateAttr);
            myorderAttrs.add(endDateAttr);

        }

        if (Objects.equals(FinancialProductType.RISE_FALL.name(), product.getProductType())) {
            
            myorder.setMyorderType(MyorderType.RISE_FALL.getCode());

            MyorderAttr beginDateAttr = genMyorderAttr(6, "计息日", DateUtil.toString(product.getDeadLineDate(), YYYY_MM_DD));
            MyorderAttr endDateAttr = genMyorderAttr(7, "到期日", DateUtil.toString(afterDays(product.getDeadLineDate(), product.getTerm()), YYYY_MM_DD));



            myorderAttrs.add(beginDateAttr);
            myorderAttrs.add(endDateAttr);
        }
        this.addMyorder(myorder, myorderAttrs);
    }

    @Override
    public void finish(String relateOrderId) {
        this.myorderMapper.finish(relateOrderId);
    }

    @Override
    public void addWithdrawGold(Sku sku, GoodsOrder goodsOrder, Double goldWeight) {
        Myorder myorder = Myorder.builder().memberId(goodsOrder.getMemberId())
                .productName(goodsOrder.getGoodsName())
                .myorderType(MyorderType.WITHDRAW_GOLD.getCode())
                .annualRate(0D)
                .createDate(new Date())
                .currentPrice(0D)
                .delFlag("0")
                .goldWeight(goldWeight)
                .myorderStatus(MyorderStatus.PROCESSING.getCode())
                .orderTotalAmount(goodsOrder.getOrderAmount())
                .relateOrderId(String.format("gorder_%d", goodsOrder.getId()))
                .withdrawNum(String.format("%s克X%d", sku.getSpec(), goodsOrder.getGoodsNumber()))
                .receiver(goodsOrder.getAddressReceiver())
                .receiverMobile(goodsOrder.getAddressReceiverMobile())
                .receiverAddress(goodsOrder.getAddress())
                .build();

        MyorderAttr manMadeFee = genMyorderAttr(1, "工费", String.format("￥%s", DoubleUtil.toString(goodsOrder.getManMadeFee())));
        MyorderAttr expressFee = genMyorderAttr(2, "快递费", String.format("￥%s", DoubleUtil.toString(goodsOrder.getExpressFee())));
        MyorderAttr insuranceFee = genMyorderAttr(3, "物流保险", String.format("￥%s", DoubleUtil.toString(goodsOrder.getInsuranceFee())));
        MyorderAttr realPay = genMyorderAttr(4, "实际支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getBalanceAmount()+goodsOrder.getShouldPay())));
        MyorderAttr balancePay = genMyorderAttr(5, "余额支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getBalanceAmount())));
        MyorderAttr shouldPay = genMyorderAttr(6, "在线支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getShouldPay())));

        List<MyorderAttr> myorderAttrs = Arrays.asList(manMadeFee, expressFee, insuranceFee, realPay, balancePay, shouldPay);


        this.addMyorder(myorder, myorderAttrs);



    }

    @Override
    public void addMallOrder(GoodsOrder goodsOrder, Sku sku) {

        Myorder myorder = Myorder.builder().memberId(goodsOrder.getMemberId())
                .productName(goodsOrder.getGoodsName())
                .myorderType(MyorderType.GOLD_MALL.getCode())
                .annualRate(0D)
                .createDate(new Date())
                .currentPrice(0D)
                .delFlag("0")
                .goldWeight(goodsOrder.getGoldWeight())
                .myorderStatus(MyorderStatus.PROCESSING.getCode())
                .orderTotalAmount(goodsOrder.getOrderAmount())
                .relateOrderId(String.format("morder_%d", goodsOrder.getId()))
                .withdrawNum(String.format("%s克X%d", sku.getSpec(), goodsOrder.getGoodsNumber()))
                .receiver(goodsOrder.getAddressReceiver())
                .receiverMobile(goodsOrder.getAddressReceiverMobile())
                .receiverAddress(goodsOrder.getAddress())
                .build();


        MyorderAttr expressFee = genMyorderAttr(2, "快递费", String.format("￥%s", DoubleUtil.toString(goodsOrder.getExpressFee())));
        MyorderAttr insuranceFee = genMyorderAttr(3, "物流保险", String.format("￥%s", DoubleUtil.toString(goodsOrder.getInsuranceFee())));
        MyorderAttr realPay = genMyorderAttr(4, "实际支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getBalanceAmount()+goodsOrder.getShouldPay())));
        MyorderAttr balancePay = genMyorderAttr(5, "余额支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getBalanceAmount())));
        MyorderAttr shouldPay = genMyorderAttr(6, "在线支付", String.format("￥%s", DoubleUtil.toString(goodsOrder.getShouldPay())));

        List<MyorderAttr> myorderAttrs = Arrays.asList(expressFee, insuranceFee, realPay, balancePay, shouldPay);


        this.addMyorder(myorder, myorderAttrs);

    }

    public MyorderAttr genMyorderAttr(int sort, String attrName, String attrValue) {
        return MyorderAttr.builder()
                .attrName(attrName)
                .attrValue(attrValue)
                .sort(sort)
                .build();
    }
}
