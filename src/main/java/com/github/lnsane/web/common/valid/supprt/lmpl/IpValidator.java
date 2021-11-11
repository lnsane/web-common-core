package com.github.lnsane.web.common.valid.supprt.lmpl;

import cn.hutool.core.lang.PatternPool;
import com.github.lnsane.web.common.valid.supprt.IP;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

/**
 * @author lnsane
 */
public class IpValidator implements ConstraintValidator<IP, String> {

    @Override
    public void initialize(IP constraintAnnotation) {
        String message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = PatternPool.IPV4.matcher(value);
        return matcher.matches();
    }
}
