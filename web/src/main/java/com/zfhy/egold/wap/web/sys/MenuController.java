package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.Menu;
import com.zfhy.egold.domain.sys.dto.MenuDto;
import com.zfhy.egold.domain.sys.service.MenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.*;

import com.zfhy.egold.common.core.parameter.SysParameter;

import javax.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.Date;
import java.util.stream.Collectors;
import com.zfhy.egold.common.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.*;
import tk.mybatis.mapper.entity.Example;
import org.springframework.beans.BeanUtils;
import java.math.BigDecimal;
import com.zfhy.egold.common.core.result.Page;



@RequestMapping("/sys/menu")

@Slf4j
public class MenuController {
    @Resource
    private MenuService menuService;

    
    @PostMapping("/add")
    public Result<String> add(MenuDto menuDto, SysParameter sysParameter) {
        Menu menu = menuDto.convertTo();
        menu.setCreateDate(new Date());
        menuService.save(menu);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "菜单id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        menuService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "菜单id", required = true) @NotBlank @RequestParam Integer id,
                                MenuDto menuDto, SysParameter sysParameter) {
        Menu menu = this.menuService.findById(id);
        if (Objects.isNull(menu) || menu.getId() <= 0) {
            throw new BusException("菜单不存在");
        }
        if (Objects.nonNull(menuDto.getParentId())) {
            menu.setParentId(menuDto.getParentId());
        }
        if (Objects.nonNull(menuDto.getMenuName())) {
            menu.setMenuName(menuDto.getMenuName());
        }
        if (Objects.nonNull(menuDto.getSort())) {
            menu.setSort(menuDto.getSort());
        }
        if (Objects.nonNull(menuDto.getHref())) {
            menu.setHref(menuDto.getHref());
        }
        if (Objects.nonNull(menuDto.getIcon())) {
            menu.setIcon(menuDto.getIcon());
        }
        if (Objects.nonNull(menuDto.getIsShow())) {
            menu.setIsShow(menuDto.getIsShow());
        }
        if (Objects.nonNull(menuDto.getPermission())) {
            menu.setPermission(menuDto.getPermission());
        }
        if (Objects.nonNull(menuDto.getRemarks())) {
            menu.setRemarks(menuDto.getRemarks());
        }

        menuService.update(menu);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<MenuDto> detail(@ApiParam(name = "id", value = "菜单id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Menu menu = menuService.findById(id);
        return ResultGenerator.genSuccessResult(new MenuDto().convertFrom(menu));
    }

    
    @PostMapping("/list")
    public Result<Page<MenuDto>> list(MenuDto menuDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Menu.class);
        Example.Criteria criteria = condition.createCriteria();
        if (Objects.nonNull(menuDto.getParentId())) {
            criteria.andEqualTo("parentId", menuDto.getParentId());
        }
        if (StringUtils.isNotBlank(menuDto.getMenuName())) {
            criteria.andLike("menuName", menuDto.getMenuName());
        }
        if (Objects.nonNull(menuDto.getSort())) {
            criteria.andEqualTo("sort", menuDto.getSort());
        }
        if (StringUtils.isNotBlank(menuDto.getHref())) {
            criteria.andLike("href", menuDto.getHref());
        }
        if (StringUtils.isNotBlank(menuDto.getIcon())) {
            criteria.andLike("icon", menuDto.getIcon());
        }
        if (StringUtils.isNotBlank(menuDto.getIsShow())) {
            criteria.andLike("isShow", menuDto.getIsShow());
        }
        if (StringUtils.isNotBlank(menuDto.getPermission())) {
            criteria.andLike("permission", menuDto.getPermission());
        }
        if (StringUtils.isNotBlank(menuDto.getRemarks())) {
            criteria.andLike("remarks", menuDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Menu> list = menuService.findByCondition(condition);
        PageInfo<Menu> pageInfo = new PageInfo<>(list);

        Page<MenuDto> menuDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, menuDtoPageInfo);
        List<MenuDto> dtoList = list.stream().map(ele -> new MenuDto().convertFrom(ele)).collect(Collectors.toList());
        menuDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(menuDtoPageInfo);

    }
}
