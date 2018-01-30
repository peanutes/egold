package com.zfhy.egold.admin.web.goods;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.goods.entity.GoodsOrder;
import com.zfhy.egold.domain.goods.service.GoodsOrderService;
import com.zfhy.egold.domain.order.dto.OrderStatus;
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
@RequestMapping("/goods/goodsOrder")
@Slf4j
@Validated
public class GoodsOrderController {
    private String prefix = "goods/goodsOrder";
    @Autowired
    private GoodsOrderService goodsOrderService;


    @RequiresPermissions("goods:goodsOrder:goodsOrder")
    @GetMapping()
    String goodsOrder() {
        return prefix + "/goodsOrder";
    }



    
    @RequiresPermissions("goods:goodsOrder:goodsOrder")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        /*Condition condition = new Condition(GoodsOrder.class);
        SqlUtil.genSqlCondition(params, condition);*/

        List<GoodsOrder> goodsOrderList = this.goodsOrderService.list(params);
        PageInfo<GoodsOrder> pageInfo = new PageInfo<>(goodsOrderList);

        return new PageUtils(goodsOrderList, (int)pageInfo.getTotal());
    }


    @RequiresPermissions("goods:goodsOrder:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("goods:goodsOrder:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        GoodsOrder goodsOrder = goodsOrderService.findById(id);
        model.addAttribute("goodsOrder", goodsOrder);
        model.addAttribute("orderStatusList", OrderStatus.values());

        return prefix + "/edit";
    }


    
    @RequiresPermissions("goods:goodsOrder:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(GoodsOrder goodsOrder) {

        goodsOrder.setCreateDate(new Date());
        goodsOrder.setDelFlag("0");
        this.goodsOrderService.save(goodsOrder);
        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsOrder:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(GoodsOrder goodsOrder) {
        goodsOrder.setUpdateDate(new Date());
        this.goodsOrderService.update(goodsOrder);

        return genSuccessResult();
    }

    
    @RequiresPermissions("goods:goodsOrder:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {

        this.goodsOrderService.deleteById(id);

        return genSuccessResult();
    }



    
    @RequiresPermissions("goods:goodsOrder:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] goodsOrderIds) {
        if (goodsOrderIds == null || goodsOrderIds.length == 0) {
            return genSuccessResult();
        }

        String goodsOrderIdStr = Stream.of(goodsOrderIds).map(Object::toString).collect(Collectors.joining(","));
        this.goodsOrderService.deleteByIds(goodsOrderIdStr);

        return genSuccessResult();
    }



}
