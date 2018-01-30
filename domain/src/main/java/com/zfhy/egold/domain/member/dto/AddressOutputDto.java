package com.zfhy.egold.domain.member.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.member.entity.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressOutputDto {


    @NotNull(message = "收货地址id不允许为空")
    
    private Integer id;


    
    private Integer memberId;
    

    
    private String receiver;
    

    
    private String receiverTel;
    

    
    private String receiverSecTel;
    

    
    private String zipCode;
    

    
    private Integer countryId;
    

    
    private Integer provinceId;
    

    
    private Integer cityId;
    

    
    private Integer countyId;
    

    
    private Integer townId;
    

    
    private String country;
    

    
    private String province;
    

    
    private String city;
    

    
    private String county;
    

    
    private String town;
    

    
    private String address;
    

    
    private String defaultAddress;
    

    
    private String remarks;
    



    
    public Address convertTo() {
        return  new AddressDtoConvert().convert(this);
    }

    
    public AddressOutputDto convertFrom(Address address) {
        return new AddressDtoConvert().reverse().convert(address);

    }


    private static class AddressDtoConvert extends  Converter<AddressOutputDto, Address> {


        @Override
        protected Address doForward(AddressOutputDto addressDto) {
            Address address = new Address();
            BeanUtils.copyProperties(addressDto, address);
            return address;
        }

        @Override
        protected AddressOutputDto doBackward(Address address) {
            AddressOutputDto addressDto = new AddressOutputDto();
            BeanUtils.copyProperties(address, addressDto);
            return addressDto;
        }
    }
}
