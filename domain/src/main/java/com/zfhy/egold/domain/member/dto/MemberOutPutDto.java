package com.zfhy.egold.domain.member.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.Member;
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
public class MemberOutPutDto {

    
    private Integer id;

    
    private String account;


    
    private String mobilePhone;


    
    private String email;








    
    private String enable;


    
    private String headImg;






    
    private String remarks;


    
    
    private String token;


    
    private String huanxinId;

    
    private String huanxinPwd;


    
    private Boolean newUser = false;


    
    private Integer realNameValid;


    
    private Integer bankcardBind;

    
    MemberAccountDto memberAccountDto;

    
    private Boolean hadSetDealPwd;


    
    private String realName;

    
    private String bankCard;

    
    private Bankcard bankCardObject;

    
    private Integer bindWechat;


    
    public Member convertTo() {
        return new MemberOutPutDtoConvert().convert(this);
    }

    
    public MemberOutPutDto convertFrom(Member member) {
        return new MemberOutPutDtoConvert().reverse().convert(member);

    }


    private static class MemberOutPutDtoConvert extends Converter<MemberOutPutDto, Member> {


        @Override
        protected Member doForward(MemberOutPutDto memberDto) {
            Member member = new Member();
            BeanUtils.copyProperties(memberDto, member);
            return member;
        }

        @Override
        protected MemberOutPutDto doBackward(Member member) {
            MemberOutPutDto memberDto = new MemberOutPutDto();
            BeanUtils.copyProperties(member, memberDto);
            memberDto.setHadSetDealPwd(StringUtils.isNotBlank(member.getDealPwd()));
            return memberDto;
        }
    }
}
