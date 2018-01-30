package com.zfhy.egold.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LAI on 2017/11/12.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = MobileValidator.class)
public @interface MobileValidate {
    String message() default "您好，您输入的手机号格式不正确，请重新输入";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};



}
