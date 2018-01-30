package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.goods.entity.GoodsProperty;
import com.zfhy.egold.domain.goods.service.GoodsPropertyService;
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
@RequestMapping("/goods/goodsProperty")
@Slf4j
@Validated
public class GoodsPropertyController {
    private String prefix = "goods/goodsProperty";
    @Autowired
    private GoodsPropertyService goodsPropertyService;


    @RequiresPermissions("goods:goodsProperty:goodsProperty")
    @GetMapping()
    String goodsProperty() {
        return prefix + "/goodsProperty";
    }



    
    @RequiresPermissions("goods:goodsProperty:goodsProperty")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(GoodsProperty.class);
        SqlUtil.genSqlCondition(params, condition);

        List<GoodsProperty> goodsPropertyList = this.goodsPropertyService.findByCondition(condition);
        PageInfo<GoodsProperty> pageInfo = new PageInfo<>(goodsPropertyList);

        return new PageUtils(goodsPropertyList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:goodsProperty:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("goods:goodsProperty:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        GoodsProperty goodsProperty = goodsPropertyService.findById(id);
        model.addAttribute("goodsProperty", goodsProperty);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("goods:goodsProperty:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<List<GoodsProperty>> save(@RequestBody List<GoodsProperty> goodsPropertys, @RequestParam Integer spuId) {

        Map<Boolean, List<GoodsProperty>> goodsPropertyPars = goodsPropertys.stream().peek(e -> {
            e.setSpuId(spuId);
            if (Objects.isNull(e.getId())) {
                e.setCreateDate(new Date());
                e.setDelFlag("0");
            }
            e.setUpdateDate(new Date());
        }).collect(Collectors.partitioningBy(e -> Objects.nonNull(e.getId())));

        List<GoodsProperty> exists = goodsPropertyPars.get(true);
        List<GoodsProperty> notExists = goodsPropertyPars.get(false);



        this.goodsPropertyService.saveOrUpdate(exists, notExists, spuId);
        return genSuccessResult(goodsPropertys);
    }

    
    @RequiresPermissions("goods:goodsProperty:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(GoodsProperty goodsProperty) {
        goodsProperty.setUpdateDate(new Date());
        this.goodsPropertyService.update(goodsProperty);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsProperty:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.goodsPropertyService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:goodsProperty:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] goodsPropertyIds) {
        if (goodsPropertyIds == null || goodsPropertyIds.length == 0) {
            return genSuccessResult();
        }

        String goodsPropertyIdStr = Stream.of(goodsPropertyIds).map(Object::toString).collect(Collectors.joining(","));
        this.goodsPropertyService.deleteByIds(goodsPropertyIdStr);

        return genSuccessResult();
    }



}
