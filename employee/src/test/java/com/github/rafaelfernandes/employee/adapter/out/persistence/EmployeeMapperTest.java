package com.github.rafaelfernandes.employee.adapter.out.persistence;

import com.github.rafaelfernandes.employee.adapter.out.persistence.entity.EmployeeJpaEntity;
import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    public void testToDomain() {
        EmployeeJpaEntity employeeJpaEntity = EmployeeJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .document("795.093.210-44")
                .cellphone("+55 11 98765-4321")
                .build();

        Employee employee = employeeMapper.toDomain(employeeJpaEntity);

        assertEquals(employeeJpaEntity.getId().toString(), employee.getEmployeeId().id());
        assertEquals(employeeJpaEntity.getName(), employee.getName());
        assertEquals(employeeJpaEntity.getDocument(), employee.getDocument());
        assertEquals(employeeJpaEntity.getCellphone(), employee.getCellphone());
    }

    @Test
    public void testToJpaEntity() {
        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        EmployeeJpaEntity employeeJpaEntity = employeeMapper.toJpaEntity(employee);

        assertEquals(employee.getEmployeeId().id(), employeeJpaEntity.getId().toString());
        assertEquals(employee.getName(), employeeJpaEntity.getName());
        assertEquals(employee.getDocument(), employeeJpaEntity.getDocument());
        assertEquals(employee.getCellphone(), employeeJpaEntity.getCellphone());
    }
}