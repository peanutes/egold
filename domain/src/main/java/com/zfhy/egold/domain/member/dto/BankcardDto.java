package com.zfhy.egold.domain.member.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.member.entity.Bankcard;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankcardDto {


    
    private Integer memberId;
    

    
    private String bankAccount;

    
    private String bankLogoImg;
    

    
    private String bankCard;
    

    
    private String bankName;

    
    private String bindMobile;


    
    private String secureTip;





    
    public Bankcard convertTo() {
        return  new BankcardDtoConvert().convert(this);
    }

    
    public BankcardDto convertFrom(Bankcard bankcard) {
        return new BankcardDtoConvert().reverse().convert(bankcard);

    }


    private static class BankcardDtoConvert extends  Converter<BankcardDto, Bankcard> {


        @Override
        protected Bankcard doForward(BankcardDto bankcardDto) {
            Bankcard bankcard = new Bankcard();
            BeanUtils.copyProperties(bankcardDto, bankcard);
            return bankcard;
        }

        @Override
        protected BankcardDto doBackward(Bankcard bankcard) {
            BankcardDto bankcardDto = new BankcardDto();
            BeanUtils.copyProperties(bankcard, bankcardDto);
            bankcardDto.setBindMobile(StrFunUtil.hidMobile(bankcard.getBindMobile()));
            return bankcardDto;
        }
    }
}
