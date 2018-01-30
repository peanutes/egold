package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RechargeDetailDto {
    
    private String rechargeDate;
    
    private Double amount;

    public static RechargeDetailDto convertFrom(FundRecord fundRecord) {
        return RechargeDetailDto.builder()
                .amount(fundRecord.getOperateMoney())
                .rechargeDate(DateUtil.toString(fundRecord.getCreateDate(), DateUtil.YYYY_MM_DD))
                .build();

    }
}
