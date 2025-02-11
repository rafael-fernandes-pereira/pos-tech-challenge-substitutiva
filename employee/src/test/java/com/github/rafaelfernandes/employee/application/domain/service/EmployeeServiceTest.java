package com.github.rafaelfernandes.employee.application.domain.service;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import com.github.rafaelfernandes.employee.application.domain.service.EmployeeService;
import com.github.rafaelfernandes.employee.application.port.out.ManageEmployeePort;
import com.github.rafaelfernandes.employee.common.exception.EmployeeCellphoneExistsException;
import com.github.rafaelfernandes.employee.common.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private ManageEmployeePort manageEmployeePort;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldThrowException_WhenEmployeeCellphoneExists() {
        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findByCellphone(employee.getCellphone())).thenReturn(Optional.of(employee));

        assertThrows(EmployeeCellphoneExistsException.class, () -> employeeService.create(employee));
    }

    @Test
    void create_ShouldReturnEmployeeId_WhenEmployeeDoesNotExist() {
        Employee.EmployeeId employeeId = new Employee.EmployeeId(UUID.randomUUID().toString());
        Employee employee = Employee.of(
                employeeId.id(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findByCellphone(employee.getCellphone())).thenReturn(Optional.empty());
        when(manageEmployeePort.save(employee)).thenReturn(employee);

        Employee.EmployeeId result = employeeService.create(employee);

        assertEquals(employeeId, result);
    }

    @Test
    void getAll_ShouldReturnAllEmployees() {
        Pageable pageable = PageRequest.of(0, 10);

        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        Page<Employee> employees = new PageImpl<>(Collections.singletonList(employee));

        when(manageEmployeePort.getAll(pageable)).thenReturn(employees);

        Page<Employee> result = employeeService.getAll(pageable);

        assertEquals(employees, result);
    }

    @Test
    void delete_ShouldThrowException_WhenEmployeeNotFound() {
        String residentId = "1";

        when(manageEmployeePort.findById(residentId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.delete(residentId));
    }

    @Test
    void delete_ShouldDeleteEmployee_WhenEmployeeExists() {
        String residentId = "1";
        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findById(residentId)).thenReturn(Optional.of(employee));

        employeeService.delete(residentId);

        verify(manageEmployeePort, times(1)).delete(employee);
    }

    @Test
    void update_ShouldThrowException_WhenEmployeeNotFound() {
        Employee.EmployeeId employeeId = new Employee.EmployeeId(UUID.randomUUID().toString());
        Employee employee = Employee.of(
                employeeId.id(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findById(employeeId.id())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(employeeId, employee));
    }

    @Test
    void update_ShouldUpdateEmployee_WhenEmployeeExists() {
        Employee.EmployeeId employeeId = new Employee.EmployeeId(UUID.randomUUID().toString());
        Employee employee = Employee.of(
                employeeId.id(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findById(employeeId.id())).thenReturn(Optional.of(employee));

        employeeService.update(employeeId, employee);

        verify(manageEmployeePort, times(1)).update(employeeId, employee);
    }

    @Test
    void findById_ShouldThrowException_WhenEmployeeNotFound() {
        String residentId = "1";

        when(manageEmployeePort.findById(residentId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(residentId));
    }

    @Test
    void findById_ShouldReturnEmployee_WhenEmployeeExists() {
        String residentId = "1";
        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findById(residentId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findById(residentId);

        assertEquals(employee, result);
    }

    @Test
    void findByCellphone_ShouldThrowException_WhenEmployeeNotFound() {
        String cellphone = "123456789";

        when(manageEmployeePort.findByCellphone(cellphone)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findByCellphone(cellphone));
    }

    @Test
    void findByCellphone_ShouldReturnEmployee_WhenEmployeeExists() {
        String cellphone = "123456789";
        Employee employee = Employee.of(
                UUID.randomUUID().toString(),
                "Jane Doe",
                "795.093.210-44",
                "+55 11 98765-4321"
        );

        when(manageEmployeePort.findByCellphone(cellphone)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findByCellphone(cellphone);

        assertEquals(employee, result);
    }
}
