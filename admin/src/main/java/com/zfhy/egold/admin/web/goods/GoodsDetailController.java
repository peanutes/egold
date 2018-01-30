package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.goods.entity.GoodsDetail;
import com.zfhy.egold.domain.goods.service.GoodsDetailService;
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
@RequestMapping("/goods/goodsDetail")
@Slf4j
@Validated
public class GoodsDetailController {
    private String prefix = "goods/goodsDetail";
    @Autowired
    private GoodsDetailService goodsDetailService;


    @RequiresPermissions("goods:goodsDetail:goodsDetail")
    @GetMapping()
    String goodsDetail() {
        return prefix + "/goodsDetail";
    }



    
    @RequiresPermissions("goods:goodsDetail:goodsDetail")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(GoodsDetail.class);
        SqlUtil.genSqlCondition(params, condition);

        List<GoodsDetail> goodsDetailList = this.goodsDetailService.findByCondition(condition);
        PageInfo<GoodsDetail> pageInfo = new PageInfo<>(goodsDetailList);

        return new PageUtils(goodsDetailList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:goodsDetail:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("goods:goodsDetail:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        GoodsDetail goodsDetail = goodsDetailService.findById(id);
        model.addAttribute("goodsDetail", goodsDetail);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("goods:goodsDetail:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<Integer> save(GoodsDetail goodsDetail) {

        if (Objects.nonNull(goodsDetail.getId())) {
            
            goodsDetail.setUpdateDate(new Date());
            this.goodsDetailService.update(goodsDetail);

        } else {
            
            goodsDetail.setCreateDate(new Date());
            goodsDetail.setUpdateDate(new Date());
            goodsDetail.setDelFlag("0");
            this.goodsDetailService.save(goodsDetail);
        }

        return genSuccessResult(goodsDetail.getId());
    }

    
    @RequiresPermissions("goods:goodsDetail:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(GoodsDetail goodsDetail) {
        goodsDetail.setUpdateDate(new Date());
        this.goodsDetailService.update(goodsDetail);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsDetail:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.goodsDetailService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:goodsDetail:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] goodsDetailIds) {
        if (goodsDetailIds == null || goodsDetailIds.length == 0) {
            return genSuccessResult();
        }

        String goodsDetailIdStr = Stream.of(goodsDetailIds).map(Object::toString).collect(Collectors.joining(","));
        this.goodsDetailService.deleteByIds(goodsDetailIdStr);

        return genSuccessResult();
    }



}
