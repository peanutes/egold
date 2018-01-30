package com.zfhy.egold.domain.gold.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceSerialDto {


    
    private List<String> times;


    
    private List<Double> prices;


    
    private Double maxPrice;

    
    private Double minPrice;




}
