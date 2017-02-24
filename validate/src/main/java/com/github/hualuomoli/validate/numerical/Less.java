package com.github.hualuomoli.validate.numerical;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 小于
 * @author lbq
 *
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { GreaterIntegerValidator.class, GreaterDoubleValidator.class })
public @interface Less {

	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int value();

}
