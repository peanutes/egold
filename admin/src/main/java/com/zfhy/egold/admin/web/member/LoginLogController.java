package com.zfhy.egold.admin.web.member;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.member.dto.LoginStaticDto;
import com.zfhy.egold.domain.member.entity.LoginLog;
import com.zfhy.egold.domain.member.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zfhy.egold.common.core.result.ResultGenerator.genSuccessResult;


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/member/loginLog")
@Slf4j
@Validated
public class LoginLogController {
    private String prefix = "member/loginLog";
    @Autowired
    private LoginLogService loginLogService;


    @RequiresPermissions("member:loginLog:loginLog")
    @GetMapping()
    String loginLog() {
        return prefix + "/loginLog";
    }



    
    @RequiresPermissions("member:loginLog:loginLog")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));

        List<LoginStaticDto> loginLogList = this.loginLogService.statisticLogin(params);
        PageInfo<LoginStaticDto> pageInfo = new PageInfo<>(loginLogList);

        return new PageUtils(loginLogList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:loginLog:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:loginLog:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        LoginLog loginLog = loginLogService.findById(id);
        model.addAttribute("loginLog", loginLog);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:loginLog:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(LoginLog loginLog) {

        loginLog.setCreateDate(new Date());
        loginLog.setDelFlag("0");
        this.loginLogService.save(loginLog);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:loginLog:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(LoginLog loginLog) {
        loginLog.setUpdateDate(new Date());
        this.loginLogService.update(loginLog);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:loginLog:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.loginLogService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:loginLog:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] loginLogIds) {
        if (loginLogIds == null || loginLogIds.length == 0) {
            return genSuccessResult();
        }

        String loginLogIdStr = Stream.of(loginLogIds).map(Object::toString).collect(Collectors.joining(","));
        this.loginLogService.deleteByIds(loginLogIdStr);

        return genSuccessResult();
    }



}
