package com.github.rafaelfernandes.employee.adapter.in.web;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import com.github.rafaelfernandes.employee.application.port.in.EmployeeUseCase;
import com.github.rafaelfernandes.employee.adapter.in.web.request.EmployeeRequest;
import com.github.rafaelfernandes.employee.adapter.in.web.response.EmployeeIdResponse;
import com.github.rafaelfernandes.employee.adapter.in.web.response.EmployeeResponse;
import com.github.rafaelfernandes.employee.adapter.in.web.response.ResponseError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

class EmployeeControllerTest {

    @Mock
    private EmployeeUseCase useCase;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnEmployeeIdResponse() {
        EmployeeRequest request = new EmployeeRequest("John Doe", "795.093.210-44", "+44 44 98765-4321");

        String id = UUID.randomUUID().toString();

        Employee createdEmployee = Employee.of(id, "John Doe", "795.093.210-44", "+44 44 98765-4321");

        when(useCase.create(any(Employee.class))).thenReturn(createdEmployee.getEmployeeId());

        ResponseEntity<EmployeeIdResponse> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().employee_id()).isEqualTo(UUID.fromString(id));
    }

    @Test
    void getAll_ShouldReturnPageOfEmployeeResponse() {
        PageRequest pageable = PageRequest.of(0, 10);
        Employee employee = new Employee("John Doe", "795.093.210-44", "+44 44 98765-4321");
        Page<Employee> employees = new PageImpl<>(List.of(employee), pageable, 1);

        when(useCase.getAll(pageable)).thenReturn(employees);

        ResponseEntity<Page<EmployeeResponse>> response = controller.getAll(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
    }

    @Test
    void getById_ShouldReturnEmployeeResponse() {
        String id = UUID.randomUUID().toString();

        Employee createdEmployee = Employee.of(id, "John Doe", "795.093.210-44", "+44 44 98765-4321");

        when(useCase.findById(id)).thenReturn(createdEmployee);

        ResponseEntity<EmployeeResponse> response = controller.getById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(id);
    }

    @Test
    void deleteById_ShouldReturnSuccessMessage() {
        doNothing().when(useCase).delete("1");

        ResponseEntity<Void> response = controller.deleteById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void updateById_ShouldReturnSuccessMessage() {
        EmployeeRequest request = new EmployeeRequest("John Doe", "795.093.210-44", "+44 44 98765-4321");
        String id = UUID.randomUUID().toString();

        Employee createdEmployee = Employee.of(id, "John Doe", "795.093.210-44", "+44 44 98765-4321");

        when(useCase.findById(id)).thenReturn(createdEmployee);
        doNothing().when(useCase).update(any(Employee.EmployeeId.class), any(Employee.class));

        ResponseEntity<Void> response = controller.updateById(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void getByCellphone_ShouldReturnEmployeeResponse() {
        String id = UUID.randomUUID().toString();

        Employee createdEmployee = Employee.of(id, "John Doe", "795.093.210-44", "+44 44 98765-4321");


        when(useCase.findByCellphone("+44 44 98765-4321")).thenReturn(createdEmployee);

        ResponseEntity<EmployeeResponse> response = controller.getByCellphone("+44 44 98765-4321");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(id);
    }
}