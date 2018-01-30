package com.zfhy.egold.wap.web.front;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@Slf4j
@Validated
public class ProductController {
    private String PREFIX = "product";


    
    @GetMapping("/introduction")
    public String detail(Model model) {

        return PREFIX + "/product_introduction";
    }

    
    @GetMapping("/security")
    public String security(Model model) {

        return PREFIX + "/product_security";
    }

    
    @GetMapping("/insurance")
    public String insurance(Model model) {

        return PREFIX + "/product_insurance";
    }
}
