package com.github.hualuomoli.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdentityValidator implements ConstraintValidator<Identity, String> {

	@Override
	public void initialize(Identity constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		return true;
	}

}
