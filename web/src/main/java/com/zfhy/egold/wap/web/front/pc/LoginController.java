package com.zfhy.egold.wap.web.front.pc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/pc/")
@Slf4j
@Validated
public class LoginController {
    private String prefix = "pc";
    @RequestMapping("/gotoLogin")
    public String gotoLogin(HttpSession session, Model model){
        return prefix + "/login";
    }

    @RequestMapping("/gotoRegister")
    public String gotoRegister(HttpSession session, Model model){
        return prefix + "/register";
    }



}
