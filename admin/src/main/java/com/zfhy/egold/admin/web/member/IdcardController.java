package com.zfhy.egold.admin.web.member;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.service.IdcardService;
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
@RequestMapping("/member/idcard")
@Slf4j
@Validated
public class IdcardController {
    private String prefix = "member/idcard";
    @Autowired
    private IdcardService idcardService;


    @RequiresPermissions("member:idcard:idcard")
    @GetMapping()
    String idcard() {
        return prefix + "/idcard";
    }



    
    @RequiresPermissions("member:idcard:idcard")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Idcard.class);
        SqlUtil.genSqlCondition(params, condition);

        List<Idcard> idcardList = this.idcardService.findByCondition(condition);
        PageInfo<Idcard> pageInfo = new PageInfo<>(idcardList);

        return new PageUtils(idcardList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:idcard:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:idcard:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Idcard idcard = idcardService.findById(id);
        model.addAttribute("idcard", idcard);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:idcard:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Idcard idcard) {

        idcard.setCreateDate(new Date());
        idcard.setDelFlag("0");
        this.idcardService.save(idcard);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:idcard:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Idcard idcard) {
        idcard.setUpdateDate(new Date());
        this.idcardService.update(idcard);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:idcard:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.idcardService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:idcard:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] idcardIds) {
        if (idcardIds == null || idcardIds.length == 0) {
            return genSuccessResult();
        }

        String idcardIdStr = Stream.of(idcardIds).map(Object::toString).collect(Collectors.joining(","));
        this.idcardService.deleteByIds(idcardIdStr);

        return genSuccessResult();
    }



}
