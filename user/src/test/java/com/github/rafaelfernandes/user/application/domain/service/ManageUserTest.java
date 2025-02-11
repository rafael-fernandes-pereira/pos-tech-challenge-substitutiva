package com.github.rafaelfernandes.user.application.domain.service;

import com.github.rafaelfernandes.common.exceptions.ResidentApartmentExistsException;
import com.github.rafaelfernandes.common.exceptions.UserCellphoneExistsException;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.out.EmployeePort;
import com.github.rafaelfernandes.user.application.port.out.ResidentPort;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.common.exceptions.ResidentCellphoneExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageUserTest {

    @Mock
    private ResidentPort residentPort;

    @Mock
    private UserPort userPort;

    @Mock
    private EmployeePort employeePort;

    @InjectMocks
    private ManageUser manageUser;

    private Resident resident;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        resident = mock(Resident.class);
        MockitoAnnotations.openMocks(this);
        resident = new Resident(
                "John Doe",
                "795.093.210-44",
                "+55 12 98765-4321",
                101
        );

        employee = new Employee(
                "Jane Doe",
                "795.093.210-44",
                "+55 12 98765-4321"
        );


    }

    @Test
    public void testCreateResident_Success() {
        when(userPort.findByCellphone(resident.getCellphone())).thenReturn(Optional.empty());
        when(residentPort.exitsByCellphone(resident.getCellphone())).thenReturn(false);
        when(residentPort.exitsByApartment(resident.getApartment())).thenReturn(false);
        when(residentPort.save(resident)).thenReturn("newResidentId");
        when(residentPort.getById("newResidentId")).thenReturn(resident);
        when(userPort.save(any(User.class))).thenReturn(new User(resident));

        String password = manageUser.createResident(resident);

        assertNotNull(password);
        verify(userPort).findByCellphone(resident.getCellphone());
        verify(residentPort).exitsByCellphone(resident.getCellphone());
        verify(residentPort).exitsByApartment(resident.getApartment());
        verify(residentPort).save(resident);
        verify(residentPort).getById("newResidentId");
        verify(userPort).save(any(User.class));
    }

    @Test
    public void testCreateResident_UserCellphoneExists() {
        when(userPort.findByCellphone(resident.getCellphone())).thenReturn(Optional.of(new User(resident)));

        assertThrows(UserCellphoneExistsException.class, () -> manageUser.createResident(resident));

        verify(userPort).findByCellphone(resident.getCellphone());
        verify(residentPort, never()).exitsByCellphone(anyString());
        verify(residentPort, never()).exitsByApartment(anyInt());
        verify(residentPort, never()).save(any(Resident.class));
        verify(residentPort, never()).getById(anyString());
        verify(userPort, never()).save(any(User.class));
    }

    @Test
    public void testCreateResident_ResidentCellphoneExists() {
        when(userPort.findByCellphone(resident.getCellphone())).thenReturn(Optional.empty());
        when(residentPort.exitsByCellphone(resident.getCellphone())).thenReturn(true);

        assertThrows(UserCellphoneExistsException.class, () -> manageUser.createResident(resident));

        verify(userPort).findByCellphone(resident.getCellphone());
        verify(residentPort).exitsByCellphone(resident.getCellphone());
        verify(residentPort, never()).exitsByApartment(anyInt());
        verify(residentPort, never()).save(any(Resident.class));
        verify(residentPort, never()).getById(anyString());
        verify(userPort, never()).save(any(User.class));
    }

    @Test
    public void testCreateResident_ResidentApartmentExists() {
        when(userPort.findByCellphone(resident.getCellphone())).thenReturn(Optional.empty());
        when(residentPort.exitsByCellphone(resident.getCellphone())).thenReturn(false);
        when(residentPort.exitsByApartment(resident.getApartment())).thenReturn(true);

        assertThrows(ResidentApartmentExistsException.class, () -> manageUser.createResident(resident));

        verify(userPort).findByCellphone(resident.getCellphone());
        verify(residentPort).exitsByCellphone(resident.getCellphone());
        verify(residentPort).exitsByApartment(resident.getApartment());
        verify(residentPort, never()).save(any(Resident.class));
        verify(residentPort, never()).getById(anyString());
        verify(userPort, never()).save(any(User.class));
    }

    @Test
    public void testCreateEmployee_Success() {
        when(userPort.findByCellphone(employee.getCellphone())).thenReturn(Optional.empty());
        when(employeePort.exitsByCellphone(employee.getCellphone())).thenReturn(false);
        when(employeePort.save(employee)).thenReturn("newEmployeeId");
        when(employeePort.getById("newEmployeeId")).thenReturn(employee);
        when(userPort.save(any(User.class))).thenReturn(new User(employee));

        String password = manageUser.createEmployee(employee);

        assertNotNull(password);
        verify(userPort).findByCellphone(employee.getCellphone());
        verify(employeePort).exitsByCellphone(employee.getCellphone());
        verify(employeePort).save(employee);
        verify(employeePort).getById("newEmployeeId");
        verify(userPort).save(any(User.class));
    }

    @Test
    public void testCreateEmployee_UserCellphoneExists() {
        when(userPort.findByCellphone(employee.getCellphone())).thenReturn(Optional.of(new User(employee)));

        assertThrows(UserCellphoneExistsException.class, () -> manageUser.createEmployee(employee));

        verify(userPort).findByCellphone(employee.getCellphone());
        verify(employeePort, never()).exitsByCellphone(anyString());
        verify(employeePort, never()).save(any(Employee.class));
        verify(employeePort, never()).getById(anyString());
        verify(userPort, never()).save(any(User.class));
    }

    @Test
    public void testCreateEmployee_EmployeeCellphoneExists() {
        when(userPort.findByCellphone(employee.getCellphone())).thenReturn(Optional.empty());
        when(employeePort.exitsByCellphone(employee.getCellphone())).thenReturn(true);

        assertThrows(UserCellphoneExistsException.class, () -> manageUser.createEmployee(employee));

        verify(userPort).findByCellphone(employee.getCellphone());
        verify(employeePort).exitsByCellphone(employee.getCellphone());
        verify(employeePort, never()).save(any(Employee.class));
        verify(employeePort, never()).getById(anyString());
        verify(userPort, never()).save(any(User.class));
    }



}