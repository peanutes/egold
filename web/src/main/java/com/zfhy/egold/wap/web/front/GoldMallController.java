package com.zfhy.egold.wap.web.front;

import com.google.common.collect.Lists;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.goods.dto.SkuDetailDto;
import com.zfhy.egold.domain.goods.dto.SpuDto;
import com.zfhy.egold.domain.goods.dto.WithdrawGoldFeeDto;
import com.zfhy.egold.domain.goods.entity.Spu;
import com.zfhy.egold.domain.goods.service.SkuService;
import com.zfhy.egold.domain.goods.service.SpuService;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.dto.MemberAccountDto;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.service.AddressService;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.service.DictService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/goldMall")
@Slf4j
@Validated
public class GoldMallController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuService spuService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DictService dictService;

    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private MemberService memberService;

    private String PREFIX = "mall";


    
    @GetMapping(value = {"/index"})
    public String index(Model model) {

        return PREFIX + "/mall_index";
    }

    
    @GetMapping(value = {"/index4App", "/skuList"})
    public String index4App(@RequestParam String token, Model model) {

        
        String token4App = null;

        if (StringUtils.isNotBlank(token)) {
            MemberSession member = this.redisService.checkAndGetMemberToken4Mall(token);
            if (null != member) {
                token4App = token;
                RequestUtil.getHttpServletSession().setAttribute("token", token4App);
            }
        } else {
            RequestUtil.getHttpServletSession().invalidate();
        }


        return PREFIX + "/mall_index_4_app";
    }


    
    @GetMapping("/skuDetail")
    public String skuDetail(@RequestParam Integer skuId, Model model) {

        SkuDetailDto skuDetailDto = this.skuService.getSkuDetail(skuId);
        model.addAttribute("skuDetailDto", skuDetailDto);
        return PREFIX + "/sku_detail";
    }

    
    @GetMapping("/skuDetail4App")
    public String skuDetail4App(@RequestParam Integer skuId, Model model) {

        SkuDetailDto skuDetailDto = this.skuService.getSkuDetail(skuId);
        model.addAttribute("skuDetailDto", skuDetailDto);
        return PREFIX + "/sku_detail_4_app";
    }

    
    @GetMapping("/toGoldOrder")
    public String toGoldOrder(
                              @RequestParam Integer skuId,
                              @RequestParam(required = false) Integer addressId,
                              Model model) {

        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");

        if (null == session) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {

            SkuDetailDto skuDetailDto = null;
            Spu spu = null;

            
            String bankCardBindMobileMask = "";

            
            Double enabledBalance = Double.valueOf(0);
            
            Double balancePayAmount = Double.valueOf(0);
            
            Double price = Double.valueOf(0);
            
            Double otherTotalMoney = Double.valueOf(0);
            
            Double totalAmount = Double.valueOf(0);
            
            Double onlinePayAmount = Double.valueOf(0);

            
            boolean hasAddress = false;
            
            AddressOutputDto addressOutputDto = new AddressOutputDto();

            
            boolean hasBindCard = false;
            
            Bankcard bankCard = null;
            
            String bankCardLastNumber = "";
            
            String bankCardName = "";

            
            bankCard = this.bankcardService.findBankCardObjectByMemberId(session.getId());
            if (null != bankCard) {
                String bankCardNumber = bankCard.getBankCard();
                String bindMobile = bankCard.getBindMobile();

                bankCardLastNumber = bankCardNumber.substring(bankCardNumber.length() - 4);
                bankCardName = bankCard.getBankName();
                bankCardBindMobileMask = bindMobile.substring(0, 3) + "****" + bindMobile.substring(7, 11);
                hasBindCard = true;
            }


            
            List<AddressOutputDto> addressOutputDtos = Lists.newArrayList();

            
            if (null != addressId) {
                Address selectAddress = this.addressService.findAddressById(addressId);
                addressOutputDto = addressOutputDto.convertFrom(selectAddress);

                hasAddress = true;
            } else {
                Address defaultAddress = this.addressService.findDefaultAddress(session.getId());
                addressOutputDtos = addressService.list(session.getId());

                
                if (defaultAddress != null) {
                    addressOutputDto.convertFrom(defaultAddress);
                    addressId = addressOutputDto.getId();
                    hasAddress = true;
                } else {
                    if (null != addressOutputDtos && !addressOutputDtos.isEmpty()) {
                        addressOutputDto = addressOutputDtos.get(0);
                        addressId = addressOutputDto.getId();
                        hasAddress = true;
                    }
                }

            }

            
            WithdrawGoldFeeDto withdrawGoldFeeDto = WithdrawGoldFeeDto.builder()
                    .insuranceFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE))

                    .expressFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE))
                    .addressDtoList(addressOutputDtos)
                    .build();

            
            otherTotalMoney = Double.sum(withdrawGoldFeeDto.getExpressFee(), withdrawGoldFeeDto.getInsuranceFee());

            
            skuDetailDto = this.skuService.getSkuDetail(skuId);

            if (null != skuDetailDto) {
                price = skuDetailDto.getPrice();
                spu = this.spuService.findById(skuDetailDto.getGoodsDetailDto().getSpuId());
            }


            
            totalAmount = price + otherTotalMoney;

            MemberAccountDto memberAccountDto = this.memberService.getAccountOverview(session.getId());

            if (null != memberAccountDto) {
                
                enabledBalance = memberAccountDto.getEnableBalance();

            }

            if (enabledBalance.compareTo(totalAmount) > 0) {
                balancePayAmount = totalAmount;
            } else {
                balancePayAmount = enabledBalance;
            }

            onlinePayAmount = Double.sum(totalAmount, -balancePayAmount);

            model.addAttribute("skuId", skuId);
            model.addAttribute("skuDetailDto", skuDetailDto);
            model.addAttribute("spu", spu);
            model.addAttribute("hasAddress", hasAddress);
            model.addAttribute("addressOutputDto", addressOutputDto);
            model.addAttribute("withdrawGoldFeeDto", withdrawGoldFeeDto);
            model.addAttribute("memberAccountDto", memberAccountDto);

            model.addAttribute("hasBindCard", hasBindCard);
            model.addAttribute("bankCardName", bankCardName);

            model.addAttribute("bankCardLastNumber", bankCardLastNumber);

            model.addAttribute("bankCardBindMobile", bankCardBindMobileMask);
            model.addAttribute("addressId", addressId);


            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("enabledBalance", enabledBalance);
            model.addAttribute("otherTotalMoney", otherTotalMoney);
            model.addAttribute("price", price);
            model.addAttribute("onlinePayAmount", onlinePayAmount);
            model.addAttribute("balancePayAmount", balancePayAmount);
        }

        model.addAttribute("memberSession", session);

        return PREFIX + "/sku_order";
    }

    
    @GetMapping("/toGoldOrder4App")
    public String toGoldOrder4App(@RequestParam(required = false) String token,

                                  @RequestParam Integer skuId,
                                  @RequestParam(required = false) Integer addressId,
                                  Model model) {

        if (StringUtils.isBlank(token)) {
            token = (String) RequestUtil.getHttpServletSession().getAttribute("token");
        }

        MemberSession session = null;

        
        if (StringUtils.isNotBlank(token)) {

            MemberSession member = this.redisService.checkAndGetMemberToken4Mall(token);


            session = this.redisService.checkAndGetMemberToken4Mall(token);


            if (session != null) {

                SkuDetailDto skuDetailDto = null;
                Spu spu = null;

                
                String bankCardBindMobileMask = "";

                
                Double enabledBalance = Double.valueOf(0);
                
                Double balancePayAmount = Double.valueOf(0);
                
                Double price = Double.valueOf(0);
                
                Double otherTotalMoney = Double.valueOf(0);
                
                Double totalAmount = Double.valueOf(0);
                
                Double onlinePayAmount = Double.valueOf(0);

                
                boolean hasAddress = false;
                
                AddressOutputDto addressOutputDto = new AddressOutputDto();

                
                boolean hasBindCard = false;
                
                Bankcard bankCard = null;
                
                String bankCardLastNumber = "";
                
                String bankCardName = "";

                
                bankCard = this.bankcardService.findBankCardObjectByMemberId(session.getId());
                if (null != bankCard) {
                    String bankCardNumber = bankCard.getBankCard();
                    String bindMobile = bankCard.getBindMobile();

                    bankCardLastNumber = bankCardNumber.substring(bankCardNumber.length() - 4);
                    bankCardName = bankCard.getBankName();
                    bankCardBindMobileMask = bindMobile.substring(0, 3) + "****" + bindMobile.substring(7, 11);
                    hasBindCard = true;
                }


                
                List<AddressOutputDto> addressOutputDtos = Lists.newArrayList();

                
                if (null != addressId) {
                    Address selectAddress = this.addressService.findAddressById(addressId);
                    addressOutputDto = addressOutputDto.convertFrom(selectAddress);

                    hasAddress = true;
                } else {
                    Address defaultAddress = this.addressService.findDefaultAddress(session.getId());
                    addressOutputDtos = addressService.list(session.getId());

                    
                    if (defaultAddress != null) {
                        addressOutputDto.convertFrom(defaultAddress);
                        addressId = addressOutputDto.getId();
                        hasAddress = true;
                    } else {
                        if (null != addressOutputDtos && !addressOutputDtos.isEmpty()) {
                            addressOutputDto = addressOutputDtos.get(0);
                            addressId = addressOutputDto.getId();
                            hasAddress = true;
                        }
                    }

                }

                
                WithdrawGoldFeeDto withdrawGoldFeeDto = WithdrawGoldFeeDto.builder()
                        .insuranceFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_INSURANCE_FEE))

                        .expressFee(this.dictService.findDouble(DictType.WITHDRAW_GOLD_EXPRESS_FEE))
                        .addressDtoList(addressOutputDtos)
                        .build();

                
                otherTotalMoney = Double.sum(withdrawGoldFeeDto.getExpressFee(), withdrawGoldFeeDto.getInsuranceFee());

                
                skuDetailDto = this.skuService.getSkuDetail(skuId);

                if (null != skuDetailDto) {
                    price = skuDetailDto.getPrice();
                    spu = this.spuService.findById(skuDetailDto.getGoodsDetailDto().getSpuId());
                }


                
                totalAmount = price + otherTotalMoney;

                MemberAccountDto memberAccountDto = this.memberService.getAccountOverview(session.getId());

                if (null != memberAccountDto) {
                    
                    enabledBalance = memberAccountDto.getEnableBalance();

                }

                if (enabledBalance.compareTo(totalAmount) > 0) {
                    balancePayAmount = totalAmount;
                } else {
                    balancePayAmount = enabledBalance;
                }

                onlinePayAmount = Double.sum(totalAmount, -balancePayAmount);

                RequestUtil.getHttpServletSession().setAttribute("token", token);

                model.addAttribute("skuId", skuId);
                model.addAttribute("skuDetailDto", skuDetailDto);
                model.addAttribute("spu", spu);
                model.addAttribute("hasAddress", hasAddress);
                model.addAttribute("addressOutputDto", addressOutputDto);
                model.addAttribute("withdrawGoldFeeDto", withdrawGoldFeeDto);
                model.addAttribute("memberAccountDto", memberAccountDto);

                model.addAttribute("hasBindCard", hasBindCard);
                model.addAttribute("bankCardName", bankCardName);

                model.addAttribute("bankCardLastNumber", bankCardLastNumber);

                model.addAttribute("bankCardBindMobile", bankCardBindMobileMask);
                model.addAttribute("addressId", addressId);


                model.addAttribute("totalAmount", totalAmount);
                model.addAttribute("enabledBalance", enabledBalance);
                model.addAttribute("otherTotalMoney", otherTotalMoney);
                model.addAttribute("price", price);
                model.addAttribute("onlinePayAmount", onlinePayAmount);
                model.addAttribute("balancePayAmount", balancePayAmount);
            }
        }

        model.addAttribute("memberSession", session);

        return PREFIX + "/sku_order_4_app";
    }


}
