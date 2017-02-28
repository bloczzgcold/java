package com.github.hualuomoli.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.github.hualuomoli.validate.values.ValuesIntegerValidator;

/**
 * 身份证
 * @author lbq
 *
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IdentityValidator.class, ValuesIntegerValidator.class })
public @interface Identity {

	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
