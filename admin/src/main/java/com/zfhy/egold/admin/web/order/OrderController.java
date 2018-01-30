package com.zfhy.egold.admin.web.order;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.order.dto.OrderStatus;
import com.zfhy.egold.domain.order.dto.PayType;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.service.OrderService;
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
@RequestMapping("/order/order")
@Slf4j
@Validated
public class OrderController {
    private String prefix = "order/order";
    @Autowired
    private OrderService orderService;


    @RequiresPermissions("order:order:order")
    @GetMapping()
    String order(Model model) {
        model.addAttribute("statusList", OrderStatus.values());
        return prefix + "/order";
    }



    
    @RequiresPermissions("order:order:order")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
       /* Condition condition = new Condition(Order.class);
        SqlUtil.genSqlCondition(params, condition);*/

        List<Order> orderList = this.orderService.list(params);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        return new PageUtils(orderList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("order:order:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("order:order:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("payTypes", PayType.values());
        model.addAttribute("allFinancialProductType", FinancialProductType.values());

        return prefix + "/edit";
    }


    
    @RequiresPermissions("order:order:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Order order) {

        order.setCreateDate(new Date());
        order.setDelFlag("0");
        this.orderService.save(order);
        return genSuccessResult();
    }

    
    @RequiresPermissions("order:order:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Order order) {
        order.setUpdateDate(new Date());
        this.orderService.update(order);

        return genSuccessResult();
    }

    
    @RequiresPermissions("order:order:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.orderService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("order:order:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] orderIds) {
        if (orderIds == null || orderIds.length == 0) {
            return genSuccessResult();
        }

        String orderIdStr = Stream.of(orderIds).map(Object::toString).collect(Collectors.joining(","));
        this.orderService.deleteByIds(orderIdStr);

        return genSuccessResult();
    }



}
