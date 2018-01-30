package com.zfhy.egold.domain.order.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.order.dto.MyorderDetailDto;
import com.zfhy.egold.domain.order.entity.Myorder;
import com.zfhy.egold.domain.order.entity.MyorderAttr;
import com.zfhy.egold.domain.order.entity.Order;

import java.util.List;



public interface MyorderService  extends Service<Myorder> {

    MyorderDetailDto getMyorderDetail(Integer id);

    void addMyorder(Myorder myorder, List<MyorderAttr> myorderAttrs);

    void addSaleGold(SoldDetail soldDetail);

    void addWithdraw(Withdraw withdraw);

    void addRecharge(Recharge recharge);

    void addGoldSwitch(FinancialProduct product, Order order);

    void expiredMyorder(String relateOrderId);

    void addBuyGold(FinancialProduct product, Order order);

    void finish(String relateOrderId);

    void addWithdrawGold(Sku sku, GoodsOrder goodsOrder, Double goldWeight);

    void addMallOrder(GoodsOrder goodsOrder, Sku sku);

}
