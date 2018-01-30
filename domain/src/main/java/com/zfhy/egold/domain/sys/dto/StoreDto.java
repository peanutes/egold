package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.sys.entity.Store;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {


    
    private String storeName;
    

    
    private String storeAddr;
    

    
    private String tel;
    

    
    private String img;
    

    
    private Integer canStore;
    

    
    private Integer canWithdraw;
    

    
    private String remarks;
    



    
    public Store convertTo() {
        return  new StoreDtoConvert().convert(this);
    }

    
    public StoreDto convertFrom(Store store) {
        return new StoreDtoConvert().reverse().convert(store);

    }


    private static class StoreDtoConvert extends  Converter<StoreDto, Store> {


        @Override
        protected Store doForward(StoreDto storeDto) {
            Store store = new Store();
            BeanUtils.copyProperties(storeDto, store);
            return store;
        }

        @Override
        protected StoreDto doBackward(Store store) {
            StoreDto storeDto = new StoreDto();
            BeanUtils.copyProperties(store, storeDto);
            return storeDto;
        }
    }
}
