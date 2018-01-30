package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.MemberOauth;
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
public class MemberOauthDto {


    
    private Integer memberId;
    

    
    private String openId;
    

    
    private String type;
    

    
    private String remarks;
    



    
    public MemberOauth convertTo() {
        return  new MemberOauthDtoConvert().convert(this);
    }

    
    public MemberOauthDto convertFrom(MemberOauth memberOauth) {
        return new MemberOauthDtoConvert().reverse().convert(memberOauth);

    }


    private static class MemberOauthDtoConvert extends  Converter<MemberOauthDto, MemberOauth> {


        @Override
        protected MemberOauth doForward(MemberOauthDto memberOauthDto) {
            MemberOauth memberOauth = new MemberOauth();
            BeanUtils.copyProperties(memberOauthDto, memberOauth);
            return memberOauth;
        }

        @Override
        protected MemberOauthDto doBackward(MemberOauth memberOauth) {
            MemberOauthDto memberOauthDto = new MemberOauthDto();
            BeanUtils.copyProperties(memberOauth, memberOauthDto);
            return memberOauthDto;
        }
    }
}
