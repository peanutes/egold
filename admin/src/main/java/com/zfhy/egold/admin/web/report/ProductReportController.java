package com.zfhy.egold.admin.web.report;

import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.order.service.OrderService;
import com.zfhy.egold.domain.report.dto.ProductStatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by LAI on 2018/1/6.
 */
@Controller
@RequestMapping("/report/product")
@Slf4j
@Validated
public class ProductReportController {
    private String prefix = "report/product";

    @Autowired
    private OrderService orderService;


    @RequiresPermissions("report:product:productBuyerStatistics")
    @GetMapping()
    String productReport() {
        return prefix + "/productReport";
    }


    
    @RequiresPermissions("report:product:productBuyerStatistics")
    @GetMapping("/productBuyerStatistics")
    @ResponseBody
    PageUtils productBuyerStatistics() {

        List<ProductStatisticsDto> productStatisticsDtos = this.orderService.productBuyerStatistics();

        ProductStatisticsDto summary = new ProductStatisticsDto();
        Integer buyerCount = 0;
        Integer buyerTimes = 0;
        Double investAmount = 0D;
        for (ProductStatisticsDto dto : productStatisticsDtos) {
            buyerCount += dto.getBuyerCount();
            buyerTimes += dto.getBuyerTimes();
            investAmount += dto.getInvestAmount();

        }

        summary.setProductName("合计");
        summary.setBuyerCount(buyerCount);
        summary.setBuyerTimes(buyerTimes);
        summary.setInvestAmount(DoubleUtil.changeDecimal(investAmount, 2));
        productStatisticsDtos.add(summary);

        return new PageUtils(productStatisticsDtos, productStatisticsDtos.size());
    }

}
