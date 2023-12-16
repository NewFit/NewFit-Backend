package com.newfit.reservation.common.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
	Class<? extends Enum<?>> value();

	String message() default "enum validation error";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
