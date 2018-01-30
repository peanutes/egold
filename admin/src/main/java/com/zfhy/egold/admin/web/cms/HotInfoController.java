package com.zfhy.egold.admin.web.cms;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.ArticleType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zfhy.egold.common.core.result.ResultGenerator.genSuccessResult;


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/cms/hotInfo")
@Slf4j
@Validated
public class HotInfoController {
    private String prefix = "cms/hotInfo";
    @Autowired
    private HotInfoService hotInfoService;


    @RequiresPermissions("cms:hotInfo:hotInfo")
    @GetMapping()
    String hotInfo(Model model) {
        model.addAttribute("articleTypes", ArticleType.values());
        return prefix + "/hotInfo";
    }



    
    @RequiresPermissions("cms:hotInfo:hotInfo")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        params.put("delFlag", "0");
        Condition condition = new Condition(HotInfo.class);
        SqlUtil.genSqlCondition(params, condition);
        PageHelper.orderBy("sort");

        List<HotInfo> hotInfoList = this.hotInfoService.findByCondition(condition);
        PageInfo<HotInfo> pageInfo = new PageInfo<>(hotInfoList);

        return new PageUtils(hotInfoList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("cms:hotInfo:add")
    @GetMapping("/add")
    String add(Model model) {

        model.addAttribute("articleTypes", ArticleType.values());
        return prefix + "/add";
    }


    @RequiresPermissions("cms:hotInfo:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        HotInfo hotInfo = hotInfoService.findById(id);
        model.addAttribute("hotInfo", hotInfo);
        model.addAttribute("articleTypes", ArticleType.values());

        return prefix + "/edit";
    }


    
    @RequiresPermissions("cms:hotInfo:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(HotInfo hotInfo) {

        hotInfo.setCreateDate(new Date());
        hotInfo.setDelFlag("0");
        this.hotInfoService.save(hotInfo);
        return genSuccessResult();
    }

    
    @RequiresPermissions("cms:hotInfo:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(HotInfo hotInfo) {
        hotInfo.setUpdateDate(new Date());
        this.hotInfoService.update(hotInfo);

        return genSuccessResult();
    }

    
    @RequiresPermissions("cms:hotInfo:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        HotInfo hotInfo = this.hotInfoService.findById(id);
        hotInfo.setDelFlag("1");

        this.hotInfoService.update(hotInfo);

        return genSuccessResult();
    }



    
    @RequiresPermissions("cms:hotInfo:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] hotInfoIds) {
        if (hotInfoIds == null || hotInfoIds.length == 0) {
            return genSuccessResult();
        }


        this.hotInfoService.batchDelete(Arrays.asList(hotInfoIds));

        return genSuccessResult();
    }


    
    @RequiresPermissions("cms:hotInfo:publish")
    @PostMapping("/publish")
    @ResponseBody()
    Result<String> publish(Integer id) {

        HotInfo hotInfo = this.hotInfoService.findById(id);
        hotInfo.setStatus(1);

        this.hotInfoService.update(hotInfo);
        return genSuccessResult();
    }



}
