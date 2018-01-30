package com.zfhy.egold.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BankDto {
    
    private String bankCode;
    
    private String bankName;
    
    private String bankLogo;

}
