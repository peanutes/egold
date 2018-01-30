package com.zfhy.egold.domain.fund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoldAssetsDto {

    
    private String totalGold;

    
    private String goldAmount;

    
    private String historyBalance;

    
    private String currentGold;

    
    private String currentGoldAvgPrice;


    
    private String termGold;


    
    private String termGoldAvgPrice;


}
