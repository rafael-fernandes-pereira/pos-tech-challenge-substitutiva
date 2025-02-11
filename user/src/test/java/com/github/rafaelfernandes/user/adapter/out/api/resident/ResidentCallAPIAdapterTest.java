package com.github.rafaelfernandes.user.adapter.out.api.resident;

import com.github.rafaelfernandes.user.adapter.out.api.resident.request.ResidentRequest;
import com.github.rafaelfernandes.user.adapter.out.api.resident.response.ResidentIdResponse;
import com.github.rafaelfernandes.user.adapter.out.api.resident.response.ResidentResponse;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ResidentCallAPIAdapterTest {

    @Mock
    private ResidentApiClient residentApiClient;

    @InjectMocks
    private ResidentCallAPIAdapter residentCallAPIAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByCellphone_whenResidentExists() {
        String cellphone = "1234567890";
        when(residentApiClient.findByCellphone(cellphone)).thenReturn(Collections.singletonList(new ResidentResponse("1", "John Doe", "123456789", cellphone, 101)));

        boolean result = residentCallAPIAdapter.exitsByCellphone(cellphone);

        assertTrue(result);
    }

    @Test
    void testExistsByCellphone_whenResidentDoesNotExist() {
        String cellphone = "1234567890";
        when(residentApiClient.findByCellphone(cellphone)).thenReturn(Collections.emptyList());

        boolean result = residentCallAPIAdapter.exitsByCellphone(cellphone);

        assertFalse(result);
    }

    @Test
    void testExistsByApartment_whenResidentExists() {
        Integer apartment = 101;
        when(residentApiClient.findByApartment(apartment)).thenReturn(Optional.of(new ResidentResponse("1", "John Doe", "123456789", "1234567890", apartment)));

        boolean result = residentCallAPIAdapter.exitsByApartment(apartment);

        assertTrue(result);
    }

    @Test
    void testExistsByApartment_whenResidentDoesNotExist() {
        Integer apartment = 101;
        when(residentApiClient.findByApartment(apartment)).thenReturn(Optional.empty());

        boolean result = residentCallAPIAdapter.exitsByApartment(apartment);

        assertFalse(result);
    }

    @Test
    void testSave() {
        Resident resident = new Resident("John Doe", "795.093.210-44", "+55 11 23456-7891", 101);

        UUID residentId = UUID.randomUUID();
        ResidentIdResponse residentIdResponse = new ResidentIdResponse(residentId);

        when(residentApiClient.create(any(ResidentRequest.class))).thenReturn(residentIdResponse);

        String result = residentCallAPIAdapter.save(resident);

        assertEquals(residentId.toString(), result);
    }

    @Test
    void testGetById() {
        String id = UUID.randomUUID().toString();
        ResidentResponse residentResponse = new ResidentResponse(id, "John Doe", "795.093.210-44", "+99 12 93456-7890", 101);

        when(residentApiClient.findById(id)).thenReturn(residentResponse);

        Resident result = residentCallAPIAdapter.getById(id);

        assertEquals(id, result.getResidentId().id());
        assertEquals("John Doe", result.getName());
        assertEquals("795.093.210-44", result.getDocument());
        assertEquals("+99 12 93456-7890", result.getCellphone());
        assertEquals(101, result.getApartment());
    }



}