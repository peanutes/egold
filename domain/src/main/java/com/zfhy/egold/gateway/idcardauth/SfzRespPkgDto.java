package com.zfhy.egold.gateway.idcardauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by LAI on 2017/10/2.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SfzRespPkgDto {
    private String gmsfhm = "";
    private String xm = "";
    private String resultGmsfhm = "";
    private String resultXm = "";
    private String sid = "0";
    private String resultxp = "";
    private String state = "";
    private int statcode;
    private String callbackUrl = "";
    private String SessionId;
    private String msg;
}
