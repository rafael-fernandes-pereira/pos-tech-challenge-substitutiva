package com.github.rafaelfernandes.user.adapter.out.api.employee;

import com.github.rafaelfernandes.user.adapter.out.api.employee.request.EmployeeRequest;
import com.github.rafaelfernandes.user.adapter.out.api.employee.response.EmployeeIdResponse;
import com.github.rafaelfernandes.user.adapter.out.api.employee.response.EmployeeResponse;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeCallAPIAdapterTest {

    @Mock
    private EmployeeApiClient employeeApiClient;

    @InjectMocks
    private EmployeeCallAPIAdapter employeeCallAPIAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExitsByCellphone_EmployeeExists() {
        String cellphone = "+12 34 56789-9675";
        EmployeeResponse employeeResponse = new EmployeeResponse("1", "Jane Doe", "123456789", cellphone);

        when(employeeApiClient.findByCellphone(cellphone)).thenReturn(employeeResponse);

        Boolean result = employeeCallAPIAdapter.exitsByCellphone(cellphone);

        assertTrue(result);
        verify(employeeApiClient).findByCellphone(cellphone);
    }

    @Test
    public void testExitsByCellphone_EmployeeDoesNotExist() {
        String cellphone = "+12 34 56789-9675";
        EmployeeResponse employeeResponse = new EmployeeResponse(null, "Jane Doe", "123456789", cellphone);

        when(employeeApiClient.findByCellphone(cellphone)).thenReturn(employeeResponse);

        Boolean result = employeeCallAPIAdapter.exitsByCellphone(cellphone);

        assertFalse(result);
        verify(employeeApiClient).findByCellphone(cellphone);
    }

    @Test
    public void testSave_Success() {

        String id = UUID.randomUUID().toString();

        Employee employee = new Employee("Jane Doe", "795.093.210-44", "+12 34 56789-9675");
        EmployeeRequest employeeRequest = new EmployeeRequest(employee.getName(), employee.getDocument(), employee.getCellphone());

        EmployeeIdResponse idResponse = new EmployeeIdResponse(UUID.fromString(id));

        when(employeeApiClient.create(employeeRequest)).thenReturn(idResponse);

        String result = employeeCallAPIAdapter.save(employee);

        assertEquals(id, result);
        verify(employeeApiClient).create(employeeRequest);
    }

    @Test
    public void testGetById_Success() {
        String id = UUID.randomUUID().toString();
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Jane Doe", "795.093.210-44", "+12 34 56789-9675");

        when(employeeApiClient.findById(id)).thenReturn(employeeResponse);

        Employee result = employeeCallAPIAdapter.getById(id);

        assertEquals(id, result.getEmployeeId().id());
        assertEquals(employeeResponse.name(), result.getName());
        assertEquals(employeeResponse.document(), result.getDocument());
        assertEquals(employeeResponse.cellphone(), result.getCellphone());
        verify(employeeApiClient).findById(id);
    }
}