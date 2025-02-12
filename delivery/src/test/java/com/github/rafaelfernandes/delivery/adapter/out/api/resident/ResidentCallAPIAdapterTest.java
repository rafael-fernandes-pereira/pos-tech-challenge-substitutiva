package com.github.rafaelfernandes.delivery.adapter.out.api.resident;

import com.github.rafaelfernandes.delivery.adapter.out.api.resident.response.ResidentResponse;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    void testGetById() {
        String id = UUID.randomUUID().toString();
        ResidentResponse residentResponse = new ResidentResponse(id, "John Doe", "795.093.210-44", "+99 12 93456-7890", 101);

        when(residentApiClient.findById(id)).thenReturn(Optional.of(residentResponse));

        var result = residentCallAPIAdapter.getById(id);

        assertEquals(id, result.get().residentId().id());
        assertEquals("John Doe", result.get().name());
        assertEquals("795.093.210-44", result.get().document());
        assertEquals("+99 12 93456-7890", result.get().cellphone());
        assertEquals(101, result.get().apartment());
    }

    @Test
    void testGetByApartment() {
        Integer apartment = 101;
        ResidentResponse residentResponse = new ResidentResponse("resident-id", "John Doe", "795.093.210-44", "+99 12 93456-7890", apartment);

        when(residentApiClient.findByApartment(apartment)).thenReturn(Optional.of(residentResponse));

        Optional<Resident> result = residentCallAPIAdapter.getByApartment(apartment);

        assertTrue(result.isPresent());
        assertEquals("resident-id", result.get().residentId().id());
        assertEquals("John Doe", result.get().name());
        assertEquals("795.093.210-44", result.get().document());
        assertEquals("+99 12 93456-7890", result.get().cellphone());
        assertEquals(apartment, result.get().apartment());
    }

    @Test
    void testGetByApartment_NotFound() {
        Integer apartment = 101;

        when(residentApiClient.findByApartment(apartment)).thenReturn(Optional.empty());

        Optional<Resident> result = residentCallAPIAdapter.getByApartment(apartment);

        assertTrue(result.isEmpty());
    }

}