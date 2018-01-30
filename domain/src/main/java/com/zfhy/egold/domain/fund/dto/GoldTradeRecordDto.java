package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;


@Data
@Builder
public class GoldTradeRecordDto {

    
    private String operation;
    
    private String operateDateStr;
    
    private String goldWeight;
    
    private String goldPrice;


    public static GoldTradeRecordDto convertFrom(FundRecord fundRecord, Double realPrice) {
        Double investPrice = fundRecord.getInvestPrice();
        if (Objects.isNull(investPrice) || DoubleUtil.equal(investPrice, 0d)) {
            investPrice = realPrice;
        }
        return GoldTradeRecordDto.builder()
                .goldPrice(String.format("%s元/克", DoubleUtil.toString(investPrice)))
                .goldWeight(fundRecord.getDisplayLabel())
                .operation(fundRecord.getRemarks())
                .operateDateStr(DateUtil.toString(fundRecord.getCreateDate(), DateUtil.YYYY_MM_DD))
                .build();

    }
}
