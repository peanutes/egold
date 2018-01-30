package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dto.FinancialProductDto;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.member.dto.MemberAccountDto;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/gold")
@Slf4j
@Validated
public class BuyGoldController {
    private String prefix = "gold";

    @Resource
    private BannerService bannerService;

    @Autowired
    private HotInfoService hotInfoService;

    @Autowired
    private DictService dictService;

    @Autowired
    private BankcardService bankcardService;

    @Resource
    private FinancialProductService financialProductService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisService redisService;


    
    @GetMapping("/goldDetail")
    public String fixedDetail(@ApiParam(name = "id", value = "理财产品id", required = true) @NotNull @RequestParam Integer id, Model model) {
        FinancialProduct product = this.financialProductService.findById(id);


        Double realTimePrice = this.redisService.getRealTimePrice();

        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        model.addAttribute("detail", new FinancialProductDto().convertFrom(product, currentYearPriceRisePercent, realTimePrice));
        return prefix + "/gold_detail";
    }

    
    @GetMapping("/goldOrder")
    public String goldOrder(@RequestParam Integer id, Model model) {

        FinancialProduct product = this.financialProductService.findById(id);


        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        Double realTimePrice = this.redisService.getRealTimePrice();

        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        String bankCardLastNumber = "";
        String bankCardName = "";
        String bankCardBindMobileMask = "";
        if (null != memberOutPutDto) {
            Bankcard bankCard = memberOutPutDto.getBankCardObject();
            if (null != bankCard) {
                String bankCardNumber = bankCard.getBankCard();
                String bindMobile = bankCard.getBindMobile();

                bankCardLastNumber = bankCardNumber.substring(bankCardNumber.length() - 4);
                bankCardName = bankCard.getBankName();
                bankCardBindMobileMask = bindMobile.substring(0, 3) + "****" + bindMobile.substring(7, 11);
            }

            MemberAccountDto memberAccountDto = this.memberService.getAccountOverview(memberOutPutDto.getId());

            memberOutPutDto.setMemberAccountDto(memberAccountDto);

            
            RequestUtil.getHttpServletSession().setAttribute("memberOutPutDto", memberOutPutDto);
        }

        model.addAttribute("bankCardName", bankCardName);

        model.addAttribute("bankCardLastNumber", bankCardLastNumber);

        model.addAttribute("bankCardBindMobile", bankCardBindMobileMask);

        model.addAttribute("product", product);

        return prefix + "/gold_order_form";
    }

    
    @GetMapping("/riseNfallDetil")
    public String riseNfallDetil(@ApiParam(name = "id", value = "理财产品id", required = true) @NotNull @RequestParam Integer id, Model model) {

        FinancialProduct product = this.financialProductService.findById(id);


        Double realTimePrice = this.redisService.getRealTimePrice();

        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        model.addAttribute("detail", new FinancialProductDto().convertFrom(product, currentYearPriceRisePercent, realTimePrice));

        return prefix + "/rise_fall_gold_detail";
    }

    
    @GetMapping("/riseNfallGoldOrder")
    public String riseNfallGoldOrder(@RequestParam Integer id, Model model) {

        FinancialProduct product = this.financialProductService.findById(id);


        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        Double realTimePrice = this.redisService.getRealTimePrice();

        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        String bankCardLastNumber = "";
        String bankCardName = "";
        String bankCardBindMobileMask = "";
        if (null != memberOutPutDto) {
            Bankcard bankCard = memberOutPutDto.getBankCardObject();
            if (null != bankCard) {
                String bankCardNumber = bankCard.getBankCard();
                String bindMobile = bankCard.getBindMobile();

                bankCardLastNumber = bankCardNumber.substring(bankCardNumber.length() - 4);
                bankCardName = bankCard.getBankName();
                bankCardBindMobileMask = bindMobile.substring(0, 3) + "****" + bindMobile.substring(7, 11);
            }

            MemberAccountDto memberAccountDto = this.memberService.getAccountOverview(memberOutPutDto.getId());

            memberOutPutDto.setMemberAccountDto(memberAccountDto);

            
            RequestUtil.getHttpServletSession().setAttribute("memberOutPutDto", memberOutPutDto);
        }

        model.addAttribute("bankCardName", bankCardName);

        model.addAttribute("bankCardLastNumber", bankCardLastNumber);

        model.addAttribute("bankCardBindMobile", bankCardBindMobileMask);

        model.addAttribute("product", product);

        return prefix + "/rise_fall_gold_order_form";
    }


}
