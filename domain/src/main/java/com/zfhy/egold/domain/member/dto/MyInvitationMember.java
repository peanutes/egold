package com.zfhy.egold.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInvitationMember {
    
    private String mobile;
    
    private String registerDate;
    
    private String invisted;

    public static void main(String[] args) {
        System.out.println("18922463298".replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));

        String phone = "13123456789";
        String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        System.out.println("正则phone中4*：" + phoneNumber);
    }
}
