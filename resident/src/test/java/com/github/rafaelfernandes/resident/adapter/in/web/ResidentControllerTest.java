package com.github.rafaelfernandes.resident.adapter.in.web;

import com.github.rafaelfernandes.resident.adapter.in.web.request.ResidentRequest;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResidentIdResponse;
import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import com.github.rafaelfernandes.resident.application.port.in.ResidentUseCase;
import com.github.rafaelfernandes.resident.common.exception.ResidentApartmentExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rafaelfernandes.resident.common.exception.ResidentNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ResidentControllerTest {

    @Mock
    private ResidentUseCase residentUseCase;

    @InjectMocks
    private ResidentController residentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(residentController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateResidentSuccess() throws Exception {
        // Arrange
        ResidentRequest request = new ResidentRequest("John Doe", "12345678909", "+55 12 98765-4321", 101);
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        Resident.ResidentId residentId = new Resident.ResidentId("550e8400-e29b-41d4-a716-446655440000");

        when(residentUseCase.create(any(Resident.class))).thenReturn(residentId);

        // Act & Assert
        mockMvc.perform(post("/api/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resident_id").value(residentId.id()));

        verify(residentUseCase).create(any(Resident.class));
    }

    @Test
    void testCreateResidentThrowsResidentApartmentExistsException() throws Exception {
        // Arrange
        ResidentRequest request = new ResidentRequest("John Doe", "12345678909", "+55 12 98765-4321", 101);
        when(residentUseCase.create(any(Resident.class))).thenThrow(new ResidentApartmentExistsException(101));

        // Act & Assert
        mockMvc.perform(post("/api/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident with apartment 101 already exists"))
                .andExpect(jsonPath("$.status").value(409));

        verify(residentUseCase).create(any(Resident.class));
    }

    @Test
    void testShowAllResidents() throws Exception {
        // Arrange
        Resident resident1 = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        Resident resident2 = new Resident("Jane Doe", "98765432100", "+55 12 98765-4322", 102);
        List<Resident> residents = List.of(resident1, resident2);
        Page<Resident> residentPage = new PageImpl<>(residents, PageRequest.of(0, 10), residents.size());

        when(residentUseCase.getAll(any(Pageable.class))).thenReturn(residentPage);

        // Act & Assert
        mockMvc.perform(get("/api/resident")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Jane Doe"));

        verify(residentUseCase).getAll(any(Pageable.class));
    }

    @Test
    void testGetByIdSuccess() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        when(residentUseCase.findById(residentId)).thenReturn(resident);

        // Act & Assert
        mockMvc.perform(get("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.document").value("12345678909"))
                .andExpect(jsonPath("$.cellphone").value("+55 12 98765-4321"))
                .andExpect(jsonPath("$.apartment").value(101));

        verify(residentUseCase).findById(residentId);
    }


    @Test
    void testGetByIdNotFound() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        when(residentUseCase.findById(residentId)).thenThrow(new ResidentNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(residentUseCase).findById(residentId);
    }

    @Test
    void testCreateResidentThrowsValidationException() throws Exception {
        // Arrange
        ResidentRequest request = new ResidentRequest("", "invalid_document", "invalid_phone", -1);
        when(residentUseCase.create(any(Resident.class))).thenThrow(new ValidationException("Invalid data"));

        // Act & Assert
        mockMvc.perform(post("/api/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400));


    }

    @Test
    void testGetByApartmentSuccess() throws Exception {
        // Arrange
        var apartment = 101;
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        when(residentUseCase.findByApartment(apartment)).thenReturn(resident);

        // Act & Assert
        mockMvc.perform(get("/api/resident/apartment/{apartment}", apartment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.document").value("12345678909"))
                .andExpect(jsonPath("$.cellphone").value("+55 12 98765-4321"))
                .andExpect(jsonPath("$.apartment").value(101));

        verify(residentUseCase).findByApartment(apartment);
    }


    @Test
    void testGetByApartmentNotFound() throws Exception {
        // Arrange
        var apartment = 101;
        when(residentUseCase.findByApartment(apartment)).thenThrow(new ResidentNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/api/resident/apartment/{apartment}", apartment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(residentUseCase).findByApartment(apartment);
    }

    @Test
    void testDeleteByIdSuccess() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        doNothing().when(residentUseCase).delete(residentId);

        // Act & Assert
        mockMvc.perform(delete("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Resident deleted successfully"));

        verify(residentUseCase).delete(residentId);
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        doThrow(new ResidentNotFoundException()).when(residentUseCase).delete(residentId);

        // Act & Assert
        mockMvc.perform(delete("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(residentUseCase).delete(residentId);
    }

    @Test
    void testUpdateByIdSuccess() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        ResidentRequest request = new ResidentRequest("John Doe", "12345678909", "+55 12 98765-4321", 101);
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);

        when(residentUseCase.findById(residentId)).thenReturn(resident);
        doNothing().when(residentUseCase).update(any(Resident.ResidentId.class), any(Resident.class));

        // Act & Assert
        mockMvc.perform(put("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Resident updated successfully"));

        verify(residentUseCase).findById(residentId);
        verify(residentUseCase).update(any(Resident.ResidentId.class), any(Resident.class));
    }

    @Test
    void testUpdateByIdNotFound() throws Exception {
        // Arrange
        String residentId = "550e8400-e29b-41d4-a716-446655440000";
        ResidentRequest request = new ResidentRequest("John Doe", "12345678909", "+55 12 98765-4321", 101);

        when(residentUseCase.findById(residentId)).thenThrow(new ResidentNotFoundException());

        // Act & Assert
        mockMvc.perform(put("/api/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(residentUseCase).findById(residentId);
    }

    @Test
    void testGetByCellphoneSuccess() throws Exception {
        // Arrange
        String cellphone = "+55 12 98765-4321";
        Resident resident1 = new Resident("John Doe", "12345678909", cellphone, 101);
        Resident resident2 = new Resident("Jane Doe", "98765432100", cellphone, 102);
        List<Resident> residents = List.of(resident1, resident2);

        when(residentUseCase.findByCellphone(cellphone)).thenReturn(residents);

        // Act & Assert
        mockMvc.perform(get("/api/resident/cellphone/{cellphone}", cellphone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(residentUseCase).findByCellphone(cellphone);
    }

    @Test
    void testGetByCellphoneNotFound() throws Exception {
        // Arrange
        String cellphone = "+55 12 98765-4321";
        when(residentUseCase.findByCellphone(cellphone)).thenThrow(new ResidentNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/api/resident/cellphone/{cellphone}", cellphone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Resident not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(residentUseCase).findByCellphone(cellphone);
    }


}