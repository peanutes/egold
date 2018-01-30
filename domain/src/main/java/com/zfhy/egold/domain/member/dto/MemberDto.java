package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.domain.member.entity.Member;
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
public class MemberDto {


    
    private String account;
    

    
    private String mobilePhone;
    

    
    private String email;
    

    
    private String password;
    

    
    private String dealPwd;
    

    
    private String salt;
    

    
    private String enable;
    

    
    private String headImg;
    

    
    private Integer referee;
    

    
    private Integer realNameValid;
    

    
    private Integer bankcardBind;
    

    
    private String remarks;
    



    
    public Member convertTo() {
        return  new MemberDtoConvert().convert(this);
    }

    
    public MemberDto convertFrom(Member member) {
        return new MemberDtoConvert().reverse().convert(member);

    }


    private static class MemberDtoConvert extends  Converter<MemberDto, Member> {


        @Override
        protected Member doForward(MemberDto memberDto) {
            Member member = new Member();
            BeanUtils.copyProperties(memberDto, member);
            return member;
        }

        @Override
        protected MemberDto doBackward(Member member) {
            MemberDto memberDto = new MemberDto();
            BeanUtils.copyProperties(member, memberDto);
            return memberDto;
        }
    }
}
