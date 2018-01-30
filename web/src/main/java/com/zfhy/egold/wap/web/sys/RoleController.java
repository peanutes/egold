package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.Role;
import com.zfhy.egold.domain.sys.dto.RoleDto;
import com.zfhy.egold.domain.sys.service.RoleService;
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



@RequestMapping("/sys/role")

@Slf4j
public class RoleController {
    @Resource
    private RoleService roleService;

    
    @PostMapping("/add")
    public Result<String> add(RoleDto roleDto, SysParameter sysParameter) {
        Role role = roleDto.convertTo();
        role.setCreateDate(new Date());
        roleService.save(role);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "管理员角色id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        roleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "管理员角色id", required = true) @NotBlank @RequestParam Integer id,
                                RoleDto roleDto, SysParameter sysParameter) {
        Role role = this.roleService.findById(id);
        if (Objects.isNull(role) || role.getId() <= 0) {
            throw new BusException("管理员角色不存在");
        }
        if (Objects.nonNull(roleDto.getRoleName())) {
            role.setRoleName(roleDto.getRoleName());
        }
        if (Objects.nonNull(roleDto.getRemarks())) {
            role.setRemarks(roleDto.getRemarks());
        }

        roleService.update(role);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<RoleDto> detail(@ApiParam(name = "id", value = "管理员角色id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Role role = roleService.findById(id);
        return ResultGenerator.genSuccessResult(new RoleDto().convertFrom(role));
    }

    
    @PostMapping("/list")
    public Result<Page<RoleDto>> list(RoleDto roleDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Role.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(roleDto.getRoleName())) {
            criteria.andLike("roleName", roleDto.getRoleName());
        }
        if (StringUtils.isNotBlank(roleDto.getRemarks())) {
            criteria.andLike("remarks", roleDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Role> list = roleService.findByCondition(condition);
        PageInfo<Role> pageInfo = new PageInfo<>(list);

        Page<RoleDto> roleDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, roleDtoPageInfo);
        List<RoleDto> dtoList = list.stream().map(ele -> new RoleDto().convertFrom(ele)).collect(Collectors.toList());
        roleDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(roleDtoPageInfo);

    }
}
