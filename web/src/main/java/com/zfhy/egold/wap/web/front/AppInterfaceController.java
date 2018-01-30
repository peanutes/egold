package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.cms.dto.HotInfoDetailDto;
import com.zfhy.egold.domain.cms.dto.HotInfoDto;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.ArticleType;
import com.zfhy.egold.domain.invest.dto.BuyGoldDto;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app")
@Slf4j
@Validated
public class AppInterfaceController {
    private String PREFIX = "app";

    @Resource
    private BannerService bannerService;

    @Autowired
    private HotInfoService hotInfoService;

    @Resource
    private FinancialProductService financialProductService;

    @Resource
    private InvestRecordService investRecordService;

    @Autowired
    private DictService dictService;

    
    @GetMapping("/helpCenter")
    public String helpCenter(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType,
                             Model model) {

        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("articleType", ArticleType.HELP_DESK.getCode())
                .andEqualTo("status", 1);

        condition.orderBy("sort");

        List<HotInfo> list = hotInfoService.findByCondition(condition);

        String hostInfoUrl = this.dictService.findStringByType(DictType.HOT_INFO_ARTICLE_URL);


        List<HotInfoDto> dtoList = list.stream().map(ele -> {
            HotInfoDto hotInfoDto = new HotInfoDto().convertFrom(ele);
            hotInfoDto.setHref(String.join("", hostInfoUrl, "?id=", hotInfoDto.getId() + ""));
            return hotInfoDto;

        }).collect(Collectors.toList());

        model.addAttribute("articleList", dtoList);

        return PREFIX + "/help_center";
    }



    
    @GetMapping("/invitation")
    public String invitation(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {
        model.addAttribute("terminalType", terminalType);
        return PREFIX + "/invitation";
    }

    
    @GetMapping("/invitationGift")
    public String invitationGift(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {
        return PREFIX + "/invitation_gift";
    }

    
    @GetMapping("/aboutUs")
    public String aboutUs(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {
        return PREFIX + "/understand_us";
    }

    
    @GetMapping("/security")
    public String security(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {
        return PREFIX + "/security_guard";
    }

    
    @GetMapping("/goldTrend")
    public String goldTrend(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        BuyGoldDto buyGoldDto = this.financialProductService.productList();
        model.addAttribute("buyGold", buyGoldDto);
        model.addAttribute("terminalType", terminalType);

        return PREFIX + "/gold_trend_4_app";
    }

    
    @GetMapping("/goldTrendOnly")
    public String goldTrendOnly(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        BuyGoldDto buyGoldDto = this.financialProductService.productList();
        model.addAttribute("buyGold", buyGoldDto);

        return PREFIX + "/gold_trend_only_4_app";
    }

    
    @GetMapping("/newsDetail")
    public String newsDetail(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, @ApiParam(name = "id", value = "资讯id") @NotNull @RequestParam Integer id, Model model) {
        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", 1)
                .andEqualTo("id", id);

        HotInfo hotInfo = hotInfoService.fineOneByCondition(condition);
        model.addAttribute("detail", new HotInfoDetailDto().convertFrom(hotInfo));

        return PREFIX + "/hotinfo_detail_4_app";
    }

    
    @GetMapping("/infoContent")
    public String infoContent(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, @ApiParam(name = "id", value = "资讯id") @NotNull @RequestParam Integer id, Model model) {
        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", 1)
                .andEqualTo("id", id);

        HotInfo hotInfo = hotInfoService.fineOneByCondition(condition);
        model.addAttribute("detail", new HotInfoDetailDto().convertFrom(hotInfo));

        return PREFIX + "/info_content_only_4_app";
    }

    
    @GetMapping("/article")
    public String articleByType(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, @ApiParam(name = "articleType", value = "文章类型", required = true) @NotNull @RequestParam ArticleType articleType, Model model) {

        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("articleType", articleType.getCode())
                .andEqualTo("status", 1);

        condition.orderBy("sort");

        List<HotInfo> list = hotInfoService.findByCondition(condition);

        String hostInfoUrl = this.dictService.findStringByType(DictType.HOT_INFO_ARTICLE_URL);

        if (CollectionUtils.isEmpty(list)) {
            throw new BusException("文章不存在");
        }

        HotInfo hotInfo = list.get(0);
        HotInfoDto hotInfoDto = new HotInfoDto().convertFrom(hotInfo);
        hotInfoDto.setHref(String.join("", hostInfoUrl, "?id=", hotInfoDto.getId() + ""));

        model.addAttribute("detail", hotInfoDto);

        return PREFIX + "/hotinfo_detail_info_by_type_4_app";
    }

    
    @GetMapping("/productIntroduction")
    public String productIntroduction(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        return PREFIX + "/production_introduction";
    }

    
    @GetMapping("/newUserInterest")
    public String newUserInterst(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        FinancialProduct newUserProduct =this.financialProductService.findNewUserProduct();
        model.addAttribute("newUserProduct", newUserProduct);
        return PREFIX + "/new_user_interest";
    }

    
    @GetMapping("/newUserSpecialPriceGold")
    public String newUserSpecialPriceGold(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {
        FinancialProduct newUserProduct =this.financialProductService.findNewUserProduct();
        List<InvestRecord> investRecordList = new ArrayList<>();

        if(null != newUserProduct){
            investRecordList = this.investRecordService.buyHistory(newUserProduct.getId());
            if(investRecordList.isEmpty() || investRecordList.size() < 4){
                InvestRecord investRecord = new InvestRecord();
                investRecord.setMobile("15313375086");
                investRecordList.add(investRecord);

                investRecord = new InvestRecord();
                investRecord.setMobile("13113374268");
                investRecordList.add(investRecord);


                investRecord = new InvestRecord();
                investRecord.setMobile("18913375850");
                investRecordList.add(investRecord);


                investRecord = new InvestRecord();
                investRecord.setMobile("13513375081");
                investRecordList.add(investRecord);


                investRecord = new InvestRecord();
                investRecord.setMobile("13413375083");
                investRecordList.add(investRecord);
            }
        }

        model.addAttribute("newUserProduct", newUserProduct);
        model.addAttribute("investRecordList", investRecordList);

        return PREFIX + "/new_user_special_price_gold";
    }

    
    @GetMapping("/newUserBuyGoldTicket")
    public String newUserBuyGoldTicket(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        return PREFIX + "/new_user_buy_gold_ticket";
    }

    
    @GetMapping("/millionGoldInterest")
    public String millionGoldInterest(@ApiParam(name = "terminalType", value = "终端端类型:android, iphone") @RequestParam String terminalType, Model model) {

        return PREFIX + "/million_gold_interest";
    }
}
