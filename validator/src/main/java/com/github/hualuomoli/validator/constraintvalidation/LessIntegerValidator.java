package com.github.hualuomoli.validator.constraintvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.hualuomoli.validator.constraints.Greater;

public class LessIntegerValidator implements ConstraintValidator<Greater, Integer> {

	private int value;

	@Override
	public void initialize(Greater constraintAnnotation) {
		this.value = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {

		if (value == null) {
			return true;
		}

		return value < this.value;
	}

}
