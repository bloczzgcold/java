package com.github.hualuomoli.validator.constraintvalidation;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.hualuomoli.validator.constraints.Values;
import com.google.common.collect.Sets;

public class ValuesIntegerValidator implements ConstraintValidator<Values, Integer> {

	private Set<Integer> values;

	@Override
	public void initialize(Values constraintAnnotation) {
		String[] value = constraintAnnotation.value();
		values = Sets.newHashSet();
		for (String val : value) {
			values.add(Integer.parseInt(val));
		}
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		// 如果值为空,不验证
		if (value == null) {
			return true;
		}
		return values.contains(value);
	}

}
