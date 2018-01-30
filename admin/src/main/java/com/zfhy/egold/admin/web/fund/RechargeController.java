package com.zfhy.egold.admin.web.fund;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.fund.dto.RechargeStatus;
import com.zfhy.egold.domain.fund.entity.Recharge;
import com.zfhy.egold.domain.fund.service.RechargeService;
import com.zfhy.egold.domain.member.service.MemberService;
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
@RequestMapping("/fund/recharge")
@Slf4j
@Validated
public class RechargeController {
    private String prefix = "fund/recharge";
    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private MemberService memberService;


    @RequiresPermissions("fund:recharge:recharge")
    @GetMapping()
    String recharge(Model model) {
        model.addAttribute("rechargeStatusList", RechargeStatus.values());

        return prefix + "/recharge";
    }



    
    @RequiresPermissions("fund:recharge:recharge")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        PageHelper.orderBy("create_date desc");

        List<Recharge> rechargeList = this.rechargeService.list(params);
        PageInfo<Recharge> pageInfo = new PageInfo<>(rechargeList);


        return new PageUtils(rechargeList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("fund:recharge:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("fund:recharge:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Recharge recharge = rechargeService.findById(id);
        model.addAttribute("recharge", recharge);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("fund:recharge:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Recharge recharge) {

        recharge.setCreateDate(new Date());
        recharge.setDelFlag("0");
        this.rechargeService.save(recharge);
        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:recharge:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Recharge recharge) {
        recharge.setUpdateDate(new Date());
        this.rechargeService.update(recharge);

        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:recharge:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.rechargeService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("fund:recharge:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] rechargeIds) {
        if (rechargeIds == null || rechargeIds.length == 0) {
            return genSuccessResult();
        }

        String rechargeIdStr = Stream.of(rechargeIds).map(Object::toString).collect(Collectors.joining(","));
        this.rechargeService.deleteByIds(rechargeIdStr);

        return genSuccessResult();
    }



}
