package com.zfhy.egold.admin.web.invest;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.common.util.SqlUtil;
import com.zfhy.egold.domain.invest.dto.FinancialProductRisk;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.dto.RiseFallInvestStatus;
import com.zfhy.egold.domain.invest.dto.RiseOrFall;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
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
@RequestMapping("/invest/financialProduct")
@Slf4j
@Validated
public class FinancialProductController {
    private String prefix = "invest/financialProduct";
    @Autowired
    private FinancialProductService financialProductService;


    @RequiresPermissions("invest:financialProduct:financialProduct")
    @GetMapping()
    String financialProduct(Model model) {
        model.addAttribute("productTypes", FinancialProductType.values());

        return prefix + "/financialProduct";
    }



    
    @RequiresPermissions("invest:financialProduct:financialProduct")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(FinancialProduct.class);
        params.put("delFlag", "0");
        SqlUtil.genSqlCondition(params, condition);

        List<FinancialProduct> financialProductList = this.financialProductService.findByCondition(condition);
        PageInfo<FinancialProduct> pageInfo = new PageInfo<>(financialProductList);

        return new PageUtils(financialProductList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("invest:financialProduct:add")
    @GetMapping("/add")
    String add(Model model) {
        model.addAttribute("allFinancialProductType", FinancialProductType.values());
        model.addAttribute("allFinancialProductRisk", FinancialProductRisk.values());
        model.addAttribute("riseOrFall", RiseOrFall.values());

        return prefix + "/add";
    }


    @RequiresPermissions("invest:financialProduct:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        FinancialProduct financialProduct = financialProductService.findById(id);

        model.addAttribute("financialProduct", financialProduct);

        model.addAttribute("allFinancialProductType", FinancialProductType.values());
        model.addAttribute("allFinancialProductRisk", FinancialProductRisk.values());
        model.addAttribute("riseOrFall", RiseOrFall.values());

        return prefix + "/edit";
    }


    
    @RequiresPermissions("invest:financialProduct:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(FinancialProduct financialProduct) {

        financialProduct.setCreateDate(new Date());
        financialProduct.setDelFlag("0");
        if ("on".equalsIgnoreCase(financialProduct.getNewUser())) {
            financialProduct.setNewUser("1");
        } else {
            financialProduct.setNewUser("0");
        }

        financialProduct.setStatus("1");
        financialProduct.setVersion(1);
        financialProduct.setInvestStatus(RiseFallInvestStatus.PROCESSING.getCode());
        financialProduct.setHadInvestAmount(0D);

        this.financialProductService.save(financialProduct);
        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:financialProduct:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(FinancialProduct financialProduct) {
        financialProduct.setUpdateDate(new Date());
        if ("on".equalsIgnoreCase(financialProduct.getNewUser())) {
            financialProduct.setNewUser("1");
        } else {
            financialProduct.setNewUser("0");
        }
        this.financialProductService.update(financialProduct);

        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:financialProduct:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        FinancialProduct financialProduct = this.financialProductService.findById(id);
        financialProduct.setDelFlag("1");
        this.financialProductService.update(financialProduct);

        

        return genSuccessResult();
    }



    
    @RequiresPermissions("invest:financialProduct:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] financialProductIds) {
        if (financialProductIds == null || financialProductIds.length == 0) {
            return genSuccessResult();
        }



        this.financialProductService.batchDelete(Arrays.asList(financialProductIds));

        
        

        return genSuccessResult();
    }


    
    @RequiresPermissions("invest:financialProduct:stop")
    @PostMapping("/stop")
    @ResponseBody()
    Result<String> stop(Integer id) {

        FinancialProduct financialProduct = this.financialProductService.findById(id);
        financialProduct.setStatus("0");
        this.financialProductService.update(financialProduct);

        

        return genSuccessResult();
    }

    
    @RequiresPermissions("invest:financialProduct:up")
    @PostMapping("/up")
    @ResponseBody()
    Result<String> up(Integer id) {

        FinancialProduct financialProduct = this.financialProductService.findById(id);
        financialProduct.setStatus("1");
        this.financialProductService.update(financialProduct);


        return genSuccessResult();
    }



}
