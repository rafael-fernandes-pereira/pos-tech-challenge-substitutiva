package com.github.rafaelfernandes.resident.adapter.out.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ResidentRepositoryIntegrationTest {

    @Autowired
    private ResidentRepository residentRepository;

    @Test
    public void testSaveAndFindResident() {
        // Criação de um novo residente
        ResidentJpaEntity resident = ResidentJpaEntity.builder()
                .name("John Doe")
                .document("123456789")
                .cellphone("987654321")
                .apartment(101)
                .build();

        // Salvar o residente no banco de dados
        ResidentJpaEntity savedResident = residentRepository.save(resident);

        // Recuperar o residente do banco de dados
        Optional<ResidentJpaEntity> foundResident = residentRepository.findById(savedResident.getId());

        // Verificar se o residente foi encontrado e seus dados estão corretos
        assertThat(foundResident).isPresent();
        assertThat(foundResident.get().getName()).isEqualTo("John Doe");
        assertThat(foundResident.get().getDocument()).isEqualTo("123456789");
        assertThat(foundResident.get().getCellphone()).isEqualTo("987654321");
        assertThat(foundResident.get().getApartment()).isEqualTo(101);
    }

}