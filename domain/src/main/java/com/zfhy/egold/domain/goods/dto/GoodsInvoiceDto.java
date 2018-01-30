package com.zfhy.egold.domain.goods.dto;

import com.zfhy.egold.domain.goods.entity.GoodsInvoice;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInvoiceDto {


    
    private Integer orderId;
    

    
    private String orderSn;
    

    
    private Double invoiceAmount;
    

    
    private String invoiceItem;
    

    
    private String invoiceTitle;
    

    
    private Integer invoiceType;
    

    
    private String taxNo;
    

    
    private String remarks;
    



    
    public GoodsInvoice convertTo() {
        return  new GoodsInvoiceDtoConvert().convert(this);
    }

    
    public GoodsInvoiceDto convertFrom(GoodsInvoice goodsInvoice) {
        return new GoodsInvoiceDtoConvert().reverse().convert(goodsInvoice);

    }


    private static class GoodsInvoiceDtoConvert extends  Converter<GoodsInvoiceDto, GoodsInvoice> {


        @Override
        protected GoodsInvoice doForward(GoodsInvoiceDto goodsInvoiceDto) {
            GoodsInvoice goodsInvoice = new GoodsInvoice();
            BeanUtils.copyProperties(goodsInvoiceDto, goodsInvoice);
            return goodsInvoice;
        }

        @Override
        protected GoodsInvoiceDto doBackward(GoodsInvoice goodsInvoice) {
            GoodsInvoiceDto goodsInvoiceDto = new GoodsInvoiceDto();
            BeanUtils.copyProperties(goodsInvoice, goodsInvoiceDto);
            return goodsInvoiceDto;
        }
    }
}
