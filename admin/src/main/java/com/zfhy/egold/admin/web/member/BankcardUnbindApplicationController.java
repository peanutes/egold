package com.zfhy.egold.admin.web.member;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.member.entity.BankcardUnbindApplication;
import com.zfhy.egold.domain.member.service.BankcardUnbindApplicationService;
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
@RequestMapping("/member/bankcardUnbindApplication")
@Slf4j
@Validated
public class BankcardUnbindApplicationController {
    private String prefix = "member/bankcardUnbindApplication";
    @Autowired
    private BankcardUnbindApplicationService bankcardUnbindApplicationService;


    @RequiresPermissions("member:bankcardUnbindApplication:bankcardUnbindApplication")
    @GetMapping()
    String bankcardUnbindApplication() {
        return prefix + "/bankcardUnbindApplication";
    }



    
    @RequiresPermissions("member:bankcardUnbindApplication:bankcardUnbindApplication")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(BankcardUnbindApplication.class);
        SqlUtil.genSqlCondition(params, condition);

        List<BankcardUnbindApplication> bankcardUnbindApplicationList = this.bankcardUnbindApplicationService.findByCondition(condition);
        PageInfo<BankcardUnbindApplication> pageInfo = new PageInfo<>(bankcardUnbindApplicationList);

        return new PageUtils(bankcardUnbindApplicationList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:bankcardUnbindApplication:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:bankcardUnbindApplication:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        BankcardUnbindApplication bankcardUnbindApplication = bankcardUnbindApplicationService.findById(id);
        model.addAttribute("bankcardUnbindApplication", bankcardUnbindApplication);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:bankcardUnbindApplication:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(BankcardUnbindApplication bankcardUnbindApplication) {

        bankcardUnbindApplication.setCreateDate(new Date());
        bankcardUnbindApplication.setDelFlag("0");
        this.bankcardUnbindApplicationService.save(bankcardUnbindApplication);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:bankcardUnbindApplication:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(BankcardUnbindApplication bankcardUnbindApplication) {
        bankcardUnbindApplication.setUpdateDate(new Date());
        this.bankcardUnbindApplicationService.update(bankcardUnbindApplication);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:bankcardUnbindApplication:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.bankcardUnbindApplicationService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:bankcardUnbindApplication:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] bankcardUnbindApplicationIds) {
        if (bankcardUnbindApplicationIds == null || bankcardUnbindApplicationIds.length == 0) {
            return genSuccessResult();
        }

        String bankcardUnbindApplicationIdStr = Stream.of(bankcardUnbindApplicationIds).map(Object::toString).collect(Collectors.joining(","));
        this.bankcardUnbindApplicationService.deleteByIds(bankcardUnbindApplicationIdStr);

        return genSuccessResult();
    }


    
    @RequiresPermissions("member:bankcardUnbindApplication:approved")
    @PostMapping("/approved")
    @ResponseBody()
    Result<String> approved(Integer id) {

        BankcardUnbindApplication application = this.bankcardUnbindApplicationService.findById(id);
        application.setStatus(1);
        this.bankcardUnbindApplicationService.approved(id);

        return genSuccessResult();
    }


    
    @RequiresPermissions("member:bankcardUnbindApplication:reject")
    @PostMapping("/reject")
    @ResponseBody()
    Result<String> reject(Integer id) {

        BankcardUnbindApplication application = this.bankcardUnbindApplicationService.findById(id);
        application.setStatus(2);
        this.bankcardUnbindApplicationService.update(application);

        return genSuccessResult();
    }


}
