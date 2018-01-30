package com.zfhy.egold.common.core.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Setter
@Getter
@Validated
@ApiModel(value = "SysParameterWithoutToken")
public class SysParameterWithoutToken {
    @Min(1506653819183L)
    @Max(1606653819183L)
    
    private long callTimestamp;


    @NotBlank(message = "签名不允许为空")
    
    private String sign;


    @NotBlank(message = "终端类型不允许为空")
    
    private String terminalType;

    @NotBlank(message = "终端唯一标识不允许为空")
    
    private String terminalId;




}
