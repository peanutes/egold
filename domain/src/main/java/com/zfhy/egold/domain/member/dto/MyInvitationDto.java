package com.zfhy.egold.domain.member.dto;

import com.zfhy.egold.common.core.result.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Collections;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInvitationDto {
    public static final MyInvitationDto EMPTY = MyInvitationDto.builder()
            .commission(0.0d)
            .myInvitationMemberPage(Page.newPage(Collections.emptyList()))
            .build();



    
    private Double commission;
    
    private Page<MyInvitationMember> myInvitationMemberPage;

}
