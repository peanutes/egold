package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.cms.dto.HotInfoDetailDto;
import com.zfhy.egold.domain.cms.dto.HotInfoDto;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.ArticleType;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
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
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/news")
@Slf4j
@Validated
public class NewsController {
    private String PREFIX_DISCOVER = "discover";
    private String PREFIX = "news";
    private String PREFIX_NEWS = "news";

    @Resource
    private BannerService bannerService;

    @Autowired
    private HotInfoService hotInfoService;

    @Resource
    private FinancialProductService financialProductService;


    @Autowired
    private DictService dictService;

    
    @GetMapping("/newsDetail")
    public String detail(@ApiParam(name = "id", value = "资讯id") @NotNull @RequestParam Integer id, Model model) {
        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", 1)
                .andEqualTo("id", id);

        HotInfo hotInfo = hotInfoService.fineOneByCondition(condition);

        model.addAttribute("detail", new HotInfoDetailDto().convertFrom(hotInfo));

        return PREFIX_NEWS + "/news_detail";
    }

    
    @GetMapping("/infoContent")
    public String infoContent(@ApiParam(name = "id", value = "资讯id") @NotNull @RequestParam Integer id, Model model) {
        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("status", 1)
                .andEqualTo("id", id);

        HotInfo hotInfo = hotInfoService.fineOneByCondition(condition);
        model.addAttribute("detail", new HotInfoDetailDto().convertFrom(hotInfo));

        return PREFIX_NEWS + "/info_content_only";
    }


    
    @GetMapping("/helpCenter")
    public String helpCenter(
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

    
    @GetMapping("/article")
    public String articleByType(@ApiParam(name = "articleType", value = "文章类型", required = true) @NotNull @RequestParam ArticleType articleType, Model model) {

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

        return PREFIX_NEWS + "/news_detail_4_type";
    }
}
