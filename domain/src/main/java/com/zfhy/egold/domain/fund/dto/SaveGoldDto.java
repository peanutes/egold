package com.zfhy.egold.domain.fund.dto;

import com.zfhy.egold.domain.fund.entity.SaveGold;
import com.google.common.base.Converter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveGoldDto {


    
    private Integer id;
    

    
    private Integer memberId;
    

    
    private Date saveTime;
    

    
    private Double goldWeight;
    

    
    private Integer storeId;
    

    
    private String storeName;
    

    
    private Integer adminId;
    

    
    private String operator;
    

    
    private String status;
    

    
    private String remarks;
    



    
    public SaveGold convertTo() {
        return  new SaveGoldDtoConvert().convert(this);
    }

    
    public SaveGoldDto convertFrom(SaveGold saveGold) {
        return new SaveGoldDtoConvert().reverse().convert(saveGold);

    }


    private static class SaveGoldDtoConvert extends  Converter<SaveGoldDto, SaveGold> {


        @Override
        protected SaveGold doForward(SaveGoldDto saveGoldDto) {
            SaveGold saveGold = new SaveGold();
            BeanUtils.copyProperties(saveGoldDto, saveGold);
            return saveGold;
        }

        @Override
        protected SaveGoldDto doBackward(SaveGold saveGold) {
            SaveGoldDto saveGoldDto = new SaveGoldDto();
            BeanUtils.copyProperties(saveGold, saveGoldDto);
            return saveGoldDto;
        }
    }
}
