package com.zfhy.egold.domain.goods.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsOrderDto {

    
    private Integer memberId;
    

    
    private String orderSn;
    

    
    private Integer supId;
    

    
    private Integer skuId;
    

    
    private String goodsName;
    

    
    private Integer goodsNumber;
    

    
    private Double orderAmount;
    

    
    private Double price;
    

    
    private Double expressFee;
    

    
    private Double insuranceFee;
    

    
    private Double balanceAmount;
    

    
    private Double shouldPay;
    

    
    private Integer addressId;
    

    
    private String address;
    

    
    private Integer status;

    
    private String payRequestNo;

    

   /* 
    private Integer version;
    

    
    private String remarks;*/
    



    
    public GoodsOrder convertTo() {
        return  new GoodsOrderDtoConvert().convert(this);
    }

    
    public GoodsOrderDto convertFrom(GoodsOrder goodsOrder) {
        return new GoodsOrderDtoConvert().reverse().convert(goodsOrder);

    }


    private static class GoodsOrderDtoConvert extends  Converter<GoodsOrderDto, GoodsOrder> {


        @Override
        protected GoodsOrder doForward(GoodsOrderDto goodsOrderDto) {
            GoodsOrder goodsOrder = new GoodsOrder();
            BeanUtils.copyProperties(goodsOrderDto, goodsOrder);
            return goodsOrder;
        }

        @Override
        protected GoodsOrderDto doBackward(GoodsOrder goodsOrder) {
            GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
            BeanUtils.copyProperties(goodsOrder, goodsOrderDto);
            return goodsOrderDto;
        }
    }
}
