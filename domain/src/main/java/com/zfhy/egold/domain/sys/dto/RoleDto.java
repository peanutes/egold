package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Role;
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
public class RoleDto {


    
    private String roleName;
    

    
    private String remarks;
    



    
    public Role convertTo() {
        return  new RoleDtoConvert().convert(this);
    }

    
    public RoleDto convertFrom(Role role) {
        return new RoleDtoConvert().reverse().convert(role);

    }


    private static class RoleDtoConvert extends  Converter<RoleDto, Role> {


        @Override
        protected Role doForward(RoleDto roleDto) {
            Role role = new Role();
            BeanUtils.copyProperties(roleDto, role);
            return role;
        }

        @Override
        protected RoleDto doBackward(Role role) {
            RoleDto roleDto = new RoleDto();
            BeanUtils.copyProperties(role, roleDto);
            return roleDto;
        }
    }
}
