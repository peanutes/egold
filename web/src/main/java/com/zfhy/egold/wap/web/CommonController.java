package com.zfhy.egold.wap.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/common")
@Api(value = "通用控制器",tags = "通用控制器")
public class CommonController {
    @GetMapping("/goto")
    public String gotoPage(@ApiParam(name = "goto", value = "要跳转到的页面,目录间用英文的'__'隔开", required = true) @NotBlank @RequestParam String page) {

        return "/" + page.replaceAll("__", "/");
    }
}
