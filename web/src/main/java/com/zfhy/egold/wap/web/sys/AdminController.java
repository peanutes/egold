package com.zfhy.egold.wap.web.sys;

import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.sys.entity.Admin;
import com.zfhy.egold.domain.sys.dto.AdminDto;
import com.zfhy.egold.domain.sys.service.AdminService;
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



@RequestMapping("/sys/admin")

@Slf4j
public class AdminController {
    @Resource
    private AdminService adminService;

    
    @PostMapping("/add")
    public Result<String> add(AdminDto adminDto, SysParameter sysParameter) {
        Admin admin = adminDto.convertTo();
        admin.setCreateDate(new Date());
        adminService.save(admin);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "后台管理员id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        adminService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/update")
    public Result<String> update(@ApiParam(name = "id", value = "后台管理员id", required = true) @NotBlank @RequestParam Integer id,
                                AdminDto adminDto, SysParameter sysParameter) {
        Admin admin = this.adminService.findById(id);
        if (Objects.isNull(admin) || admin.getId() <= 0) {
            throw new BusException("后台管理员不存在");
        }
        if (Objects.nonNull(adminDto.getLoginAccount())) {
            admin.setLoginAccount(adminDto.getLoginAccount());
        }
        if (Objects.nonNull(adminDto.getRemarks())) {
            admin.setRemarks(adminDto.getRemarks());
        }

        adminService.update(admin);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/detail")
    public Result<AdminDto> detail(@ApiParam(name = "id", value = "后台管理员id", required = true) @NotBlank @RequestParam Integer id, SysParameter sysParameter) {
        Admin admin = adminService.findById(id);
        return ResultGenerator.genSuccessResult(new AdminDto().convertFrom(admin));
    }

    
    @PostMapping("/list")
    public Result<Page<AdminDto>> list(AdminDto adminDto, @ApiParam(name = "page", value = "页数", required = true) @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true) @RequestParam(defaultValue = "0") Integer size, SysParameter sysParameter) {
        Condition condition = new Condition(Admin.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(adminDto.getLoginAccount())) {
            criteria.andLike("loginAccount", adminDto.getLoginAccount());
        }
        if (StringUtils.isNotBlank(adminDto.getRemarks())) {
            criteria.andLike("remarks", adminDto.getRemarks());
        }
        criteria.andEqualTo("delFlag", "0");

        PageHelper.startPage(page, size);

        List<Admin> list = adminService.findByCondition(condition);
        PageInfo<Admin> pageInfo = new PageInfo<>(list);

        Page<AdminDto> adminDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, adminDtoPageInfo);
        List<AdminDto> dtoList = list.stream().map(ele -> new AdminDto().convertFrom(ele)).collect(Collectors.toList());
        adminDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(adminDtoPageInfo);

    }
}
