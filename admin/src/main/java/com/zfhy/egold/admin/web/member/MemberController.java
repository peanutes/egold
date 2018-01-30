package com.zfhy.egold.admin.web.member;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.member.entity.Member;
import com.zfhy.egold.domain.member.service.MemberService;
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


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/member/member")
@Slf4j
@Validated
public class MemberController {
    private String prefix = "member/member";
    @Autowired
    private MemberService memberService;


    @RequiresPermissions("member:member:member")
    @GetMapping()
    String member() {
        return prefix + "/member";
    }



    
    @RequiresPermissions("member:member:member")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        params.put("delFlag", "0");
        Condition condition = new Condition(Member.class);
        SqlUtil.genSqlCondition(params, condition);

        List<Member> memberList = this.memberService.findByCondition(condition);
        PageInfo<Member> pageInfo = new PageInfo<>(memberList);

        return new PageUtils(memberList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:member:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:member:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:member:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Member member) {

        member.setCreateDate(new Date());
        member.setDelFlag("0");
        this.memberService.save(member);
        return ResultGenerator.genSuccessResult();
    }

    
    @RequiresPermissions("member:member:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Member member) {
        member.setUpdateDate(new Date());
        this.memberService.update(member);

        return ResultGenerator.genSuccessResult();
    }

    
    @RequiresPermissions("member:member:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        Member member = this.memberService.findById(id);
        member.setDelFlag("1");

        this.memberService.update(member);

        return ResultGenerator.genSuccessResult();
    }


    
    @RequiresPermissions("member:member:disable")
    @PostMapping("/disable")
    @ResponseBody()
    Result<String> disable(Integer id) {

        Member member = this.memberService.findById(id);
        member.setEnable("1");

        this.memberService.update(member);


        return ResultGenerator.genSuccessResult();
    }


    
    @RequiresPermissions("member:member:recovery")
    @PostMapping("/recovery")
    @ResponseBody()
    Result<String> recovery(Integer id) {

        Member member = this.memberService.findById(id);
        member.setEnable("0");

        this.memberService.update(member);


        return ResultGenerator.genSuccessResult();
    }



    /*
    @RequiresPermissions("member:member:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] memberIds) {
        if (memberIds == null || memberIds.length == 0) {
            return ResultGenerator.genSuccessResult();
        }

        String memberIdStr = Stream.of(memberIds).map(Object::toString).collect(Collectors.joining(","));
        this.memberService.deleteByIds(memberIdStr);

        return ResultGenerator.genSuccessResult();
    }*/



}
