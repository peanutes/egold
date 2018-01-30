package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.dto.InvestRecordStatus;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;


@Data
@Builder
public class TermGoldTradeRecordDto {

    
    private String productName;
    
    private String operateDateStr;
    
    private String goldWeight;
    
    private String annualRate;

    
    private String status;


    public static TermGoldTradeRecordDto convertFrom(InvestRecord investRecord) {
        return TermGoldTradeRecordDto.builder()
                .productName(investRecord.getProductName())
                .goldWeight(String.format("%s克", DoubleUtil.toScal4(investRecord.getInvestGoldWeight())))
                .operateDateStr(DateUtil.toString(investRecord.getCreateDate(), DateUtil.YYYY_MM_DD))
                .annualRate(String.format("%s%%", DoubleUtil.toString(DoubleUtil.doubleAdd(investRecord.getAnnualRate(), investRecord.getAdditionalRate()))))
                .status(Objects.equals(InvestRecordStatus.HAD_REPAY.getCode(), investRecord.getStatus()) ? "已到期" : "在投")
                .build();

    }
}
