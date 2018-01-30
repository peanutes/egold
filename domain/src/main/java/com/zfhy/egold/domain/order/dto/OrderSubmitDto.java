package com.zfhy.egold.domain.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitDto {

    @NotNull
    
    private Integer memberId;

    @NotNull
    
    private Integer productId;

    @NotNull
    
    private Double totalAmount;

    
    private Integer cashCouponId;

    /*@NotNull
    
    private Integer payType;*/

    
    private Integer discountCouponId;

    
    private Double balancePayAmount;

    @NotNull
    
    private Integer productType;

    @NotBlank
    
    private String productName;

    
    private Boolean estimateFall;




}
