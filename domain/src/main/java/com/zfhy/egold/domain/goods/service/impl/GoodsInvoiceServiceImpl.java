package com.zfhy.egold.domain.goods.service.impl;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.goods.dao.GoodsInvoiceMapper;
import com.zfhy.egold.domain.goods.dto.InvoiceType;
import com.zfhy.egold.domain.goods.dto.SpuType;
import com.zfhy.egold.domain.goods.entity.GoodsInvoice;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zfhy.egold.domain.goods.service.GoodsInvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;



@Service
@Transactional
@Slf4j
public class GoodsInvoiceServiceImpl extends AbstractService<GoodsInvoice> implements GoodsInvoiceService{
    @Autowired
    private GoodsInvoiceMapper goodsInvoiceMapper;

    @Override
    public GoodsInvoice submitInvoice(Integer memberId, Double invoiceAmount, String invoiceItem, InvoiceType invoiceType, String invoiceTitle, String taxNo) {

        GoodsInvoice goodsInvoice = new GoodsInvoice();
        goodsInvoice.setRemarks("");
        goodsInvoice.setDelFlag("0");
        goodsInvoice.setOrderSn("");
        goodsInvoice.setCreateDate(new Date());
        goodsInvoice.setInvoiceAmount(invoiceAmount);
        goodsInvoice.setInvoiceItem(invoiceItem);
        goodsInvoice.setInvoiceTitle(invoiceTitle);
        goodsInvoice.setInvoiceType(invoiceType.getCode());
        goodsInvoice.setTaxNo(taxNo);
        goodsInvoice.setMemberId(memberId);

        this.save(goodsInvoice);
        return goodsInvoice;
    }

    @Override
    public void relateOrder(GoodsOrder goodsOrder, Integer invoiceId, SpuType spuType) {
        if (Objects.isNull(invoiceId)) {
            return;
        }
        GoodsInvoice invoice = this.findById(invoiceId);

        if (Objects.isNull(invoice)) {
            return;
        }

        if (!Objects.equals(invoice.getMemberId(), goodsOrder.getMemberId())) {
            throw new BusException("发票信息不正确");
        }

        invoice.setOrderId(goodsOrder.getId());
        invoice.setOrderSn(goodsOrder.getOrderSn());

        Double invoiceAmount = 0D;

        if (SpuType.WITHDRAW_GOLD.equals(spuType)) {

            invoiceAmount = DoubleUtil.doubleMul(goodsOrder.getGoldWeight(), goodsOrder.getPrice());

        } else {
            invoiceAmount = DoubleUtil.doubleMul(goodsOrder.getPrice(), new Double(goodsOrder.getGoodsNumber()));

        }

        invoice.setInvoiceAmount(invoiceAmount);

        this.update(invoice);

    }
}
