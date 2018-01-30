package com.zfhy.egold.wap.web.front;

import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.domain.member.service.AddressService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/myAddress")
@Slf4j
@Validated
public class MyAddressController {

    @Autowired
    private AddressService addressService;

    private String PREFIX = "address";


    
    @GetMapping(value = {"/list"})
    public String index(Model model) {
        MemberSession memberSession = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");

        if (null == memberSession) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {

            List<AddressOutputDto> addressOutputDtoList = addressService.list(memberSession.getId());
            model.addAttribute("addressOutputDtoList", addressOutputDtoList);
            return PREFIX + "/list_address";
        }
    }

    
    @GetMapping(value = {"/addOrEdit"})
    public String add(Integer addressId, Model model) {
        MemberSession memberSession = (MemberSession) RequestUtil.getHttpServletSession().getAttribute("auth_member_session");
        Address address = new Address();
        if (null == memberSession) {
            model.addAttribute("error", "您还没有登录，请先登录或者注册!");
            return "user/register_or_login";
        } else {
            if (null != addressId) {
                address = this.addressService.findAddressById(addressId);

            }
            model.addAttribute("address", address);
            model.addAttribute("memberSession", memberSession);
            return PREFIX + "/add_or_edit_address";
        }
    }


}
