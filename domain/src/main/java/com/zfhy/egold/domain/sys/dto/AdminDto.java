package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Admin;
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
public class AdminDto {


    
    private String loginAccount;
    

    
    private String name;
    



    

    
    private String status;
    

    
    private String remarks;
    



    
    public Admin convertTo() {
        return  new AdminDtoConvert().convert(this);
    }

    
    public AdminDto convertFrom(Admin admin) {
        return new AdminDtoConvert().reverse().convert(admin);

    }


    private static class AdminDtoConvert extends  Converter<AdminDto, Admin> {


        @Override
        protected Admin doForward(AdminDto adminDto) {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminDto, admin);
            return admin;
        }

        @Override
        protected AdminDto doBackward(Admin admin) {
            AdminDto adminDto = new AdminDto();
            BeanUtils.copyProperties(admin, adminDto);
            return adminDto;
        }
    }
}
