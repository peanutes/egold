package com.zfhy.egold.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginStaticDto {
    private Integer id;

    private String mobile;

    private String terminalType;

    private String loginIp;


    private Date loginTime;

    private Integer time;


}
