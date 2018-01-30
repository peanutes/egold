package com.zfhy.egold.wap.web.mall;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.goods.dto.*;
import com.zfhy.egold.domain.goods.entity.GoodsInvoice;
import com.zfhy.egold.domain.goods.service.GoodsInvoiceService;
import com.zfhy.egold.domain.goods.service.GoodsOrderService;
import com.zfhy.egold.domain.goods.service.SkuService;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.service.AddressService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/mall")
@Api(value = "MallController", tags = "MallController", description = "商城")
@Slf4j
@Validated
public class MallController {


    @Autowired
    private SkuService skuService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DictService dictService;

    @Autowired
    private GoodsInvoiceService goodsInvoiceService;

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private AddressService addressService;







    
    @PostMapping("/skuList")

    public Result<Page<SkuDto>> skuList(
            @ApiParam(name = "skuQueryOrderBy", value = "排序,PRICE_ASC=价格升序，PRICE_DESC=价格降序， NEW_GOOD=新品", required = false)
            @RequestParam(required = false) SkuQueryOrderBy skuQueryOrderBy,
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {


        if (Objects.isNull(skuQueryOrderBy)) {
            skuQueryOrderBy = SkuQueryOrderBy.NEW_GOOD;
        }
        PageHelper.startPage(page, size);
        PageHelper.orderBy(skuQueryOrderBy.getOrderBy());

        List<SkuDto> skuDtos = this.skuService.skuList(skuQueryOrderBy);

        PageInfo<SkuDto> skuDtoPageInfo = new PageInfo<>(skuDtos);

        Page<SkuDto> skuDtoPage = new Page<>();
        BeanUtils.copyProperties(skuDtoPageInfo, skuDtoPage);

        return ResultGenerator.genSuccessResult(skuDtoPage);
    }

    
    @PostMapping("/skuDetail")
    public Result<SkuDetailDto> skuDetail(
            @ApiParam(name = "skuId", value = "skuId", required = true)
            @RequestParam(required = true) @NotNull(message = "skuId不允许为空") Integer skuId,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        SkuDetailDto skuDetailDto = this.skuService.getSkuDetail(skuId);

        return ResultGenerator.genSuccessResult(skuDetailDto);
    }

    
    @PostMapping("/submitOrder")
    public Result<GoodsOrderDto> submitOrder(
            @ApiParam(name = "skuId", value = "skuId", required = true) @NotNull(message = "skuId不能为空") @RequestParam Integer skuId,
            @ApiParam(name = "num", value = "数量", required = true) @NotNull(message = "购买数量不能为空") @RequestParam Integer num,
            @ApiParam(name = "balanceAmount", value = "余额支付（如果不用余额支付传0）", required = true) @NotNull(message = "余额支付不能为空") @RequestParam Double balanceAmount,
            @ApiParam(name = "addressId", value = "收货地址", required = true)
            @NotNull(message = "收货地址不允许为空") @RequestParam Integer addressId,
            @ApiParam(name = "dealPwd", value = "交易密码", required = true)
            @NotNull(message = "交易密码不允许为空") @RequestParam String dealPwd,
            @ApiParam(name = "invoiceId", value = "发票id，不开发标就不传该参数", required = false) @RequestParam(required = false) Integer invoiceId,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        GoodsOrderDto goodsOrderDto = this.goodsOrderService.submitMallOrder(memberSession.getId(), skuId, num, balanceAmount, addressId, dealPwd, sysParameter.getTerminalId(), invoiceId);


        return ResultGenerator.genSuccessResult(goodsOrderDto);

    }

    
    @PostMapping("/submitOrder4App")
    public Result<GoodsOrderDto> submitOrder4App(
            @ApiParam(name = "skuId", value = "skuId", required = true) @NotNull(message = "skuId不能为空") @RequestParam Integer skuId,
            @ApiParam(name = "num", value = "数量", required = true) @NotNull(message = "购买数量不能为空") @RequestParam Integer num,
            @ApiParam(name = "balanceAmount", value = "余额支付（如果不用余额支付传0）", required = true) @NotNull(message = "余额支付不能为空") @RequestParam Double balanceAmount,
            @ApiParam(name = "addressId", value = "收货地址", required = true)
            @NotNull(message = "收货地址不允许为空") @RequestParam Integer addressId,
            @ApiParam(name = "dealPwd", value = "交易密码", required = true)
            @NotNull(message = "交易密码不允许为空") @RequestParam String dealPwd,
            @ApiParam(name = "invoiceId", value = "发票id，不开发标就不传该参数", required = false) @RequestParam(required = false) Integer invoiceId,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken4Mall(sysParameter.getToken());


        GoodsOrderDto goodsOrderDto = this.goodsOrderService.submitMallOrder(memberSession.getId(), skuId, num, balanceAmount, addressId, dealPwd, sysParameter.getTerminalId(), invoiceId);


        return ResultGenerator.genSuccessResult(goodsOrderDto);

    }


    
    @PostMapping("/shoppingFee")
    public Result<WithdrawGoldFeeDto> shoppingFee(
            @ModelAttribute @Valid SysParameter sysParameter) {
        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        List<AddressOutputDto> addressOutputDtos = this.addressService.list(memberSession.getId());


        WithdrawGoldFeeDto withdrawGoldFeeDto = WithdrawGoldFeeDto.builder()
                .insuranceFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE))

                .expressFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE))
                .addressDtoList(addressOutputDtos)
                .build();


        return ResultGenerator.genSuccessResult(withdrawGoldFeeDto);
    }


    
    @PostMapping("/submitInvoice")
    public Result<Integer> submitInvoice(
            
            @ApiParam(name = "invoiceItem", value = "发票项目", required = true) @NotBlank(message = "发票项目不能为空") @RequestParam String invoiceItem,
            @ApiParam(name = "invoiceType", value = "发票类型", required = true) @NotNull(message = "发票类型不能为空") @RequestParam InvoiceType invoiceType,
            @ApiParam(name = "invoiceTitle", value = "发票抬头", required = true) @NotBlank(message = "发票抬头不能为空") @RequestParam String invoiceTitle,
            @ApiParam(name = "taxNo", value = "税号", required = false) @RequestParam(required = false) String taxNo,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        GoodsInvoice goodsInvoice = this.goodsInvoiceService.submitInvoice(memberSession.getId(), 0D, invoiceItem, invoiceType, invoiceTitle, taxNo);


        return ResultGenerator.genSuccessResult(goodsInvoice.getId());
    }

    
    @PostMapping("/submitInvoice4Mall")
    public Result<Integer> submitInvoice4Mall(
            
            @ApiParam(name = "invoiceItem", value = "发票项目", required = true) @NotBlank(message = "发票项目不能为空") @RequestParam String invoiceItem,
            @ApiParam(name = "invoiceType", value = "发票类型", required = true) @NotNull(message = "发票类型不能为空") @RequestParam InvoiceType invoiceType,
            @ApiParam(name = "invoiceTitle", value = "发票抬头", required = true) @NotBlank(message = "发票抬头不能为空") @RequestParam String invoiceTitle,
            @ApiParam(name = "taxNo", value = "税号", required = false) @RequestParam(required = false) String taxNo,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession memberSession = this.redisService.checkAndGetMemberToken4Mall(sysParameter.getToken());


        GoodsInvoice goodsInvoice = this.goodsInvoiceService.submitInvoice(memberSession.getId(), 0D, invoiceItem, invoiceType, invoiceTitle, taxNo);


        return ResultGenerator.genSuccessResult(goodsInvoice.getId());
    }


}
