package com.github.lnsane.web.common.valid.supprt;

import com.github.lnsane.web.common.valid.supprt.lmpl.IpValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lnsane
 */
@Documented
@Constraint(validatedBy = IpValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface EnumValid {
    String message() default "取值范围不正确";

    Class<?>[] start() default {};

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IdCard[] value();
    }
}
