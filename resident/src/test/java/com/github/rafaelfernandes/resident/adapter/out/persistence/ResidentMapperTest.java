package com.github.rafaelfernandes.resident.adapter.out.persistence;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResidentMapperTest {

    private ResidentMapper residentMapper;

    @BeforeEach
    void setUp() {
        residentMapper = new ResidentMapper();
    }

    @Test
    void testToCreateEntity() {
        // Arrange
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);

        // Act
        ResidentJpaEntity result = residentMapper.toCreateEntity(resident);

        // Assert
        assertNotNull(result);
        assertEquals(resident.getName(), result.getName());
        assertEquals(resident.getDocument(), result.getDocument());
        assertEquals(resident.getCellphone(), result.getCellphone());
        assertEquals(resident.getApartment(), result.getApartment());
    }

    @Test
    void testToDomain() {
        // Arrange
        ResidentJpaEntity residentJpaEntity = ResidentJpaEntity.builder()
                .id(UUID.randomUUID()) // Supondo que a classe ResidentJpaEntity tenha um campo id
                .name("Jane Doe")
                .document("98765432100")
                .cellphone("+55 12 91234-5678")
                .apartment(102)
                .build();

        // Act
        Resident result = residentMapper.toDomain(residentJpaEntity);

        // Assert
        assertNotNull(result);
        assertEquals(residentJpaEntity.getId().toString(), result.getResidentId().id());
        assertEquals(residentJpaEntity.getName(), result.getName());
        assertEquals(residentJpaEntity.getDocument(), result.getDocument());
        assertEquals(residentJpaEntity.getCellphone(), result.getCellphone());
        assertEquals(residentJpaEntity.getApartment(), result.getApartment());
    }

    @Test
    void testToUpdateEntity() {
        // Arrange
        Resident.ResidentId residentId = new Resident.ResidentId(UUID.randomUUID().toString());
        Resident resident = new Resident("John Smith", "827.225.370-44", "+55 12 98765-4321", 103);

        // Act
        ResidentJpaEntity result = residentMapper.toUpdateEntity(residentId, resident);

        // Assert
        assertNotNull(result);
        assertEquals(residentId.id(), result.getId().toString());
        assertEquals(resident.getName(), result.getName());
        assertEquals(resident.getDocument(), result.getDocument());
        assertEquals(resident.getCellphone(), result.getCellphone());
        assertEquals(resident.getApartment(), result.getApartment());
    }

}