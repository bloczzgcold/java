package com.github.hualuomoli.validator.constraintvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.hualuomoli.validator.constraints.Values;

public class ValuesIntegerValidator implements ConstraintValidator<Values, Integer> {

	@Override
	public void initialize(Values constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return false;
	}

}
