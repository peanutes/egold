package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.domain.fund.entity.MemberFund;
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
public class MemberFundDto {


    
    private Integer memberId;
    

    
    private Double cashBalance;
    

    
    private Double termGoldBalance;
    

    
    private Double currentGoldBalance;
    

    
    private Double investBalance;
    

    
    private Integer version;
    

    
    private String remarks;
    



    
    public MemberFund convertTo() {
        return  new MemberFundDtoConvert().convert(this);
    }

    
    public MemberFundDto convertFrom(MemberFund memberFund) {
        return new MemberFundDtoConvert().reverse().convert(memberFund);

    }


    private static class MemberFundDtoConvert extends  Converter<MemberFundDto, MemberFund> {


        @Override
        protected MemberFund doForward(MemberFundDto memberFundDto) {
            MemberFund memberFund = new MemberFund();
            BeanUtils.copyProperties(memberFundDto, memberFund);
            return memberFund;
        }

        @Override
        protected MemberFundDto doBackward(MemberFund memberFund) {
            MemberFundDto memberFundDto = new MemberFundDto();
            BeanUtils.copyProperties(memberFund, memberFundDto);
            return memberFundDto;
        }
    }
}
