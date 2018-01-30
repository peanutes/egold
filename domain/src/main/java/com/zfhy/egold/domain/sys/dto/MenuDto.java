package com.zfhy.egold.domain.sys.dto;

import com.zfhy.egold.domain.sys.entity.Menu;
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
public class MenuDto {


    
    private Integer parentId;
    

    
    private String menuName;
    

    
    private String type;
    

    
    private Integer sort;
    

    
    private String href;
    

    
    private String icon;
    

    
    private String isShow;
    

    
    private String permission;
    

    
    private String remarks;
    



    
    public Menu convertTo() {
        return  new MenuDtoConvert().convert(this);
    }

    
    public MenuDto convertFrom(Menu menu) {
        return new MenuDtoConvert().reverse().convert(menu);

    }


    private static class MenuDtoConvert extends  Converter<MenuDto, Menu> {


        @Override
        protected Menu doForward(MenuDto menuDto) {
            Menu menu = new Menu();
            BeanUtils.copyProperties(menuDto, menu);
            return menu;
        }

        @Override
        protected MenuDto doBackward(Menu menu) {
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menu, menuDto);
            return menuDto;
        }
    }
}
