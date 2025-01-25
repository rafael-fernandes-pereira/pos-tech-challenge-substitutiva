package com.github.rafaelfernandes.resident.application.domain.service;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import com.github.rafaelfernandes.resident.application.port.out.ManageResidentPort;
import com.github.rafaelfernandes.resident.common.exception.ResidentApartmentExistsException;
import com.github.rafaelfernandes.resident.common.exception.ResidentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageResidentServiceTest {

    @Mock
    private ManageResidentPort manageResidentPort;

    @InjectMocks
    private ManageResidentService manageResidentService;

    private Resident resident;
    private Resident resident1;
    private Resident resident2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        resident1 = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        resident2 = new Resident("Jane Doe", "98765432100", "+55 12 98765-4322", 102);
    }

    @Test
    void testCreateResidentSuccessfully() {
        when(manageResidentPort.existsByApartment(resident.getApartment())).thenReturn(false);
        when(manageResidentPort.save(resident)).thenReturn(resident);

        Resident.ResidentId residentId = manageResidentService.create(resident);

        assertNotNull(residentId);
        verify(manageResidentPort).existsByApartment(resident.getApartment());
        verify(manageResidentPort).save(resident);
    }

    @Test
    void testCreateResidentThrowsExceptionWhenApartmentExists() {
        when(manageResidentPort.existsByApartment(resident.getApartment())).thenReturn(true);

        assertThrows(ResidentApartmentExistsException.class, () -> {
            manageResidentService.create(resident);
        });

        verify(manageResidentPort, never()).save(resident);
    }

    @Test
    void testGetAllResidents() {
        // Arrange
        List<Resident> residents = List.of(resident1, resident2);
        Page<Resident> residentPage = new PageImpl<>(residents, PageRequest.of(0, 10), residents.size());

        when(manageResidentPort.getAll(any(Pageable.class))).thenReturn(residentPage);

        // Act
        Page<Resident> result = manageResidentService.getAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(resident1, result.getContent().get(0));
        assertEquals(resident2, result.getContent().get(1));
        verify(manageResidentPort).getAll(any(Pageable.class));
    }

    @Test
    void testFindByIdSuccessfully() {
        when(manageResidentPort.findById(resident.getResidentId().id())).thenReturn(Optional.of(resident));

        Resident result = manageResidentService.findById(resident.getResidentId().id());

        assertNotNull(result);
        assertEquals(resident, result);
        verify(manageResidentPort).findById(resident.getResidentId().id());
    }

    @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
        when(manageResidentPort.findById(resident.getResidentId().id())).thenReturn(Optional.empty());

        assertThrows(ResidentNotFoundException.class, () -> {
            manageResidentService.findById(resident.getResidentId().id());
        });

        verify(manageResidentPort).findById(resident.getResidentId().id());
    }

    @Test
    void testFindByApartmentSuccessfully() {
        when(manageResidentPort.findByApartment(resident.getApartment())).thenReturn(Optional.of(resident));

        Resident result = manageResidentService.findByApartment(resident.getApartment());

        assertNotNull(result);
        assertEquals(resident, result);
        verify(manageResidentPort).findByApartment(resident.getApartment());
    }

    @Test
    void testFindByApartmentThrowsExceptionWhenNotFound() {
        when(manageResidentPort.findByApartment(resident.getApartment())).thenReturn(Optional.empty());

        assertThrows(ResidentNotFoundException.class, () -> {
            manageResidentService.findByApartment(resident.getApartment());
        });

        verify(manageResidentPort).findByApartment(resident.getApartment());
    }

    @Test
    void testDeleteResidentSuccessfully() {
        when(manageResidentPort.findById(resident.getResidentId().id())).thenReturn(Optional.of(resident));

        manageResidentService.delete(resident.getResidentId().id());

        verify(manageResidentPort).findById(resident.getResidentId().id());
        verify(manageResidentPort).delete(resident);
    }

    @Test
    void testDeleteResidentThrowsExceptionWhenNotFound() {
        when(manageResidentPort.findById(resident.getResidentId().id())).thenReturn(Optional.empty());

        assertThrows(ResidentNotFoundException.class, () -> {
            manageResidentService.delete(resident.getResidentId().id());
        });

        verify(manageResidentPort).findById(resident.getResidentId().id());
        verify(manageResidentPort, never()).delete(any(Resident.class));
    }
}