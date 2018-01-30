package com.zfhy.egold.api.web.conf;
 
import com.zfhy.egold.common.core.dto.AppType;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.StrFunUtil;
import com.zfhy.egold.domain.sys.dto.*;
import com.zfhy.egold.domain.sys.entity.Activity;
import com.zfhy.egold.domain.sys.entity.AppVersion;
import com.zfhy.egold.domain.sys.service.ActivityService;
import com.zfhy.egold.domain.sys.service.AppVersionService;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/conf/conf")
@Api(value = "SysConfigController", tags = "SysConfigController", description = "系统配置", position = 99)
@Slf4j
public class
SysConfigController {



    @Autowired
    private DictService dictService;

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private ActivityService activityService;

    
    @PostMapping("/appStoreSwitch")
    public Result<String> appStoreSwitch(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        String appStoreSwitch = this.dictService.findStringByType(DictType.APP_STORE_SWITCH);
        return ResultGenerator.genSuccessResult(appStoreSwitch);

    }


    
    @PostMapping("/appVersion")
    public Result<AppVersionDto> appVersion(@ApiParam(name = "appType", value = "终端类型", required = true) @NotNull @RequestParam AppType appType,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        AppVersion appVersion = this.appVersionService.findLatelyVersion(appType);

        return ResultGenerator.genSuccessResult(new AppVersionDto().convertFrom(appVersion));

    }


    
    @PostMapping("/appConfig")
    public Result<AppConfigDto> appConfig(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {
        String terminalType = sysParameterWithoutToken.getTerminalType();
        String terminalId = sysParameterWithoutToken.getTerminalId();

        Map<String, String> dictMap = this.dictService.findDictMap();
        AppConfigDto appConfigDto = new AppConfigDto();
        appConfigDto.setAboutUsUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.ABOUT_US_URL.name())));
        appConfigDto.setAccountAgreementUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.ACCOUNT_AGREEMENT.name())));
        appConfigDto.setAgreementRegisterUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_REGISTER.name())));
        appConfigDto.setAgreementCurrentGoldUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_CURRENT_GOLD.name())));
        appConfigDto.setAgreementFixedGoldUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_FIXED_GOLD.name())));
        appConfigDto.setAgreementRiseNFallGoldUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_RISE_N_FALL_GOLD.name())));
        appConfigDto.setAgreementRealNameUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_REAL_NAME.name())));
        appConfigDto.setAgreementNewUserUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.AGREEMENT_NEW_USER.name())));
        appConfigDto.setAppStoreSwitch(dictMap.get(DictType.APP_STORE_SWITCH.name()));
        appConfigDto.setGoldKLineChartUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.GOLD_KLINE_CHART_URL.name())));
        appConfigDto.setGoldKLineChartOnlyUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.GOLD_KLINE_CHART_ONLY_URL.name())));
        appConfigDto.setHelpDeskUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.HELP_DESK_URL.name())));
        appConfigDto.setInviteUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.INVITE_URL.name())));
        appConfigDto.setInviteRedPackageUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.INVITE_RED_PACKAGE_URL.name())));
        appConfigDto.setMyBankSecureTip(dictMap.get(DictType.MY_BANK_SECURE_TIP.name()));
        appConfigDto.setProductDescUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.PRODUCT_DESC.name())));
        appConfigDto.setSafetyGuaranteeUrl(StrFunUtil.assembleUrl(terminalType, terminalId, dictMap.get(DictType.SAFETY_GUARANTEE_URL.name())));
        appConfigDto.setOrderExpiredTime(dictMap.get(DictType.ORDER_EXPIRED_TIME.name()));
        appConfigDto.setRefreshPriceInterval(dictMap.get(DictType.REFRESH_PRICE_INTERVAL.name()));
        appConfigDto.setMallUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.MALL_URL.name())));
        appConfigDto.setMallOrderUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.MALL_ORDER_URL.name())));
        appConfigDto.setAndroidAppDownloadUrl(dictMap.get(DictType.ANDROID_APP_DOWNLOAD_URL.name()));
        appConfigDto.setIphoneAppDownloadUrl(dictMap.get(DictType.IPHONE_APP_DOWNLOAD_URL.name()));
        appConfigDto.setGoldRuleUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.GOLD_RULE_URL.name())));

        appConfigDto.setShareTitle(dictMap.get(DictType.SHARE_TITLE.name()));
        appConfigDto.setShareContent(dictMap.get(DictType.SHARE_CONTENT.name()));
        appConfigDto.setShareUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.SHARE_URL.name())));

        appConfigDto.setSysEmail(dictMap.get(DictType.SYS_EMAIL.name()));
        appConfigDto.setSysMobile(dictMap.get(DictType.SYS_MOBILE.name()));
        appConfigDto.setSysQQ(dictMap.get(DictType.SYS_QQ.name()));
        appConfigDto.setSysWebSite(dictMap.get(DictType.SYS_WEB_SITE.name()));
        appConfigDto.setSysWebSiteUrl(dictMap.get(DictType.SYS_WEB_SITE_URL.name()));
        appConfigDto.setSysWechat(dictMap.get(DictType.SYS_WECHAT.name()));
        appConfigDto.setContractUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.CONTRACT_URL.name())));

        appConfigDto.setInvestAgreementUrl(StrFunUtil.assembleUrl(terminalType, terminalId,dictMap.get(DictType.INVEST_AGREEMENT_URL.name())));

        Activity activity = this.activityService.findByType(ActivityType.APP_HOME_POPUP);

        if (Objects.nonNull(activity)) {
            appConfigDto.setActivityDto(new ActivityDto().convertFrom(activity, terminalType, terminalId));

        }

        return ResultGenerator.genSuccessResult(appConfigDto);
    }








}
