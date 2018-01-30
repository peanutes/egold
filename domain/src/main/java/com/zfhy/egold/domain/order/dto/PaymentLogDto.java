package com.zfhy.egold.domain.order.dto;

import com.zfhy.egold.domain.order.entity.PaymentLog;
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
public class PaymentLogDto {


    
    private Integer id;
    

    
    private Integer memberId;
    

    
    private String paySn;
    

    
    private String payRequestNo;
    

    
    private Double amount;
    

    
    private Integer rechargeOrPay;
    

    
    private Integer status;
    

    
    private String errroMsg;
    

    
    private String remarks;
    



    
    public PaymentLog convertTo() {
        return  new PaymentLogDtoConvert().convert(this);
    }

    
    public PaymentLogDto convertFrom(PaymentLog paymentLog) {
        return new PaymentLogDtoConvert().reverse().convert(paymentLog);

    }


    private static class PaymentLogDtoConvert extends  Converter<PaymentLogDto, PaymentLog> {


        @Override
        protected PaymentLog doForward(PaymentLogDto paymentLogDto) {
            PaymentLog paymentLog = new PaymentLog();
            BeanUtils.copyProperties(paymentLogDto, paymentLog);
            return paymentLog;
        }

        @Override
        protected PaymentLogDto doBackward(PaymentLog paymentLog) {
            PaymentLogDto paymentLogDto = new PaymentLogDto();
            BeanUtils.copyProperties(paymentLog, paymentLogDto);
            return paymentLogDto;
        }
    }
}
