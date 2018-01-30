package com.zfhy.egold.wap.web.front.pc;

import com.google.gson.Gson;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.common.util.SessionUtil;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.fund.entity.MemberFund;
import com.zfhy.egold.domain.fund.service.MemberFundService;
import com.zfhy.egold.domain.invest.dto.PcHomeDto;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.dto.PcAccountOverView;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
@Validated
public class PcIndexController {
    private String prefix = "pc";

    @Autowired
    private RedisService redisService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFundService memberFundService;

    @Autowired
    private FinancialProductService financialProductService;

    @Autowired
    private HotInfoService hotInfoService;

    
    @GetMapping({"", "/", "/index", "/pc"})
    public String index(Model model) {
        PcHomeDto pcHomeDto = this.financialProductService.pcHome();

        model.addAttribute("currentPrice", DoubleUtil.toString(this.redisService.getRealTimePrice()));
        model.addAttribute("pcHomeDto", pcHomeDto);

        return prefix + "/index";
    }


    
    @GetMapping("/pc/securityGuard")
    public String securityGuard(Model model) {
        return prefix + "/security_guard";
    }


    
    @GetMapping("/pc/understandUs")
    public String understandUs(Model model) {
        return prefix + "/understand_us";
    }


    
    @GetMapping("/pc/myAccount")
    public String myAccount(Model model) {

        MemberSession member;
        try {
            member = this.redisService.checkAndGetMemberToken("");

        } catch (Throwable throwable) {
            return prefix + "/login";
        }


        List<PcAccountOverView> accountOverViews = this.memberService.getPcAccountOverview(member.getId());
        List<String> accountLabels = accountOverViews.stream().map(PcAccountOverView::getName).collect(Collectors.toList());

        model.addAttribute("accountOverVies", new Gson().toJson(accountOverViews));
        model.addAttribute("accountLabels", new Gson().toJson(accountLabels));


        return prefix + "/myaccount";
    }


    
    @GetMapping("/pc/gotoRecharge")
    public String gotoRecharge(Model model) {
        MemberSession memberSession = SessionUtil.getSession();
        if (Objects.isNull(memberSession)) {
            
            return prefix + "/login";
        }

        MemberFund memberFund = this.memberFundService.findBy("memberId", memberSession.getId());


        model.addAttribute("cashBalance", DoubleUtil.toString(Objects.nonNull(memberFund) ? memberFund.getCashBalance() : 0.0D));
        return prefix + "/recharge";
    }


    
    @GetMapping("/pc/gotoLoading")
    public String gotoLoading(Model model) {
        return prefix + "/loading";
    }

    
    @GetMapping("/pc/gotoGoldPrice")
    public String gotoGoldPrice(Model model) {
        return prefix + "/gold_price";
    }


    
    @GetMapping("/pc/articleContent")
    public String articleContent(
            @ApiParam(name = "id", value = "文章id", required = true) @NotNull @RequestParam Integer id,
            Model model) {

        HotInfo hotinfo = this.hotInfoService.findContentById(id);

        model.addAttribute("hotinfo", hotinfo);

        return prefix + "/news_content";
    }

    
    @GetMapping("/pc/teamIntroduction")
    public String teamIntroduction(Model model) {
        return prefix + "/team_introduction";
    }

    
    @GetMapping("/pc/specialist")
    public String specialist(Model model) {
        return prefix + "/specialist";
    }

    
    @GetMapping("/pc/contactUs")
    public String contactUs(Model model) {
        return prefix + "/contact_us";
    }

    
    @GetMapping("/pc/aboutUs")
    public String aboutUs(Model model) {
        return prefix + "/about_us";
    }

    
    @GetMapping("/pc/understandProduct")
    public String understandProduct(Model model) {
        return prefix + "/understand_product";
    }


}
