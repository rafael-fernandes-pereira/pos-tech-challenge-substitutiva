package com.github.rafaelfernandes.user.common.validation;

import com.github.rafaelfernandes.user.common.enums.UserType;
import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfUserTypeValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Arrays.stream(UserType.values())
                .map(UserType::name)
                .map(String::toLowerCase)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString().toLowerCase());
    }

}
