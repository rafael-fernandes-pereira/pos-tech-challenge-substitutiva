package com.github.rafaelfernandes.user.application.domain.service;

import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.out.ResidentPort;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.user.common.exceptions.ResidentCellphoneExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageUserTest {

    @Mock
    private ResidentPort residentPort;

    @Mock
    private UserPort userPort;

    @InjectMocks
    private ManageUser manageUser;

    private Resident resident;

    @BeforeEach
    public void setUp() {
        resident = mock(Resident.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateResident_ResidentExists() {
        when(residentPort.exitsByCellphone(resident.getCellphone())).thenReturn(Boolean.TRUE);

        assertThrows(ResidentCellphoneExistsException.class, () -> {
            manageUser.createResident(resident);
        });

    }

    @Test
    public void testCreateResident_NewResident() {
        when(residentPort.exitsByCellphone(resident.getCellphone())).thenReturn(Boolean.FALSE);

        when(residentPort.save(resident)).thenReturn("251e9b90-3cdf-4af6-b2de-e507864e71fd");
        when(residentPort.getById("251e9b90-3cdf-4af6-b2de-e507864e71fd")).thenReturn(resident);

        when(userPort.save(any(User.class))).thenReturn(new User(resident));
        String password = manageUser.createResident(resident);

        assertNotNull(password);
        verify(residentPort, times(1)).exitsByCellphone(resident.getCellphone());
        verify(residentPort, times(1)).save(resident);
        verify(userPort, times(1)).save(any(User.class));
    }

}