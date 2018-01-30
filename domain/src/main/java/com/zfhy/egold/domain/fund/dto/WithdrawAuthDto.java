package com.zfhy.egold.domain.fund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class WithdrawAuthDto {

    
    private Integer id;

    
    private String mobile;

    
    private String realName;

    
    private String withdrawAccount;

    
    private Double withdrawAmount;


    
    private Double payAmount;


    
    private Double withdrawFee;

    
    private String status;



}
