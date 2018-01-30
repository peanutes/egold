package com.zfhy.egold.admin.web.cms;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.cms.dto.BannerCode;
import com.zfhy.egold.domain.cms.entity.Banner;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LAI on 2017/9/26.
 */
@RequestMapping("/cms/banner")
@Controller
@Validated
public class BannerController {
    String prefix = "cms/banner";
    @Autowired
    BannerService bannerService;

    @Autowired
    private FinancialProductService financialProductService;


    @RequiresPermissions("cms:banner:banner")
    @GetMapping()
    String banner(Model model) {
        model.addAttribute("allBannerCode", BannerCode.values());
        return prefix + "/banner";
    }


    @RequiresPermissions("cms:banner:banner")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Banner.class);
        Example.Criteria criteria = condition.createCriteria();
        List<Map.Entry<String, String>> parameters = params.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .filter(entry -> !"page".equalsIgnoreCase(entry.getKey()) && !"size".equalsIgnoreCase(entry.getKey()))
                .collect(Collectors.toList());

        for (Map.Entry<String, String> entry : parameters) {
            criteria.andEqualTo(entry.getKey(), entry.getValue());
        }

        List<Banner> bannerList = this.bannerService.findByCondition(condition);
        PageInfo<Banner> pageInfo = new PageInfo<>(bannerList);

        return new PageUtils(bannerList, (int)pageInfo.getTotal());
    }

    @RequiresPermissions("cms:banner:add")
    @GetMapping("/add")
    String add(Model model) {
        List<FinancialProduct> productList = this.financialProductService.findAllProduct();
        Map<Integer, String> productMap = productList.stream().collect(Collectors.toMap(FinancialProduct::getId, FinancialProduct::getProductName));
        model.addAttribute("allBannerCode", BannerCode.getAllBanner());
        model.addAttribute("productMap", productMap);

        return prefix + "/add";
    }

    @RequiresPermissions("cms:banner:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Banner banner = bannerService.findById(id);
        List<FinancialProduct> productList = this.financialProductService.findAllProduct();
        Map<Integer, String> productMap = productList.stream().collect(Collectors.toMap(FinancialProduct::getId, FinancialProduct::getProductName));


        model.addAttribute("banner", banner);
        model.addAttribute("allBannerCode", BannerCode.values());
        model.addAttribute("productMap", productMap);

        return prefix + "/edit";
    }

    @RequiresPermissions("cms:banner:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Banner banner) {

        banner.setCreateDate(new Date());
        this.bannerService.save(banner);
        return ResultGenerator.genSuccessResult();
    }

    @RequiresPermissions("cms:banner:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Banner banner) {
        this.bannerService.update(banner);

        return ResultGenerator.genSuccessResult();
    }

    @RequiresPermissions("cms:banner:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {
        

        this.bannerService.deleteById(id);

        return ResultGenerator.genSuccessResult();
    }


    @RequiresPermissions("cms:banner:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return ResultGenerator.genSuccessResult();
        }

        String userIdStr = Stream.of(userIds).map(Object::toString).collect(Collectors.joining(","));
        this.bannerService.deleteByIds(userIdStr);

        return ResultGenerator.genSuccessResult();
    }


}
