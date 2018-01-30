package com.zfhy.egold.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginResponse {
    
    private Boolean hadBind;

    
    private MemberOutPutDto memberOutPutDto;

}
