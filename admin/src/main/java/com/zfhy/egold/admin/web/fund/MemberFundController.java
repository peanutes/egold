package com.zfhy.egold.admin.web.fund;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
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
@RequestMapping("/fund/memberFund")
@Slf4j
@Validated
public class MemberFundController {
    private String prefix = "fund/memberFund";
    @Autowired
    private MemberFundService memberFundService;


    @Autowired
    private MemberService memberService;


    @RequiresPermissions("fund:memberFund:memberFund")
    @GetMapping()
    String memberFund() {
        return prefix + "/memberFund";
    }



    
    @RequiresPermissions("fund:memberFund:memberFund")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));

        List<MemberFund> memberFundList = this.memberFundService.list(params);
        PageInfo<MemberFund> pageInfo = new PageInfo<>(memberFundList);


        return new PageUtils(memberFundList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("fund:memberFund:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("fund:memberFund:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        MemberFund memberFund = memberFundService.findById(id);
        model.addAttribute("memberFund", memberFund);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("fund:memberFund:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(MemberFund memberFund) {

        memberFund.setCreateDate(new Date());
        memberFund.setDelFlag("0");
        this.memberFundService.save(memberFund);
        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:memberFund:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(MemberFund memberFund) {
        memberFund.setUpdateDate(new Date());
        this.memberFundService.update(memberFund);

        return genSuccessResult();
    }

    
    @RequiresPermissions("fund:memberFund:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.memberFundService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("fund:memberFund:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] memberFundIds) {
        if (memberFundIds == null || memberFundIds.length == 0) {
            return genSuccessResult();
        }

        String memberFundIdStr = Stream.of(memberFundIds).map(Object::toString).collect(Collectors.joining(","));
        this.memberFundService.deleteByIds(memberFundIdStr);

        return genSuccessResult();
    }



}
