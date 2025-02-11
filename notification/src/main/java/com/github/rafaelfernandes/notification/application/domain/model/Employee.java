package com.github.rafaelfernandes.notification.application.domain.model;

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