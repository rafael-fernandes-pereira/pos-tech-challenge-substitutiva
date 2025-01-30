package com.github.rafaelfernandes.employee.adapter.out.persistence;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;

import java.util.UUID;

public class EmployeeMapper {

    public static Employee toDomain(EmployeeJpaEntity employeeJpaEntity) {
        return Employee.of(
                employeeJpaEntity.getId().toString(),
                employeeJpaEntity.getName(),
                employeeJpaEntity.getDocument(),
                employeeJpaEntity.getCellphone()
        );

    }

    public static EmployeeJpaEntity toJpaEntity(Employee employee) {
        return EmployeeJpaEntity.builder()
                .id(UUID.fromString(employee.getEmployeeId().id()))
                .name(employee.getName())
                .document(employee.getDocument())
                .cellphone(employee.getCellphone())
                .build();
    }

}
