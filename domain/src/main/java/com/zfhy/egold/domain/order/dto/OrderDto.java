package com.zfhy.egold.domain.order.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.entity.TempOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {


    
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
    

    
    private Integer version;
    

    
    private String remarks;

    
    private Boolean estimateFall;
    



    
    public Order convertTo() {
        return  new OrderDtoConvert().convert(this);
    }

    
    public OrderDto convertFrom(Order order) {
        return new OrderDtoConvert().reverse().convert(order);

    }

    public OrderDto convertFromTempOrder(TempOrder tempOrder) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(tempOrder, orderDto);
        return orderDto;
    }


    private static class OrderDtoConvert extends  Converter<OrderDto, Order> {


        @Override
        protected Order doForward(OrderDto orderDto) {
            Order order = new Order();
            BeanUtils.copyProperties(orderDto, order);
            return order;
        }

        @Override
        protected OrderDto doBackward(Order order) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);

            Integer estimateFall = order.getEstimateFall();
            if (Objects.isNull(estimateFall) || estimateFall == 0) {
                orderDto.estimateFall = false;

            } else {
                orderDto.estimateFall = true;
            }
            return orderDto;
        }
    }
}
