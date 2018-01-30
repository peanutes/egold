package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllTypeFundRecordDto {
    
    private String operation;
    
    private String dealDate;
    
    private String amount;

    public static AllTypeFundRecordDto convertFrom(FundRecord fundRecord) {


        String operation = fundRecord.getRemarks();
        if (StringUtils.isBlank(operation)) {
            operation = FundOperateType.getLabelByCode(fundRecord.getOperateType());
        }


        return AllTypeFundRecordDto.builder()
                .operation(operation)
                .dealDate(DateUtil.toString(fundRecord.getCreateDate(), DateUtil.YYYY_MM_DD))
                .amount(fundRecord.getDisplayLabel())
                .build();
    }

}
