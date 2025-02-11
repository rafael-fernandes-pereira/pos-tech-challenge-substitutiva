package com.github.rafaelfernandes.employee.adapter.out.persistence;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeMapper {

    public Employee toDomain(EmployeeJpaEntity employeeJpaEntity) {
        return Employee.of(
                employeeJpaEntity.getId().toString(),
                employeeJpaEntity.getName(),
                employeeJpaEntity.getDocument(),
                employeeJpaEntity.getCellphone()
        );

    }

    public EmployeeJpaEntity toJpaEntity(Employee employee) {
        return EmployeeJpaEntity.builder()
                .id(UUID.fromString(employee.getEmployeeId().id()))
                .name(employee.getName())
                .document(employee.getDocument())
                .cellphone(employee.getCellphone())
                .build();
    }

}
