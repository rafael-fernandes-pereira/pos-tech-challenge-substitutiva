package com.github.rafaelfernandes.employee.adapter.out.persistence;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeePersistenceAdapterTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeePersistenceAdapter employeePersistenceAdapter;

    private Employee employeeToSave;
    private Employee employeeSaved;

    private EmployeeJpaEntity entity;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeToSave = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );

        employeeSaved = Employee.of(
                UUID.randomUUID().toString(),
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"
        );

        entity = new EmployeeJpaEntity();
    }

    @Test
    void save() {


        when(employeeRepository.save(any(EmployeeJpaEntity.class))).thenReturn(entity);

        when(employeeMapper.toJpaEntity(employeeToSave)).thenReturn(entity);

        when(employeeMapper.toDomain(entity)).thenReturn(employeeToSave);

        Employee result = employeePersistenceAdapter.save(employeeToSave);

        assertNotNull(result);
        verify(employeeRepository, times(1)).save(entity);
    }

    @Test
    void getAll() {

        Employee employee = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );

        Page<EmployeeJpaEntity> entities = new PageImpl<>(List.of(new EmployeeJpaEntity()));
        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(entities);
        when(employeeMapper.toDomain(any(EmployeeJpaEntity.class))).thenReturn(employee);

        Page<Employee> result = employeePersistenceAdapter.getAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findById() {

        Employee employee = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );

        UUID id = UUID.randomUUID();
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(employeeMapper.toDomain(entity)).thenReturn(employee);

        Optional<Employee> result = employeePersistenceAdapter.findById(id.toString());

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        Employee employee = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        when(employeeMapper.toJpaEntity(employee)).thenReturn(entity);

        employeePersistenceAdapter.delete(employee);

        verify(employeeRepository, times(1)).delete(entity);
    }

    @Test
    void update() {
        Employee employee = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        when(employeeMapper.toJpaEntity(employee)).thenReturn(entity);

        employeePersistenceAdapter.update(new Employee.EmployeeId("305133f0-c788-4830-8ad9-11a3e8aec531"), employee);

        verify(employeeRepository, times(1)).save(entity);
    }

    @Test
    void findByCellphone() {
        String cellphone = "+11 23 98765-0972";

        Employee employee = new Employee(
                "name",
                "795.093.210-44",
                "+11 23 98765-0972"

        );

        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        when(employeeRepository.findByCellphone(cellphone)).thenReturn(Optional.of(entity));
        when(employeeMapper.toDomain(entity)).thenReturn(employee);

        Optional<Employee> result = employeePersistenceAdapter.findByCellphone(cellphone);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findByCellphone(cellphone);
    }
}