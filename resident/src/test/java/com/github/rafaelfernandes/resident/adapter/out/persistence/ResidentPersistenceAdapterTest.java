package com.github.rafaelfernandes.resident.adapter.out.persistence;

import com.github.rafaelfernandes.resident.adapter.out.persistence.entity.ResidentJpaEntity;
import com.github.rafaelfernandes.resident.application.domain.model.Resident;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResidentPersistenceAdapterTest {

    @Mock
    private ResidentRepository repository;

    @Mock
    private ResidentMapper mapper;

    @InjectMocks
    private ResidentPersistenceAdapter residentPersistenceAdapter;

    private Resident resident;
    private ResidentJpaEntity entity;


    private Resident resident1;
    private Resident resident2;
    private ResidentJpaEntity entity1;
    private ResidentJpaEntity entity2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        entity = new ResidentJpaEntity();

        resident1 = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        resident2 = new Resident("Jane Doe", "98765432100", "+55 12 98765-4322", 102);
        entity1 = new ResidentJpaEntity();
        entity2 = new ResidentJpaEntity();
    }


    @Test
    void testExistsByApartmentReturnsTrue() {
        Integer apartment = 101;
        when(repository.existsByApartment(apartment)).thenReturn(true);

        Boolean result = residentPersistenceAdapter.existsByApartment(apartment);

        assertTrue(result);
        verify(repository).existsByApartment(apartment);
    }

    @Test
    void testExistsByApartmentReturnsFalse() {
        Integer apartment = 102;
        when(repository.existsByApartment(apartment)).thenReturn(false);

        Boolean result = residentPersistenceAdapter.existsByApartment(apartment);

        assertFalse(result);
        verify(repository).existsByApartment(apartment);
    }

    @Test
    void testSaveResidentSuccessfully() {
        when(mapper.toCreateEntity(resident)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(resident);

        Resident result = residentPersistenceAdapter.save(resident);

        assertNotNull(result);
        assertEquals(resident.getName(), result.getName());
        verify(mapper).toCreateEntity(resident);
        verify(repository).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testSaveResidentThrowsExceptionWhenMapperFails() {
        when(mapper.toCreateEntity(resident)).thenReturn(entity);
        when(repository.save(entity)).thenThrow(new RuntimeException("Error saving entity"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            residentPersistenceAdapter.save(resident);
        });

        assertEquals("Error saving entity", exception.getMessage());
        verify(mapper).toCreateEntity(resident);
        verify(repository).save(entity);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testGetAllResidents() {
        // Arrange
        List<ResidentJpaEntity> entities = List.of(entity1, entity2);
        Page<ResidentJpaEntity> entityPage = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);
        when(mapper.toDomain(entity1)).thenReturn(resident1);
        when(mapper.toDomain(entity2)).thenReturn(resident2);

        // Act
        Page<Resident> result = residentPersistenceAdapter.getAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(resident1, result.getContent().get(0));
        assertEquals(resident2, result.getContent().get(1));
        verify(repository).findAll(any(Pageable.class));
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }

    @Test
    void testFindById() {
        String residentId = UUID.randomUUID().toString();
        UUID id = UUID.fromString(residentId);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(resident);

        Optional<Resident> result = residentPersistenceAdapter.findById(residentId);

        assertTrue(result.isPresent());
        assertEquals(resident, result.get());
        verify(repository).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testFindByIdNotFound() {
        String residentId = UUID.randomUUID().toString();
        UUID id = UUID.fromString(residentId);
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Resident> result = residentPersistenceAdapter.findById(residentId);

        assertFalse(result.isPresent());
        verify(repository).findById(id);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testFindByApartment() {

        var apartment = 101;
        when(repository.findByApartment(apartment)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(resident);

        Optional<Resident> result = residentPersistenceAdapter.findByApartment(apartment);

        assertTrue(result.isPresent());
        assertEquals(resident, result.get());
        verify(repository).findByApartment(apartment);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testFindByApartmentNotFound() {

        var apartment = 101;
        when(repository.findByApartment(apartment)).thenReturn(Optional.empty());



        Optional<Resident> result = residentPersistenceAdapter.findByApartment(apartment);

        assertFalse(result.isPresent());
        verify(repository).findByApartment(apartment);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testDeleteResident() {
        when(mapper.toCreateEntity(resident)).thenReturn(entity);

        residentPersistenceAdapter.delete(resident);

        verify(mapper).toCreateEntity(resident);
        verify(repository).delete(entity);
    }

    @Test
    void testUpdateResident() {
        Resident.ResidentId residentId = new Resident.ResidentId(UUID.randomUUID().toString());
        when(mapper.toUpdateEntity(residentId, resident)).thenReturn(entity);

        residentPersistenceAdapter.update(residentId, resident);

        verify(mapper).toUpdateEntity(residentId, resident);
        verify(repository).save(entity);
    }

    @Test
    void testFindByCellphone() {
        String cellphone = "+55 12 98765-4321";
        List<ResidentJpaEntity> entities = List.of(entity1, entity2);
        when(repository.findByCellphone(cellphone)).thenReturn(Optional.of(entities));
        when(mapper.toDomain(entity1)).thenReturn(resident1);
        when(mapper.toDomain(entity2)).thenReturn(resident2);

        List<Resident> result = residentPersistenceAdapter.findByCellphone(cellphone);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(resident1, result.get(0));
        assertEquals(resident2, result.get(1));
        verify(repository).findByCellphone(cellphone);
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }

    @Test
    void testFindByCellphoneNotFound() {
        String cellphone = "+55 12 98765-4321";
        when(repository.findByCellphone(cellphone)).thenReturn(Optional.empty());

        List<Resident> result = residentPersistenceAdapter.findByCellphone(cellphone);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findByCellphone(cellphone);
        verify(mapper, never()).toDomain(any());
    }
}