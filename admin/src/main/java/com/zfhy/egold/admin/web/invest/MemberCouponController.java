package com.zfhy.egold.admin.web.invest;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.invest.dto.CouponStatus;
import com.zfhy.egold.domain.invest.dto.CouponType;
import com.zfhy.egold.domain.invest.dto.MemberCouponListDto;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.invest.service.MemberCouponService;
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
@RequestMapping("/invest/memberCoupon")
@Slf4j
@Validated
public class MemberCouponController {
    private String prefix = "invest/memberCoupon";
    @Autowired
    private MemberCouponService memberCouponService;


    @RequiresPermissions("invest:memberCoupon:memberCoupon")
    @GetMapping()
    String memberCoupon(Model model) {
        model.addAttribute("couponStatusList", CouponStatus.values());
        model.addAttribute("couponTypes", CouponType.values());
        return prefix + "/memberCoupon";
    }



    
    @RequiresPermissions("invest:memberCoupon:memberCoupon")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
       /* Condition condition = new Condition(MemberCoupon.class);
        SqlUtil.genSqlCondition(params, condition);*/

        List<MemberCouponListDto> memberCouponList = this.memberCouponService.list(params);
        PageInfo<MemberCouponListDto> pageInfo = new PageInfo<>(memberCouponList);

        return new PageUtils(memberCouponList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("invest:memberCoupon:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("invest:memberCoupon:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        MemberCoupon memberCoupon = memberCouponService.findById(id);
        model.addAttribute("memberCoupon", memberCoupon);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("invest:memberCoupon:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(MemberCoupon memberCoupon) {

        memberCoupon.setCreateDate(new Date());
        memberCoupon.setDelFlag("0");
        this.memberCouponService.save(memberCoupon);
        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:memberCoupon:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(MemberCoupon memberCoupon) {
        memberCoupon.setUpdateDate(new Date());
        this.memberCouponService.update(memberCoupon);

        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:memberCoupon:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.memberCouponService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("invest:memberCoupon:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] memberCouponIds) {
        if (memberCouponIds == null || memberCouponIds.length == 0) {
            return genSuccessResult();
        }

        String memberCouponIdStr = Stream.of(memberCouponIds).map(Object::toString).collect(Collectors.joining(","));
        this.memberCouponService.deleteByIds(memberCouponIdStr);

        return genSuccessResult();
    }



}
