package com.zfhy.egold.domain.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberReportDto {

    
    private List<String> xaxis;

    
    private List<Integer> registerCountList;

    
    private List<Integer> realNameCountList;


    
    private List<Integer> bindCardCountList;





}
