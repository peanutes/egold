package com.zfhy.egold.domain.order.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.order.entity.Myorder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;


@Data
public class MyorderDetailDto {


    
    private Integer id;

    
    private Integer memberId;


    
    private Integer myorderType;


    
    private Integer myorderStatus;


    
    private String productName;


    
    private Double annualRate;


    
    private Double orderTotalAmount;


    
    private Double currentPrice;


    
    private Double goldWeight;


    
    private String remarks;



    
    private Date createDate;

    
    private List<MyorderAttrDto> myorderAttrDtos;


    
    private String withdrawNum;



    
    private String receiver;

    
    private String receiverMobile;


    
    private String receiverAddress;

    
    private Integer estimateFall;






    
    public Myorder convertTo() {
        return  new OrderDetailDtoConvert().convert(this);
    }

    
    public MyorderDetailDto convertFrom(Myorder order) {
        return new OrderDetailDtoConvert().reverse().convert(order);

    }


    private static class OrderDetailDtoConvert extends Converter<MyorderDetailDto, Myorder> {


        @Override
        protected Myorder doForward(MyorderDetailDto orderDto) {
            Myorder order = new Myorder();
            BeanUtils.copyProperties(orderDto, order);
            return order;
        }

        @Override
        protected MyorderDetailDto doBackward(Myorder order) {
            MyorderDetailDto orderDto = new MyorderDetailDto();
            BeanUtils.copyProperties(order, orderDto);
            return orderDto;
        }
    }




}
