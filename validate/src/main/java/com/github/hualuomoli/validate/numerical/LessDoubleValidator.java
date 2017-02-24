package com.github.hualuomoli.validate.numerical;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LessDoubleValidator implements ConstraintValidator<Greater, Double> {

	private int value;

	@Override
	public void initialize(Greater constraintAnnotation) {
		this.value = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {

		if (value == null) {
			return true;
		}

		return value < this.value;
	}

}
