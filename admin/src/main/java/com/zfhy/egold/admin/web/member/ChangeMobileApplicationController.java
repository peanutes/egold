package com.zfhy.egold.admin.web.member;



import com.zfhy.egold.domain.member.entity.ChangeMobileApplication;
import com.zfhy.egold.domain.member.service.ChangeMobileApplicationService;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;

import com.zfhy.egold.admin.log.Log;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import com.zfhy.egold.common.util.SqlUtil;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

import static com.zfhy.egold.common.core.result.ResultGenerator.genSuccessResult;


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/member/changeMobileApplication")
@Slf4j
@Validated
public class ChangeMobileApplicationController {
    private String prefix = "member/changeMobileApplication";
    @Autowired
    private ChangeMobileApplicationService changeMobileApplicationService;


    @RequiresPermissions("member:changeMobileApplication:changeMobileApplication")
    @GetMapping()
    String changeMobileApplication() {
        return prefix + "/changeMobileApplication";
    }



    
    @RequiresPermissions("member:changeMobileApplication:changeMobileApplication")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(ChangeMobileApplication.class);
        SqlUtil.genSqlCondition(params, condition);

        List<ChangeMobileApplication> changeMobileApplicationList = this.changeMobileApplicationService.findByCondition(condition);
        PageInfo<ChangeMobileApplication> pageInfo = new PageInfo<>(changeMobileApplicationList);

        return new PageUtils(changeMobileApplicationList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:changeMobileApplication:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:changeMobileApplication:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        ChangeMobileApplication changeMobileApplication = changeMobileApplicationService.findById(id);
        model.addAttribute("changeMobileApplication", changeMobileApplication);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:changeMobileApplication:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(ChangeMobileApplication changeMobileApplication) {

        changeMobileApplication.setCreateDate(new Date());
        changeMobileApplication.setDelFlag("0");
        this.changeMobileApplicationService.save(changeMobileApplication);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:changeMobileApplication:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(ChangeMobileApplication changeMobileApplication) {
        changeMobileApplication.setUpdateDate(new Date());
        this.changeMobileApplicationService.update(changeMobileApplication);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:changeMobileApplication:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.changeMobileApplicationService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:changeMobileApplication:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] changeMobileApplicationIds) {
        if (changeMobileApplicationIds == null || changeMobileApplicationIds.length == 0) {
            return genSuccessResult();
        }

        String changeMobileApplicationIdStr = Stream.of(changeMobileApplicationIds).map(Object::toString).collect(Collectors.joining(","));
        this.changeMobileApplicationService.deleteByIds(changeMobileApplicationIdStr);

        return genSuccessResult();
    }



}
