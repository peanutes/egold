package com.zfhy.egold.domain.member.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.member.entity.Bankcard;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankcardListDto {


    
    private Integer id;


    
    private String realName;

    
    private String idCard;

    
    private String mobile;


    
    private String bankAccount;
    

    
    private String bankCard;
    

    
    private String bankName;
    




    
    public Bankcard convertTo() {
        return  new BankcardDtoConvert().convert(this);
    }

    
    public BankcardListDto convertFrom(Bankcard bankcard) {
        return new BankcardDtoConvert().reverse().convert(bankcard);

    }


    private static class BankcardDtoConvert extends  Converter<BankcardListDto, Bankcard> {


        @Override
        protected Bankcard doForward(BankcardListDto bankcardDto) {
            Bankcard bankcard = new Bankcard();
            BeanUtils.copyProperties(bankcardDto, bankcard);
            return bankcard;
        }

        @Override
        protected BankcardListDto doBackward(Bankcard bankcard) {
            BankcardListDto bankcardDto = new BankcardListDto();
            BeanUtils.copyProperties(bankcard, bankcardDto);
            return bankcardDto;
        }
    }
}
