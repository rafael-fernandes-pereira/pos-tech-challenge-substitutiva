package com.github.rafaelfernandes.user.common.validation;

import jakarta.validation.ConstraintValidator;

public class PhoneNumberValidator implements ConstraintValidator<ValidationContactNumber, String> {

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return value != null && value.matches("^\\+\\d{1,3} \\d{2} \\d{5}-\\d{4}$");
    }
}