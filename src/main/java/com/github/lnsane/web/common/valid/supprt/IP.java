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
public @interface IP {
    String message() default "IP地址格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IP[] value();
    }
}
