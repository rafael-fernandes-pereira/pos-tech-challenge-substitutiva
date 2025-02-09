package com.github.rafaelfernandes.delivery.common.validation;

import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfDeliveryStatusValidator implements ConstraintValidator<ValueOfEnumDeliveryStatus, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnumDeliveryStatus annotation) {
        acceptedValues = Arrays.stream(DeliveryStatus.values())
                .map(DeliveryStatus::name)
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
