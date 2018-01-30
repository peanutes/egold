package com.zfhy.egold.wap.web.invest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.dto.*;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.entity.SoldDetail;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.invest.service.SoldDetailService;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/invest/financial/product")
@Api(value = "FinancialProductController",tags = "FinancialProductController", description = "理财产品")
@Slf4j
@Validated
public class FinancialProductController {
    @Resource
    private FinancialProductService financialProductService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SoldDetailService soldDetailService;

    @Autowired
    private DictService dictService;


    
    @PostMapping("/homeProducts")
    public Result<HomeFinancialProductDto> homeProducts(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {
        HomeFinancialProductDto homeFinancialProductDto = this.financialProductService.homeProducts();

        return ResultGenerator.genSuccessResult(homeFinancialProductDto);
    }


    
    @PostMapping("/buyGoldProducts")
    public Result<BuyGoldDto> buyGoldProducts(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {
        BuyGoldDto buyGoldDto = this.financialProductService.productList();

        return ResultGenerator.genSuccessResult(buyGoldDto);
    }


    
    @PostMapping("/buyHistory")
    public Result<Page<BuyHistoryDto>> buyHistory(
            @ApiParam(name = "id", value = "理财产品id", required = true)
                                                  @NotNull @RequestParam Integer id,
                                                  @ApiParam(name = "page", value = "页数", required = true)
                                                  @RequestParam @NotNull(message = "页数不允许为空") Integer page,
                                                  @ApiParam(name = "size", value = "每页条数", required = true)
                                                  @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
                                                  @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        PageHelper.startPage(page, size);
        List<InvestRecord> investRecords = this.investRecordService.buyHistory(id);

        PageInfo<InvestRecord> investRecordPageInfo = new PageInfo<>(investRecords);

        Page<BuyHistoryDto> buyHistoryDtoPage = new Page<>();
        BeanUtils.copyProperties(investRecordPageInfo, buyHistoryDtoPage);
        List<BuyHistoryDto> dtoList = investRecords.stream().map(ele -> new BuyHistoryDto().convertFrom(ele)).collect(Collectors.toList());
        buyHistoryDtoPage.setList(dtoList);

        return ResultGenerator.genSuccessResult(buyHistoryDtoPage);
    }


    
    @PostMapping("/productDetail")
    public Result<FinancialProductDto> productDetail(@ApiParam(name = "id", value = "理财产品id", required = true)
                                                     @NotNull @RequestParam Integer id,
                                                     @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        FinancialProduct product = this.financialProductService.findById(id);


        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        Double realTimePrice = this.redisService.getRealTimePrice();

        return ResultGenerator.genSuccessResult(new FinancialProductDto().convertFrom(product, currentYearPriceRisePercent, realTimePrice));
    }



    
    @PostMapping("/confirmSaleGold")
    public Result<SaleGoldResultDto> confirmSaleGold(
            @ApiParam(name = "goldWeight", value = "卖出重量", required = true)
            @NotNull @RequestParam Double goldWeight,
            @ApiParam(name = "estimateFall", value = "是否买跌", required = false)
            @RequestParam(required = false) Boolean estimateFall,
            @ApiParam(name = "dealPwd", value = "交易密码(长度为[6,16])", required = true)
            @RequestParam @NotBlank(message = "交易密码不能为空") @Length(min = 6, max = 16, message = "交易密码不能小于6位数") String dealPwd,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());
        SoldDetail soldDetail = this.soldDetailService.confirmSaleGold(member.getId(), goldWeight, dealPwd, estimateFall);


        SaleGoldResultDto dto = SaleGoldResultDto.builder()
                .goldWeight(DoubleUtil.toScal4(soldDetail.getGoldWeight()) + "克")
                .price(DoubleUtil.toString(soldDetail.getPrice()) + "元/克")
                .charge(DoubleUtil.toString(soldDetail.getFee()) + "元")
                .build();

        return ResultGenerator.genSuccessResult(dto);
    }













}
