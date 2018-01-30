package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.BuyGoldDto;
import com.zfhy.egold.domain.invest.dto.HomeFinancialProductDto;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/wap"})
@Slf4j
@Validated
public class IndexController {
    private String prefix = "main";

    @Resource
    private BannerService bannerService;

    @Autowired
    private HotInfoService hotInfoService;

    @Resource
    private FinancialProductService financialProductService;

    @Autowired
    private MemberService memberService;

    
    @GetMapping({"","/","/index"})
    public String index(Model model) {
        HomeFinancialProductDto homeFinancialProductDto = this.financialProductService.homeProducts();

        model.addAttribute("homeFinancialProduct", homeFinancialProductDto);

        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        boolean isNewUser = true;

        if (null != memberOutPutDto) {
            isNewUser = this.memberService.isNewUser(memberOutPutDto.getId());
        }
        model.addAttribute("isNewUser",isNewUser);

        return prefix + "/index";
    }

    
    @GetMapping("/buyGold")
    public String buyGold(Model model) {
        BuyGoldDto buyGoldDto = this.financialProductService.productList();
            model.addAttribute("buyGold", buyGoldDto);

        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        boolean isNewUser = true;

        if (null != memberOutPutDto) {
             isNewUser = this.memberService.isNewUser(memberOutPutDto.getId());
        }
        model.addAttribute("isNewUser",isNewUser);

        return prefix + "/buy_gold";
    }

    
    @GetMapping("/discover")
    public String discover(Model model) {
        return prefix + "/discover";
    }

    
    @GetMapping("/mine")
    public String mine(Model model, HttpServletRequest httpServletRequest) {
        if (null == RequestUtil.getHttpServletSession().getAttribute("auth_member_session")) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {
            return prefix + "/mine";
        }


    }

}
