package com.zfhy.egold.admin.web.order;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.order.dto.PaymentLogStatus;
import com.zfhy.egold.domain.order.dto.RechargeOrPay;
import com.zfhy.egold.domain.order.entity.PaymentLog;
import com.zfhy.egold.domain.order.service.PaymentLogService;
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
@RequestMapping("/order/paymentLog")
@Slf4j
@Validated
public class PaymentLogController {
    private String prefix = "order/paymentLog";
    @Autowired
    private PaymentLogService paymentLogService;


    @RequiresPermissions("order:paymentLog:paymentLog")
    @GetMapping()
    String paymentLog(Model model) {


        model.addAttribute("paymentLogStatus", PaymentLogStatus.values());
        model.addAttribute("payTypeList", RechargeOrPay.values());
        return prefix + "/paymentLog";
    }



    
    @RequiresPermissions("order:paymentLog:paymentLog")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));



        PageHelper.orderBy("create_date desc");
        List<PaymentLog> paymentLogList = this.paymentLogService.list(params);
        PageInfo<PaymentLog> pageInfo = new PageInfo<>(paymentLogList);

        return new PageUtils(paymentLogList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("order:paymentLog:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("order:paymentLog:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        PaymentLog paymentLog = paymentLogService.findById(id);
        model.addAttribute("paymentLog", paymentLog);

        return prefix + "/edit";
    }


    
    @RequiresPermissions("order:paymentLog:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(PaymentLog paymentLog) {

        paymentLog.setCreateDate(new Date());
        paymentLog.setDelFlag("0");
        this.paymentLogService.save(paymentLog);
        return genSuccessResult();
    }

    
    @RequiresPermissions("order:paymentLog:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(PaymentLog paymentLog) {
        paymentLog.setUpdateDate(new Date());
        this.paymentLogService.update(paymentLog);

        return genSuccessResult();
    }

    
    @RequiresPermissions("order:paymentLog:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.paymentLogService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("order:paymentLog:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] paymentLogIds) {
        if (paymentLogIds == null || paymentLogIds.length == 0) {
            return genSuccessResult();
        }

        String paymentLogIdStr = Stream.of(paymentLogIds).map(Object::toString).collect(Collectors.joining(","));
        this.paymentLogService.deleteByIds(paymentLogIdStr);

        return genSuccessResult();
    }



}
