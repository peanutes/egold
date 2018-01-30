package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.Idcard;
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
public class IdcardDto {


    
    private Integer memberId;
    

    
    private String realName;
    

    
    private String idCard;
    

    
    private String tel;
    

    
    private String gender;
    

    
    private String birthday;
    

    
    private String remarks;
    



    
    public Idcard convertTo() {
        return  new IdcardDtoConvert().convert(this);
    }

    
    public IdcardDto convertFrom(Idcard idcard) {
        return new IdcardDtoConvert().reverse().convert(idcard);

    }


    private static class IdcardDtoConvert extends  Converter<IdcardDto, Idcard> {


        @Override
        protected Idcard doForward(IdcardDto idcardDto) {
            Idcard idcard = new Idcard();
            BeanUtils.copyProperties(idcardDto, idcard);
            return idcard;
        }

        @Override
        protected IdcardDto doBackward(Idcard idcard) {
            IdcardDto idcardDto = new IdcardDto();
            BeanUtils.copyProperties(idcard, idcardDto);
            return idcardDto;
        }
    }
}
