package com.zfhy.egold.domain.cms.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.cms.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {


    
    private String name;
    

    
    private String code;
    

    
    private String type;
    

    
    private Integer financeProductId;
    

    
    private String url;
    

    
    private String img;
    

    
    private Integer sort;
    

    
    private String remarks;
    



    
    public Banner convertTo() {
        return  new BannerDtoConvert().convert(this);
    }

    
    public BannerDto convertFrom(Banner banner, String terminalType, String terminalId) {
        BannerDto bannerDto = new BannerDtoConvert().reverse().convert(banner);

        bannerDto.setUrl(StrFunUtil.assembleUrl(terminalType, terminalId, banner.getUrl()));
        return bannerDto;

    }


    private static class BannerDtoConvert extends  Converter<BannerDto, Banner> {


        @Override
        protected Banner doForward(BannerDto bannerDto) {
            Banner banner = new Banner();
            BeanUtils.copyProperties(bannerDto, banner);
            return banner;
        }

        @Override
        protected BannerDto doBackward(Banner banner) {
            BannerDto bannerDto = new BannerDto();
            BeanUtils.copyProperties(banner, bannerDto);

            return bannerDto;
        }
    }
}
