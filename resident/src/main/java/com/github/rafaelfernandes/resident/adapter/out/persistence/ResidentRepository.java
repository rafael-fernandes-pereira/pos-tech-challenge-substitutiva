package com.github.rafaelfernandes.resident.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<ResidentJpaEntity, UUID> {

    Boolean existsByApartment(Integer apartment);

    Optional<ResidentJpaEntity> findByApartment(Integer apartment);

    Optional<List<ResidentJpaEntity>> findByCellphone(String cellphone);


}
