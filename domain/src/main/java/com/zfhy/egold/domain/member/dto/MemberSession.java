package com.zfhy.egold.domain.member.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.member.entity.Member;
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
public class MemberSession {

    
    private Integer id;


    
    private String account;
    

    
    private String mobilePhone;
    

    
    private String email;
    

    
    private String enable;
    

    
    private String headImg;
    

    
    private Integer referee;
    

    
    private String remarks;

    
    private Date createDate;



    



    
    public Member convertTo() {
        return  new MemberSessionConvert().convert(this);
    }

    
    public MemberSession convertFrom(Member member) {
        return new MemberSessionConvert().reverse().convert(member);

    }


    private static class MemberSessionConvert extends  Converter<MemberSession, Member> {


        @Override
        protected Member doForward(MemberSession memberSession) {
            Member member = new Member();
            BeanUtils.copyProperties(memberSession, member);
            return member;
        }

        @Override
        protected MemberSession doBackward(Member member) {
            MemberSession memberSession = new MemberSession();
            BeanUtils.copyProperties(member, memberSession);
            return memberSession;
        }
    }
}
