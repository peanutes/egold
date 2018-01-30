package com.zfhy.egold.admin.web.member;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.member.dto.BankcardListDto;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.IdcardService;
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
@RequestMapping("/member/bankcard")
@Slf4j
@Validated
public class BankcardController {
    private String prefix = "member/bankcard";
    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private MemberService memberService;


    @Autowired
    private IdcardService idcardService;


    @RequiresPermissions("member:bankcard:bankcard")
    @GetMapping()
    String bankcard() {
        return prefix + "/bankcard";
    }



    
    @RequiresPermissions("member:bankcard:bankcard")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));


        List<BankcardListDto> bankcardListDtos = this.bankcardService.list(params);

        PageInfo<BankcardListDto> pageInfo = new PageInfo<>(bankcardListDtos);

        return new PageUtils(bankcardListDtos, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("member:bankcard:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("member:bankcard:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Bankcard bankcard = bankcardService.findById(id);
        model.addAttribute("bankcard", bankcard);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("member:bankcard:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Bankcard bankcard) {

        bankcard.setCreateDate(new Date());
        bankcard.setDelFlag("0");
        this.bankcardService.save(bankcard);
        return genSuccessResult();
    }

    
    @RequiresPermissions("member:bankcard:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Bankcard bankcard) {
        bankcard.setUpdateDate(new Date());
        this.bankcardService.update(bankcard);

        return genSuccessResult();
    }

    
    @RequiresPermissions("member:bankcard:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.bankcardService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("member:bankcard:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] bankcardIds) {
        if (bankcardIds == null || bankcardIds.length == 0) {
            return genSuccessResult();
        }

        String bankcardIdStr = Stream.of(bankcardIds).map(Object::toString).collect(Collectors.joining(","));
        this.bankcardService.deleteByIds(bankcardIdStr);

        return genSuccessResult();
    }



}
