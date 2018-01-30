package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.goods.service.SkuService;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zfhy.egold.common.core.result.ResultGenerator.genSuccessResult;


/**
* Created by CodeGenerator .
*/
@Controller
@RequestMapping("/goods/sku")
@Slf4j
@Validated
public class SkuController {
    private String prefix = "goods/sku";
    @Autowired
    private SkuService skuService;


    @RequiresPermissions("goods:sku:sku")
    @GetMapping()
    String sku() {
        return prefix + "/sku";
    }



    
    @RequiresPermissions("goods:sku:sku")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Sku.class);
        SqlUtil.genSqlCondition(params, condition);

        List<Sku> skuList = this.skuService.findByCondition(condition);
        PageInfo<Sku> pageInfo = new PageInfo<>(skuList);

        return new PageUtils(skuList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:sku:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("goods:sku:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Sku sku = skuService.findById(id);
        model.addAttribute("sku", sku);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("goods:sku:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<List<Sku>> save(@RequestBody List<Sku> skus, @RequestParam Integer spuId) {

        Map<Boolean, List<Sku>> skuPars = skus.stream().peek(e -> {
            e.setSpuId(spuId);
            if (Objects.isNull(e.getId())) {
                e.setVersion(1);
                e.setCreateDate(new Date());
                e.setDelFlag("0");
            }
            e.setUpdateDate(new Date());
        }).collect(Collectors.partitioningBy(e -> Objects.nonNull(e.getId())));

        List<Sku> exists = skuPars.get(true);
        List<Sku> notExists = skuPars.get(false);



        this.skuService.saveOrUpdate(exists, notExists, spuId);
        return genSuccessResult(skus);
    }

    
    @RequiresPermissions("goods:sku:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Sku sku) {
        sku.setUpdateDate(new Date());
        this.skuService.update(sku);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:sku:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.skuService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:sku:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] skuIds) {
        if (skuIds == null || skuIds.length == 0) {
            return genSuccessResult();
        }

        String skuIdStr = Stream.of(skuIds).map(Object::toString).collect(Collectors.joining(","));
        this.skuService.deleteByIds(skuIdStr);

        return genSuccessResult();
    }



}
