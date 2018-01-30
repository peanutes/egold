package com.zfhy.egold.domain.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PaymentDto {

    
    private Integer memberId;

    
    private Double payAmount;

    
    private String paySn;

    
    private String productName;

    
    private RechargeOrPay rechargeOrPay;


}
