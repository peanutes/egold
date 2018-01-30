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
public class AvgBuyPriceDto {
    
    private Double historyBalance;
    
    private Double currentGoldAvgPrice;
    
    private Double termGoldAvgPrice;
}
