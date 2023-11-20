package com.newfit.reservation.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Enum> {

    private EnumValue enumValue;

    @Override
    public void initialize(EnumValue enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        for (Enum enumConstant : enumValue.value().getEnumConstants()) {
            if (value.equals(enumConstant)) {
                return true;
            }
        }

        return false;
    }
}
