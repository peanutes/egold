package com.zfhy.egold.admin.web.member;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.member.entity.OperateLog;
import com.zfhy.egold.domain.member.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

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
@RequestMapping("/member/operateLog")
@Slf4j
@Validated
public class OperateLogController {
    private String prefix = "member/operateLog";
    @Autowired
    private OperateLogService operateLogService;


    @RequiresPermissions("member:operateLog:operateLog")
    @GetMapping()
    String operateLog() {
        return prefix + "/operateLog";
    }



    
    @RequiresPermissions("member:operateLog:operateLog")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(OperateLog.class);
        SqlUtil.genSqlCondition(params, condition);

        List<OperateLog> operateLogList = this.operateLogService.findByCondition(condition);
        PageInfo<OperateLog> pageInfo = new PageInfo<>(operateLogList);

        return new PageUtils(operateLogList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:operateLog:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:operateLog:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        OperateLog operateLog = operateLogService.findById(id);
        model.addAttribute("operateLog", operateLog);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:operateLog:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(OperateLog operateLog) {

        operateLog.setCreateDate(new Date());
        this.operateLogService.save(operateLog);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:operateLog:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(OperateLog operateLog) {
        this.operateLogService.update(operateLog);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:operateLog:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.operateLogService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:operateLog:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] operateLogIds) {
        if (operateLogIds == null || operateLogIds.length == 0) {
            return genSuccessResult();
        }

        String operateLogIdStr = Stream.of(operateLogIds).map(Object::toString).collect(Collectors.joining(","));
        this.operateLogService.deleteByIds(operateLogIdStr);

        return genSuccessResult();
    }



}
