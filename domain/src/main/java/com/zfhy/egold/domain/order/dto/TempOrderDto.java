package com.zfhy.egold.domain.order.dto;

import com.zfhy.egold.domain.order.entity.TempOrder;
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
public class TempOrderDto {


    
    private Integer memberId;
    

    
    private Integer productId;
    

    
    private String orderSn;
    

    
    private Double goldWeight;
    

    
    private Integer payType;
    

    
    private Double totalAmount;
    

    
    private Double balancePayAmount;
    

    
    private Integer productType;
    

    
    private String financeProductType;
    

    
    private String productName;
    

    
    private Double currentGoldPrice;
    

    
    private Double shouldPayAmount;
    

    
    private Integer status;
    

    
    private Integer cashCouponId;
    

    
    private Integer discountCouponId;
    

    
    private Double cashCouponAmount;
    

    
    private Double discountCouponRate;
    

    
    private Integer switchInto;
    

    
    private Date requestPayTime;
    

    
    private Integer version;
    

    
    private String remarks;
    



    
    public TempOrder convertTo() {
        return  new TempOrderDtoConvert().convert(this);
    }

    
    public TempOrderDto convertFrom(TempOrder tempOrder) {
        return new TempOrderDtoConvert().reverse().convert(tempOrder);

    }


    private static class TempOrderDtoConvert extends  Converter<TempOrderDto, TempOrder> {


        @Override
        protected TempOrder doForward(TempOrderDto tempOrderDto) {
            TempOrder tempOrder = new TempOrder();
            BeanUtils.copyProperties(tempOrderDto, tempOrder);
            return tempOrder;
        }

        @Override
        protected TempOrderDto doBackward(TempOrder tempOrder) {
            TempOrderDto tempOrderDto = new TempOrderDto();
            BeanUtils.copyProperties(tempOrder, tempOrderDto);
            return tempOrderDto;
        }
    }
}
