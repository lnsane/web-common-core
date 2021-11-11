package com.github.lnsane.web.common.valid.supprt.lmpl;

import cn.hutool.core.util.StrUtil;
import com.github.lnsane.web.common.valid.supprt.EnumValid;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lnsane
 */
public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    List list = new ArrayList();

    @SneakyThrows
    @Override
    public void initialize(EnumValid constraintAnnotation) {
        Class<?>[] start = constraintAnnotation.start();
        for (Class<?> aClass : start) {
            Field[] fields = aClass.getFields();
            Field key =  null;
            if (fields.length > 0) {
                key = fields[0];
            }
            Method method = aClass.getMethod("get" + StrUtil.upperFirst(key.getName()));
            Object[] enumConstants = aClass.getEnumConstants();
            for (Object enumConstant : enumConstants) {
                list.add(method.invoke(enumConstant));
            }
        }

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isNotBlank(value)) {
            return list.contains(value);
        }
        return false;
    }
}
