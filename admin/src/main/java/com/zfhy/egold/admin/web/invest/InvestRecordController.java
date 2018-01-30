package com.zfhy.egold.admin.web.invest;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
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
@RequestMapping("/invest/investRecord")
@Slf4j
@Validated
public class InvestRecordController {
    private String prefix = "invest/investRecord";
    @Autowired
    private InvestRecordService investRecordService;


    @RequiresPermissions("invest:investRecord:investRecord")
    @GetMapping()
    String investRecord() {
        return prefix + "/investRecord";
    }



    
    @RequiresPermissions("invest:investRecord:investRecord")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(InvestRecord.class);
        SqlUtil.genSqlCondition(params, condition);

        List<InvestRecord> investRecordList = this.investRecordService.findByCondition(condition);
        PageInfo<InvestRecord> pageInfo = new PageInfo<>(investRecordList);

        return new PageUtils(investRecordList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("invest:investRecord:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("invest:investRecord:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        InvestRecord investRecord = investRecordService.findById(id);
        model.addAttribute("investRecord", investRecord);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("invest:investRecord:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(InvestRecord investRecord) {

        investRecord.setCreateDate(new Date());
        investRecord.setDelFlag("0");
        this.investRecordService.save(investRecord);
        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:investRecord:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(InvestRecord investRecord) {
        investRecord.setUpdateDate(new Date());
        this.investRecordService.update(investRecord);

        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:investRecord:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.investRecordService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("invest:investRecord:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] investRecordIds) {
        if (investRecordIds == null || investRecordIds.length == 0) {
            return genSuccessResult();
        }

        String investRecordIdStr = Stream.of(investRecordIds).map(Object::toString).collect(Collectors.joining(","));
        this.investRecordService.deleteByIds(investRecordIdStr);

        return genSuccessResult();
    }



}
