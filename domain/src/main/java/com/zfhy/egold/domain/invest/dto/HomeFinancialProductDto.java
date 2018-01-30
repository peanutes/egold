package com.zfhy.egold.domain.invest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeFinancialProductDto {

    
    FinancialProductDto currentDepositProduct;

    
    FinancialProductDto newUserProduct;

    
    FinancialProductDto termDepositProduct;


    
    FinancialProductDto riseAndFallProduct;


    
    ProductDescDto productDescDto;


    
    AccountSecurityAgreementDto accountSecurityAgreementDto;

    
    private int hadInvestNewUserCount;

}
