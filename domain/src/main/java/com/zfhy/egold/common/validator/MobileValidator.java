package com.zfhy.egold.common.validator;

import com.zfhy.egold.common.util.ValidateUtil;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by LAI on 2017/11/12.
 */
public class MobileValidator implements ConstraintValidator<MobileValidate, Object> {

    @Override
    public void initialize(MobileValidate constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || StringUtils.isBlank(value.toString())) {
            return true;
        }

        return ValidateUtil.isMobile(value.toString());
    }
}
