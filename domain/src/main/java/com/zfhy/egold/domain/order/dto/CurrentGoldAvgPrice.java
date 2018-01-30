package com.zfhy.egold.domain.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentGoldAvgPrice {

    
    private Double estimateRisePrice;
    
    private Double estimateFallPrice;


}
