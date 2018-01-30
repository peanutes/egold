package com.zfhy.egold.admin.web.sys;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.sys.entity.Bank;
import com.zfhy.egold.domain.sys.service.BankService;
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
@RequestMapping("/sys/bank")
@Slf4j
@Validated
public class BankController {
    private String prefix = "sys/bank";
    @Autowired
    private BankService bankService;


    @RequiresPermissions("sys:bank:bank")
    @GetMapping()
    String bank() {
        return prefix + "/bank";
    }



    
    @RequiresPermissions("sys:bank:bank")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Bank.class);
        SqlUtil.genSqlCondition(params, condition);

        List<Bank> bankList = this.bankService.findByCondition(condition);
        PageInfo<Bank> pageInfo = new PageInfo<>(bankList);

        return new PageUtils(bankList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("sys:bank:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("sys:bank:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Bank bank = bankService.findById(id);
        model.addAttribute("bank", bank);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("sys:bank:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Bank bank) {

        bank.setCreateDate(new Date());
        bank.setDelFlag("0");
        this.bankService.save(bank);
        return genSuccessResult();
    }

    
    @RequiresPermissions("sys:bank:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Bank bank) {
        bank.setUpdateDate(new Date());
        this.bankService.update(bank);

        return genSuccessResult();
    }

    
    @RequiresPermissions("sys:bank:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.bankService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("sys:bank:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] bankIds) {
        if (bankIds == null || bankIds.length == 0) {
            return genSuccessResult();
        }

        String bankIdStr = Stream.of(bankIds).map(Object::toString).collect(Collectors.joining(","));
        this.bankService.deleteByIds(bankIdStr);

        return genSuccessResult();
    }



}
