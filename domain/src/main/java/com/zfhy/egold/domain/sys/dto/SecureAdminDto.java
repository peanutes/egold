package com.zfhy.egold.domain.sys.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.sys.entity.Admin;
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
public class SecureAdminDto {

    
    private int id;


    
    private String loginAccount;


    
    private String name;
    

    
    private String remarks;
    



    
    public Admin convertTo() {
        return  new AdminDtoConvert().convert(this);
    }

    
    public SecureAdminDto convertFrom(Admin admin) {
        return new AdminDtoConvert().reverse().convert(admin);

    }


    private static class AdminDtoConvert extends  Converter<SecureAdminDto, Admin> {


        @Override
        protected Admin doForward(SecureAdminDto adminDto) {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminDto, admin);
            return admin;
        }

        @Override
        protected SecureAdminDto doBackward(Admin admin) {
            SecureAdminDto adminDto = new SecureAdminDto();
            BeanUtils.copyProperties(admin, adminDto);
            return adminDto;
        }
    }
}
