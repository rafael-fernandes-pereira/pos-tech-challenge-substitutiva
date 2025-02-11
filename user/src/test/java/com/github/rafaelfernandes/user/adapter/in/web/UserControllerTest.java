package com.github.rafaelfernandes.user.adapter.in.web;

import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.user.adapter.in.web.request.EmployeeRequest;
import com.github.rafaelfernandes.user.adapter.in.web.request.ResidentRequest;
import com.github.rafaelfernandes.user.adapter.in.web.response.UserCreatedResponse;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.port.in.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    private String password;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        password = PasswordUtils.generatePassayPassword();
    }

    @Test
    public void testCreateResident_Success() {
        ResidentRequest residentRequest = new ResidentRequest("John Doe", "795.093.210-44", "+12 34 56789-9675", 101);
        String createdUserPassword = password;

        when(userUseCase.createResident(any(Resident.class))).thenReturn(createdUserPassword);

        UserCreatedResponse response = userController.create(residentRequest);

        assertEquals(residentRequest.cellphone(), response.cellphone());
        assertEquals(createdUserPassword, response.password());

    }

    @Test
    public void testCreateEmployee_Success() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Jane Doe", "795.093.210-44", "+12 34 56789-9675");
        String createdUserPassword = password;

        when(userUseCase.createEmployee(any(Employee.class))).thenReturn(createdUserPassword);

        UserCreatedResponse response = userController.create(employeeRequest);

        assertEquals(employeeRequest.cellphone(), response.cellphone());
        assertEquals(createdUserPassword, response.password());
        verify(userUseCase).createEmployee(any(Employee.class));
    }
}