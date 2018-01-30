package com.zfhy.egold.wap.web.contract;

import com.google.common.collect.ImmutableMap;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.invest.dto.FinancialProductType;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.order.entity.Myorder;
import com.zfhy.egold.domain.order.entity.Order;
import com.zfhy.egold.domain.order.service.MyorderService;
import com.zfhy.egold.domain.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/contract")
@Slf4j
@Validated
public class ContractController {

    private String PREFIX = "contract";

    @Autowired
    private MyorderService myorderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private InvestRecordService investRecordService;

    private static Map<String, String> PRODUCTTYPE_MAP_RESULT = ImmutableMap.<String, String>builder()
            .put(FinancialProductType.CURRENT_DEPOSIT.name(), "/currentGoldContract")
            .put(FinancialProductType.TERM_DEPOSIT.name(), "/termGoldContract")
            .put(FinancialProductType.RISE_FALL.name(), "/riseOrFallContract")
            .build();




    
    @GetMapping("/viewContract")
    public String viewContract(
            @ApiParam(name = "orderId", value = "订单id：我的订单以及计单详情均有返回订单id", required = true)
            @RequestParam  Integer orderId,
            Model model) {

        log.info("orderId为>>>>:{}", orderId);

        Myorder myorder = this.myorderService.findById(orderId);
        if (Objects.isNull(myorder)) {
            return PREFIX + "/contract";
        }

        String relateOrderId = myorder.getRelateOrderId();

        if (StringUtils.isNotBlank(relateOrderId) && relateOrderId.startsWith("order_")) {
            int buyGoldOrderId = Integer.parseInt(relateOrderId.replace("order_", ""));
            Order order = this.orderService.findById(buyGoldOrderId);

            String realName = "";
            String cardNo = "";

            Idcard idcard = this.idcardService.findBy("memberId", order.getMemberId());

            if (Objects.nonNull(idcard)) {
                realName = idcard.getRealName();
                cardNo = idcard.getIdCard();
            }

            model.addAttribute("realName", realName);
            model.addAttribute("cardNo", cardNo);
            model.addAttribute("productName", order.getProductName());
            model.addAttribute("totalAmount", order.getTotalAmount());
            String financeProductType = order.getFinanceProductType();

            if (Objects.equals(FinancialProductType.TERM_DEPOSIT.name(), financeProductType) || Objects.equals(FinancialProductType.RISE_FALL.name(), financeProductType)) {

                
                model.addAttribute("goldWeight", order.getGoldWeight());
                InvestRecord investRecord = this.investRecordService.findBy("orderId", order.getId());

                if (Objects.nonNull(investRecord)) {
                    model.addAttribute("effectDate", DateUtil.toString(investRecord.getEffectDate(), DateUtil.YYYYMMDD_CN));

                    model.addAttribute("deadLineDate",DateUtil.toString(investRecord.getDeadlineDate(), DateUtil.YYYYMMDD_CN));
                    double totalRate = investRecord.getAnnualRate() + investRecord.getAdditionalRate();
                    model.addAttribute("annualRate", totalRate + "%");

                }

            }

            return PREFIX +  PRODUCTTYPE_MAP_RESULT.get(order.getFinanceProductType());
        }



        return PREFIX + "/contract";
    }


}
