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
public class RespPkgDto {
    private String state = "";
    private int statcode;
    private String msg = "";
    private String callbackUrl = "";
}
