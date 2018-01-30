package com.zfhy.egold.api.web.cms;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.cms.dto.BannerCode;
import com.zfhy.egold.domain.cms.dto.BannerDto;
import com.zfhy.egold.domain.cms.dto.HotInfoDto;
import com.zfhy.egold.domain.cms.dto.HotInfoListDto;
import com.zfhy.egold.domain.cms.entity.Banner;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import com.zfhy.egold.domain.cms.service.BannerService;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.invest.dto.ArticleType;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cms/cms")
@Api(value = "CmsController",tags = "CmsController", description = "文章资讯")
@Slf4j
@Validated
public class CmsController {
    @Resource
    private BannerService bannerService;

    @Autowired
    private HotInfoService hotInfoService;

    @Autowired
    private DictService dictService;


    
    @PostMapping("/banner")
    public Result<List<BannerDto>> banner(@ApiParam(name = "code", value = "类型（APP_HOME_BANNER=app首页banner，APP_ACTIVITY_BANNER=app活动banner）", required = true) @NotNull @RequestParam BannerCode code,
                                          @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String terminalType = sysParameterWithoutToken.getTerminalType();
        String terminalId = sysParameterWithoutToken.getTerminalId();

        Condition condition = new Condition(Banner.class);
        condition.createCriteria().andEqualTo("code", code.name());
        condition.orderBy("sort");

        List<Banner> bannerList = this.bannerService.findByCondition(condition);
        List<BannerDto> bannerDtoList = bannerList.stream().map(banner -> new BannerDto().convertFrom(banner, terminalType, terminalId)).collect(Collectors.toList());

        return ResultGenerator.genSuccessResult(bannerDtoList);
    }

    
    @PostMapping("/hotInfoList")
    public Result<Page<HotInfoListDto>> hotInfoList(@ApiParam(name = "page", value = "页数", required = true) @NotNull @RequestParam Integer page,
                                                @ApiParam(name = "size", value = "每页条数", required = true) @NotNull @RequestParam Integer size,
                                                @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String terminalType = sysParameterWithoutToken.getTerminalType();
        String terminalId = sysParameterWithoutToken.getTerminalId();

        PageHelper.startPage(page, size);

        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("articleType", ArticleType.HOST_INFO.getCode())
                .andEqualTo("status", 1);

        condition.setOrderByClause("sort asc, create_date desc ");

        List<HotInfo> list = hotInfoService.findByCondition(condition);

        String hostInfoUrl = this.dictService.findStringByType(DictType.HOT_INFO_ARTICLE_URL);

        PageInfo<HotInfo> pageInfo = new PageInfo<>(list);

        Page<HotInfoListDto> hotInfoDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageInfo, hotInfoDtoPageInfo);

        List<HotInfoListDto> dtoList = list.stream().map(ele -> {
            HotInfoListDto hotInfoDto = new HotInfoListDto().convertFrom(ele);
            hotInfoDto.setHref(StrFunUtil.assembleUrl(terminalType, terminalId, String.join("", hostInfoUrl, "?id=", hotInfoDto.getId() + "")));

            return hotInfoDto;

        }).collect(Collectors.toList());
        hotInfoDtoPageInfo.setList(dtoList);

        return ResultGenerator.genSuccessResult(hotInfoDtoPageInfo);
    }

    
    @PostMapping("/articleList")
    public Result<List<HotInfoListDto>> articleList(
            @ApiParam(name = "articleType", value = "文章类型", required = true) @NotNull @RequestParam ArticleType articleType,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String terminalType = sysParameterWithoutToken.getTerminalType();
        String terminalId = sysParameterWithoutToken.getTerminalId();


        Condition condition = new Condition(HotInfo.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("articleType", articleType.getCode())
                .andEqualTo("status", 1);

        condition.orderBy("sort");

        List<HotInfo> list = hotInfoService.findByCondition(condition);

        String hostInfoUrl = this.dictService.findStringByType(DictType.HOT_INFO_ARTICLE_URL);


        List<HotInfoListDto> dtoList = list.stream().map(ele -> {
            HotInfoListDto hotInfoDto = new HotInfoListDto().convertFrom(ele);
            hotInfoDto.setHref(StrFunUtil.assembleUrl(terminalType, terminalId, String.join("", hostInfoUrl, "?id=", hotInfoDto.getId() + "")));
            return hotInfoDto;

        }).collect(Collectors.toList());

        return ResultGenerator.genSuccessResult(dtoList);
    }


    
    @PostMapping("/article")
    public Result<HotInfoDto> article(
            @ApiParam(name = "articleType", value = "文章类型", required = true) @NotNull @RequestParam ArticleType articleType,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String terminalType = sysParameterWithoutToken.getTerminalType();
        String terminalId = sysParameterWithoutToken.getTerminalId();

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
        hotInfoDto.setHref(StrFunUtil.assembleUrl(terminalType, terminalId, String.join("", hostInfoUrl, "?id=", hotInfoDto.getId() + "")));



        return ResultGenerator.genSuccessResult(hotInfoDto);
    }

}
