package com.github.rafaelfernandes.delivery.common.validation;

import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfNotificationStatusValidator implements ConstraintValidator<ValueOfEnumNotificationStatus, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnumNotificationStatus annotation) {
        acceptedValues = Arrays.stream(NotificationStatus.values())
                .map(NotificationStatus::name)
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
