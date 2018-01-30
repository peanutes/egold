package com.zfhy.egold.admin.web.fund;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import com.zfhy.egold.domain.fund.service.FundRecordService;
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
@RequestMapping("/fund/fundRecord")
@Slf4j
@Validated
public class FundRecordController {
    private String prefix = "fund/fundRecord";
    @Autowired
    private FundRecordService fundRecordService;


    @RequiresPermissions("fund:fundRecord:fundRecord")
    @GetMapping()
    String fundRecord() {
        return prefix + "/fundRecord";
    }



    
    @RequiresPermissions("fund:fundRecord:fundRecord")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(FundRecord.class);
        SqlUtil.genSqlCondition(params, condition);

        List<FundRecord> fundRecordList = this.fundRecordService.findByCondition(condition);
        PageInfo<FundRecord> pageInfo = new PageInfo<>(fundRecordList);

        return new PageUtils(fundRecordList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("fund:fundRecord:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("fund:fundRecord:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        FundRecord fundRecord = fundRecordService.findById(id);
        model.addAttribute("fundRecord", fundRecord);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("fund:fundRecord:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(FundRecord fundRecord) {

        fundRecord.setCreateDate(new Date());
        fundRecord.setDelFlag("0");
        this.fundRecordService.save(fundRecord);
        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:fundRecord:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(FundRecord fundRecord) {
        fundRecord.setUpdateDate(new Date());
        this.fundRecordService.update(fundRecord);

        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:fundRecord:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.fundRecordService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("fund:fundRecord:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] fundRecordIds) {
        if (fundRecordIds == null || fundRecordIds.length == 0) {
            return genSuccessResult();
        }

        String fundRecordIdStr = Stream.of(fundRecordIds).map(Object::toString).collect(Collectors.joining(","));
        this.fundRecordService.deleteByIds(fundRecordIdStr);

        return genSuccessResult();
    }



}
