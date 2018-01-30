package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Bank;
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
public class BankDto {


    
    private String bankCode;
    

    
    private String bankName;
    

    
    private String bankLogo;
    

    
    private String remarks;
    



    
    public Bank convertTo() {
        return  new BankDtoConvert().convert(this);
    }

    
    public BankDto convertFrom(Bank bank) {
        return new BankDtoConvert().reverse().convert(bank);

    }


    private static class BankDtoConvert extends  Converter<BankDto, Bank> {


        @Override
        protected Bank doForward(BankDto bankDto) {
            Bank bank = new Bank();
            BeanUtils.copyProperties(bankDto, bank);
            return bank;
        }

        @Override
        protected BankDto doBackward(Bank bank) {
            BankDto bankDto = new BankDto();
            BeanUtils.copyProperties(bank, bankDto);
            return bankDto;
        }
    }
}
