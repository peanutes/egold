package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.goods.dto.*;
import com.zfhy.egold.domain.goods.entity.GoodsDetail;
import com.zfhy.egold.domain.goods.entity.Sku;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.domain.goods.service.*;
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
@RequestMapping("/goods/spu")
@Slf4j
@Validated
public class SpuController {
    private String prefix = "goods/spu";
    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private GoodsDetailService goodsDetailService;

    @Autowired
    private GoodsImgService goodsImgService;

    @Autowired
    private GoodsPropertyService goodsPropertyService;


    @RequiresPermissions("goods:spu:spu")
    @GetMapping()
    String spu(Model model) {
        model.addAttribute("spuTypes", SpuType.values());
        return prefix + "/spu";
    }



    
    @RequiresPermissions("goods:spu:spu")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        params.put("delFlag", "0");
        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Spu.class);
        SqlUtil.genSqlCondition(params, condition);

        List<Spu> spuList = this.spuService.findByCondition(condition);
        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);

        return new PageUtils(spuList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:spu:add")
    @GetMapping("/add")
    String add(Model model) {
        model.addAttribute("spuTypes", SpuType.values());
        return prefix + "/add";
    }


    @RequiresPermissions("goods:spu:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Spu spu = spuService.findById(id);
        model.addAttribute("spu", spu);
        model.addAttribute("spuTypes", SpuType.values());


        List<Sku> skuList = this.skuService.findBySpuId(id);
        List<SkuDto> skuDtoList = skuList.stream().map(e -> new SkuDto().convertFrom(e)).collect(Collectors.toList());
        model.addAttribute("skuList", skuDtoList);


        if (spu.getGoodsTypeId() == SpuType.WITHDRAW_GOLD.getCode()) {
            return prefix + "/withdrawGoldEdit";

        } else {

            GoodsDetail goodsDetail = this.goodsDetailService.findBySpuId(id);
            List<GoodsImgDto> goodsImgDtos = this.goodsImgService.findBySpuId(id);


            List<GoodsPropertyDto> goodsPropertyDtoList = this.goodsPropertyService.findBySpuId(id);


            model.addAttribute("detail", goodsDetail==null?new GoodsDetail():goodsDetail);
            model.addAttribute("imgList", goodsImgDtos);
            model.addAttribute("propertyList", goodsPropertyDtoList);
            return prefix + "/mallEdit";
        }

    }


    
    @RequiresPermissions("goods:spu:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<Integer> save(Spu spu) {


        if (Objects.isNull(spu.getId()) || Objects.equals(spu.getId(), 0)) {
            spu.setCreateDate(new Date());
            spu.setDelFlag("0");

            spu.setGoodsTypeName(SpuType.getNameByType(spu.getGoodsTypeId()));
            spu.setStatus(SpuStatus.DRAF.getCode());
            this.spuService.save(spu);
        } else {
            spu.setUpdateDate(new Date());
            this.spuService.update(spu);
        }
        return genSuccessResult(spu.getId());
    }

    
    @RequiresPermissions("goods:spu:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Spu spu) {
        spu.setUpdateDate(new Date());
        this.spuService.update(spu);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:spu:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        Spu spu = this.spuService.findById(id);
        if (Objects.nonNull(spu)) {
            spu.setDelFlag("1");
            this.spuService.update(spu);
        }


        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:spu:onLine")
    @PostMapping("/onLine")
    @ResponseBody()
    Result<String> onLine(Integer id) {

        Spu spu = this.spuService.findById(id);
        if (Objects.nonNull(spu)) {
            spu.setStatus(SpuStatus.ON.getCode());
            this.spuService.update(spu);
        }


        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:spu:offLine")
    @PostMapping("/offLine")
    @ResponseBody()
    Result<String> offLine(Integer id) {

        Spu spu = this.spuService.findById(id);
        if (Objects.nonNull(spu)) {
            spu.setStatus(SpuStatus.OFF.getCode());
            this.spuService.update(spu);
        }


        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:spu:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] spuIds) {
        if (spuIds == null || spuIds.length == 0) {
            return genSuccessResult();
        }

        String spuIdStr = Stream.of(spuIds).map(Object::toString).collect(Collectors.joining(","));


        return genSuccessResult();
    }



}
