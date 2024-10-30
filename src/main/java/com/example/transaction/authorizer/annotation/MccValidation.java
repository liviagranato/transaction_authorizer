package com.example.transaction.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.transaction.authorizer.util.ValidateMcc;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidateMcc.class)
public @interface MccValidation {

	String message() default "MCC must have 4 digits";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};
}
