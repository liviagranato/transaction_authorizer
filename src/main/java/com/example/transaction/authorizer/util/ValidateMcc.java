package com.example.transaction.authorizer.util;

import com.example.transaction.authorizer.annotation.MccValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateMcc implements ConstraintValidator<MccValidation, String> {

	@Override
	public void initialize(MccValidation constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && value.matches("\\d{4}");
	}
}
