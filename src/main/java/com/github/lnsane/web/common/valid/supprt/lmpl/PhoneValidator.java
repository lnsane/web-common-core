package com.github.lnsane.web.common.valid.supprt.lmpl;

import cn.hutool.core.lang.PatternPool;
import com.github.lnsane.web.common.valid.supprt.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

/**
 * @author lnsane
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
        String message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = PatternPool.MOBILE.matcher(value);
        return matcher.matches();
    }
}
