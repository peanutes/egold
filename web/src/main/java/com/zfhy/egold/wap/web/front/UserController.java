package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.common.util.ValidateUtil;
import com.zfhy.egold.domain.invest.dto.CouponStatus;
import com.zfhy.egold.domain.invest.dto.CouponType;
import com.zfhy.egold.domain.invest.dto.QueryCouponResultDto;
import com.zfhy.egold.domain.invest.entity.MemberCoupon;
import com.zfhy.egold.domain.member.dto.MemberOutPutDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Bankcard;
import com.zfhy.egold.domain.member.entity.Idcard;
import com.zfhy.egold.domain.member.service.IdcardService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@Slf4j
@Validated
public class UserController {
    @Autowired
    private IdcardService idcardService;

    private String PREFIX = "user";

    
    @GetMapping("/toRegisterLogin")
    public String toRegisterLogin(Model model) {

        return PREFIX + "/register_or_login";
    }

    
    @GetMapping("/toRegister")
    public String toRegister(String mobile, Model model) {
        if (StringUtils.isEmpty(mobile) || !ValidateUtil.isMobile(mobile)) {
            model.addAttribute("mobile", mobile);
            model.addAttribute("msg", "手机号码不正确，请重新输入正确的手机号码");
            return this.toRegisterLogin(model);
        }

        model.addAttribute("mobile", mobile);
        return PREFIX + "/register";
    }

    
    @GetMapping("/toLogin")
    public String toLogin(@NotBlank @RequestParam String mobile, Model model) {

        model.addAttribute("mobile", mobile);

        return PREFIX + "/login";
    }

    
    @GetMapping("/toRealName")
    public String toRealName(Model model) {
        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        String mobile = null;
        if (null == memberOutPutDto) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {
            boolean hasRealName = (null != memberOutPutDto.getRealNameValid() && 1 == memberOutPutDto.getRealNameValid().intValue());
            String realName = "";
            String realNameMask = "";
            String idCardNoMask = "";
            if (hasRealName) {
                realName = memberOutPutDto.getRealName();
                
                if (hasRealName) {
                    realNameMask = maskRealName(realName);
                }

                Idcard idcard = this.idcardService.findByMemberId(memberOutPutDto.getId());

                
                if (idcard != null) {
                    String idCardNo = idcard.getIdCard();
                    idCardNoMask = maskIdCard(idCardNo);

                }
            }

            mobile = memberOutPutDto.getMobilePhone();
            model.addAttribute("mobile", mobile);
            model.addAttribute("idCardNo", idCardNoMask);
            model.addAttribute("hasRealName", hasRealName);
            model.addAttribute("realName", realName);

            return PREFIX + "/real_name";
        }
    }

    
    @GetMapping("/toBankCardBinding")
    public String toBankCardBinding(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");

        String mobile = null;
        if (null == memberOutPutDto) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else if (null != memberOutPutDto.getBankcardBind() && memberOutPutDto.getBankcardBind().equals(1)) {
            
            Bankcard bankCard = memberOutPutDto.getBankCardObject();
            if (null != bankCard) {
                String bankCardNumber = bankCard.getBankCard();
                String bankCardLastFour = bankCardNumber.substring(bankCardNumber.length() - 4);
                model.addAttribute("bankCard", bankCard);
                model.addAttribute("bankCardLastFour", bankCardLastFour);
                return PREFIX + "/my_bankcard";
            } else {
                mobile = session.getMobilePhone();
                model.addAttribute("mobile", mobile);
                return PREFIX + "/bank_card_binding";
            }
        } else {
            boolean hasRealName = (null != memberOutPutDto.getRealNameValid() && 1 == memberOutPutDto.getRealNameValid().intValue());
            String realNameMask = null;
            String realName = memberOutPutDto.getRealName();
            if (hasRealName) {
                realNameMask = maskRealName(realName);
            }
            mobile = session.getMobilePhone();
            model.addAttribute("hasRealName", hasRealName);
            model.addAttribute("realName", realNameMask);
            model.addAttribute("mobile", mobile);
        }

        return PREFIX + "/bank_card_binding";

    }

    
    @GetMapping("/toMyAccount")
    public String toMyAccount(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        String mobile = null;
        if (null != session) {
            mobile = session.getMobilePhone();
            String mobileMask = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
            model.addAttribute("mobile", mobileMask);
        }

        return PREFIX + "/my_account";
    }

    
    @GetMapping("/invitation")
    public String invitation(Model model) {
        return PREFIX + "/invitation";
    }

    
    @GetMapping("/invitationGift")
    public String invitationGift(Model model) {
        return PREFIX + "/invitation_gift";
    }

    
    @GetMapping("/myInvitation")
    public String myInvitation(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        String mobile = null;
        if (null == session) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {
            mobile = session.getMobilePhone();
            model.addAttribute("mobile", mobile);

            return PREFIX + "/my_invitation";
        }
    }

    
    @GetMapping("/myOrderList")
    public String myOrderList(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        String mobile = null;
        if (null == session) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {
            mobile = session.getMobilePhone();
            model.addAttribute("mobile", mobile);

            return PREFIX + "/my_order_list";
        }
    }

    
    @GetMapping("/myCashCoupon")
    public String myCashCoupon(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        String mobile = null;
        if (null == session) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {

            return PREFIX + "/my_cash_coupon";
        }
    }

    
    @GetMapping("/myDiscountCoupon")
    public String myDiscountCoupon(Model model) {
        MemberSession session = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        String mobile = null;
        if (null == session) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {

            return PREFIX + "/my_discount_coupon";
        }
    }


    
    @GetMapping("/aboutUs")
    public String aboutUs(Model model) {

        return PREFIX + "/abount_us";
    }

    
    @GetMapping("/myInfo")
    public String myInfo(Model model) {
        MemberOutPutDto memberOutPutDto = (MemberOutPutDto) RequestUtil.getHttpServletSession().getAttribute("memberOutPutDto");
        String mobile = null;
        if (null != memberOutPutDto) {
            mobile = memberOutPutDto.getMobilePhone();
            String realName = memberOutPutDto.getRealName();
            boolean hasRealName = (null != memberOutPutDto.getRealNameValid() && 1 == memberOutPutDto.getRealNameValid().intValue());

            String mobileMask = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
            String realNameMask = null;
            if (hasRealName) {
                realNameMask = maskRealName(realName);
            }

            model.addAttribute("mobile", mobileMask);
            model.addAttribute("realName", realNameMask);
            model.addAttribute("hasRealName", hasRealName);
        }
        return PREFIX + "/my_info";
    }

    private static String maskRealName(String name) {
        String result = name;
        if (StringUtils.isNotEmpty(name)) {
            int length = name.length();
            if (length < 3) {
                result = name.substring(length - 1);
            } else {
                result = name.substring(0, 1) + "*" + name.substring(length - 1);
            }
        }

        return result;
    }

    private static String maskIdCard(String idCardNo) {
        String result = idCardNo;

        if (StringUtils.isNotEmpty(idCardNo)) {
            int length = idCardNo.length();
            result = idCardNo.substring(0, 1) + "****************" + idCardNo.substring(length - 1);
        }

        return result;
    }

}
