package com.zfhy.egold.admin.web.fund;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.shiro.ShiroUtil;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.fund.dto.WithdrawStatus;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.domain.fund.service.WithdrawService;
import com.zfhy.egold.domain.member.service.BankcardService;
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
@RequestMapping("/fund/withdraw")
@Slf4j
@Validated
public class WithdrawController {
    private String prefix = "fund/withdraw";
    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private BankcardService bankcardService;

    @RequiresPermissions("fund:withdraw:withdraw")
    @GetMapping()
    String withdraw(Model model, String status) {

        model.addAttribute("withdrawStatusList", WithdrawStatus.values());

        model.addAttribute("status", status);
        return prefix + "/withdraw";
    }


    @RequiresPermissions("fund:withdraw:withdrawList")
    @GetMapping("/withdrawList")
    String withdrawList(Model model, String status) {

        model.addAttribute("withdrawStatusList", WithdrawStatus.values());

        model.addAttribute("status", status);
        return prefix + "/withdraw";
    }



    

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {
        String status = params.remove("status");
        if (status.endsWith("_NOT")) {
            String[] split = status.split("_");
            params.put("status_NOT", split[0]);

        } else {
            params.put("status", status);
        }


        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Withdraw.class);
        SqlUtil.genSqlCondition(params, condition);
        condition.setOrderByClause("create_date desc");

        List<Withdraw> withdrawList = this.withdrawService.findByCondition(condition);
        /*for (Withdraw withdraw : withdrawList) {
            withdraw.setBankName(this.bankcardService.findBankCardNameByMemberId(withdraw.getMemberId()));

        }*/
        PageInfo<Withdraw> pageInfo = new PageInfo<>(withdrawList);

        return new PageUtils(withdrawList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("fund:withdraw:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("fund:withdraw:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("withdrawStatusList", WithdrawStatus.values());
        Withdraw withdraw = withdrawService.findById(id);
        model.addAttribute("withdraw", withdraw);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("fund:withdraw:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Withdraw withdraw) {

        withdraw.setCreateDate(new Date());
        withdraw.setDelFlag("0");
        this.withdrawService.save(withdraw);
        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:withdraw:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Withdraw withdraw) {
        withdraw.setUpdateDate(new Date());
        withdraw.setAdminId(ShiroUtil.getUserId());
        withdraw.setAuditorTime(new Date());

        this.withdrawService.updateAndReleaseAmount(withdraw);

        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:withdraw:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.withdrawService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("fund:withdraw:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] withdrawIds) {
        if (withdrawIds == null || withdrawIds.length == 0) {
            return genSuccessResult();
        }

        String withdrawIdStr = Stream.of(withdrawIds).map(Object::toString).collect(Collectors.joining(","));
        this.withdrawService.deleteByIds(withdrawIdStr);

        return genSuccessResult();
    }



}
