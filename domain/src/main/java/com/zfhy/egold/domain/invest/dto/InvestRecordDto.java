package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestRecordDto {


    
    private Integer memberId;
    

    
    private Integer productId;
    

    
    private String productName;

    
    private String productType;
    

    
    private Double investAmount;
    

    
    private Double investGoldWeight;
    

    
    private Double price;
    

    
    private String status;
    

    
    private Date effectDate;
    

    
    private Date deadlineDate;
    

    
    private Double annualRate;
    

    
    private Double additionalRate;
    

    
    private String remarks;
    



    
    public InvestRecord convertTo() {
        return  new InvestRecordDtoConvert().convert(this);
    }

    
    public InvestRecordDto convertFrom(InvestRecord investRecord) {
        return new InvestRecordDtoConvert().reverse().convert(investRecord);

    }


    private static class InvestRecordDtoConvert extends  Converter<InvestRecordDto, InvestRecord> {


        @Override
        protected InvestRecord doForward(InvestRecordDto investRecordDto) {
            InvestRecord investRecord = new InvestRecord();
            BeanUtils.copyProperties(investRecordDto, investRecord);
            return investRecord;
        }

        @Override
        protected InvestRecordDto doBackward(InvestRecord investRecord) {
            InvestRecordDto investRecordDto = new InvestRecordDto();
            BeanUtils.copyProperties(investRecord, investRecordDto);
            return investRecordDto;
        }
    }
}
