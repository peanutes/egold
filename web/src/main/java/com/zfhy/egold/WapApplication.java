package com.zfhy.egold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
//@EnableAspectJAutoProxy
public class WapApplication {
    public static void main(String[] args) {
        SpringApplication.run(WapApplication.class, args);
    }

}
