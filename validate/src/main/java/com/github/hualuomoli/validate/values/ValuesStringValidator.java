package com.github.hualuomoli.validate.values;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.collect.Sets;

public class ValuesStringValidator implements ConstraintValidator<Values, String> {

	private Set<String> values;

	@Override
	public void initialize(Values constraintAnnotation) {
		values = Sets.newHashSet(constraintAnnotation.value());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// 如果值为空,不验证
		if (value == null) {
			return true;
		}
		return values.contains(value);
	}

}
