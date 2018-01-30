package com.zfhy.egold.domain.invest.dto;

import com.zfhy.egold.domain.cms.dto.PcHomeHotInfoDto;
import com.zfhy.egold.domain.goods.dto.PcHomeSkuDto;
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
public class PcHomeDto {
    
    private PcHomeNewProductDto pcHomeNewProductDto;

    
    private List<PcTermProductDto> pcTermProductDtos;

    
    private List<PcHomeSkuDto> pcHomeSkuDtos;

    
    private List<PcHomeHotInfoDto> pcHomeHotInfoDtos;

    
    private List<PcHomeHotInfoDto> pcHomeHelpDesk;





}
