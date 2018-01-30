package com.zfhy.egold.common.core.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@ApiModel(value = "SysParameter")
public class SysParameter {

    @Min(1506653819183L)
    @Max(1606653819183L)
    @NotNull(message = "调用时间不允许为空")
    
    private long callTimestamp;

    @NotBlank(message = "签名不允许为空")
    
    private String sign;

    @NotBlank(message = "token不允许为空")
    
    private String token;


    @NotBlank(message = "终端类型不允许为空")
    
    private String terminalType;

    @NotBlank(message = "终端唯一标识不允许为空")
    
    private String terminalId;




}
