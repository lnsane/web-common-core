package com.github.lnsane.web.common.valid.supprt.lmpl;

import cn.hutool.core.lang.PatternPool;
import com.github.lnsane.web.common.valid.supprt.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

/**
 * @author lnsane
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public void initialize(IdCard constraintAnnotation) {
        String message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = PatternPool.CITIZEN_ID.matcher(value);
        return matcher.matches();
    }
}
