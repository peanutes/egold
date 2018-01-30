package com.zfhy.egold.domain.goods.service;

import com.zfhy.egold.domain.goods.dto.InvoiceType;
import com.zfhy.egold.domain.goods.dto.SpuType;
import com.zfhy.egold.domain.goods.entity.GoodsInvoice;
import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;



public interface GoodsInvoiceService  extends Service<GoodsInvoice> {

    GoodsInvoice submitInvoice(Integer memberId, Double invoiceAmount, String invoiceItem, InvoiceType invoiceType, String invoiceTitle, String taxNo);

    void relateOrder(GoodsOrder goodsOrder, Integer invoiceId, SpuType spuType);
}
