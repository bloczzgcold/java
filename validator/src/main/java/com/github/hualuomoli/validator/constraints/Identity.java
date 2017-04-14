package com.github.hualuomoli.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 身份证
 * @author lbq
 *
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Identity {

	String message() default "{com.github.hualuomoli.validator.constraints.Identity.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
