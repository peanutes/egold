package com.zfhy.egold.domain.fund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyInvestDto {

    
    private String productId;

    
    private String productName;

    
    private Double investAmount;

    
    private Double income;


}
