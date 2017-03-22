package com.github.hualuomoli.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.github.hualuomoli.validator.constraintvalidation.GreaterDoubleValidator;
import com.github.hualuomoli.validator.constraintvalidation.GreaterIntegerValidator;

/**
 * 大于
 * @author lbq
 *
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { GreaterIntegerValidator.class, GreaterDoubleValidator.class })
public @interface Greater {

	String message() default "invalid amount.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int value();

}
