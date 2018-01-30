package com.zfhy.egold.domain.invest.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuyHistoryDto {

    
    private String mobile;
    
    private String weight;
    
    private String buyTime;

    
    private String terminalType;

    
    public InvestRecord convertTo() {
        return  new BuyHistoryDtoConvert().convert(this);
    }

    
    public BuyHistoryDto convertFrom(InvestRecord investRecord) {
        return new BuyHistoryDtoConvert().reverse().convert(investRecord);

    }


    private static class BuyHistoryDtoConvert extends Converter<BuyHistoryDto, InvestRecord> {


        @Override
        protected InvestRecord doForward(BuyHistoryDto buyHistoryDto) {
            InvestRecord investRecord = new InvestRecord();
            BeanUtils.copyProperties(buyHistoryDto, investRecord);
            return investRecord;
        }

        @Override
        protected BuyHistoryDto doBackward(InvestRecord investRecord) {
            BuyHistoryDto investRecordDto = new BuyHistoryDto();

            Double weight = DoubleUtil.doubleDiv(investRecord.getInvestAmount(), investRecord.getPrice(), 4);

            investRecordDto.setWeight(DoubleUtil.toScal4(weight));

            investRecordDto.setBuyTime(DateUtil.toString(investRecord.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM));

            investRecordDto.setMobile(StrFunUtil.hidMobile(investRecord.getMobile()));
            if (StringUtils.isBlank(investRecord.getTerminalType())) {
                investRecordDto.setTerminalType("pc");
            } else {
                investRecordDto.setTerminalType(investRecord.getTerminalType());
            }

            return investRecordDto;
        }
    }

}
