package com.github.rafaelfernandes.delivery.adapter.out.api.employee;

import com.github.rafaelfernandes.delivery.adapter.out.api.employee.response.EmployeeResponse;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class EmployeeCallAPIAdapterTest {

    @Mock
    private EmployeeApiClient employeeApiClient;

    @InjectMocks
    private EmployeeCallAPIAdapter employeeCallAPIAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByCellphone() {
        String cellphone = "+99 12 93456-7890";
        EmployeeResponse employeeResponse = new EmployeeResponse("employee-id", "John Doe", "795.093.210-44", cellphone);

        when(employeeApiClient.findByCellphone(cellphone)).thenReturn(Optional.of(employeeResponse));

        Optional<Employee> result = employeeCallAPIAdapter.findByCellphone(cellphone);

        assertTrue(result.isPresent());
        assertEquals("employee-id", result.get().employeeId().id());
        assertEquals("John Doe", result.get().name());
        assertEquals("795.093.210-44", result.get().document());
        assertEquals(cellphone, result.get().cellphone());
    }

    @Test
    void testFindByCellphone_NotFound() {
        String cellphone = "+99 12 93456-7890";

        when(employeeApiClient.findByCellphone(cellphone)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeCallAPIAdapter.findByCellphone(cellphone);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindById() {
        String employeeId = "employee-id";
        EmployeeResponse employeeResponse = new EmployeeResponse(employeeId, "John Doe", "795.093.210-44", "+99 12 93456-7890");

        when(employeeApiClient.findById(employeeId)).thenReturn(Optional.of(employeeResponse));

        Optional<Employee> result = employeeCallAPIAdapter.findById(employeeId);

        assertTrue(result.isPresent());
        assertEquals(employeeId, result.get().employeeId().id());
        assertEquals("John Doe", result.get().name());
        assertEquals("795.093.210-44", result.get().document());
        assertEquals("+99 12 93456-7890", result.get().cellphone());
    }

    @Test
    void testFindById_NotFound() {
        String employeeId = "employee-id";

        when(employeeApiClient.findById(employeeId)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeCallAPIAdapter.findById(employeeId);

        assertTrue(result.isEmpty());
    }
}