package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSecurityAgreementDto {
    
    private String title;
    
    private String url;
}
