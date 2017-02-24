package com.github.hualuomoli.validate.values;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 特定几个值验证器
 * @author lbq
 *
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { ValuesStringValidator.class, ValuesIntegerValidator.class })
public @interface Values {

	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	// 可选数据
	String[] value();

}
