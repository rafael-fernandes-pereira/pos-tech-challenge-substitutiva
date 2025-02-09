package com.github.rafaelfernandes.delivery.application.domain.model;

import com.github.rafaelfernandes.delivery.common.validation.ValidationContactNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import static com.github.rafaelfernandes.delivery.common.validation.Validation.validate;


public record Employee(
        EmployeeId employeeId,
        String name,
        String document,
        String cellphone
) {
    public record EmployeeId(
        String id
    ) {
    }
}