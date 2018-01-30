package com.zfhy.egold.domain.fund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MyInvestSummaryDto {
    
    private Double onInvestAmount;

    
    private Double sumInvestAmount;

    
    private Double sumIncome;

}
