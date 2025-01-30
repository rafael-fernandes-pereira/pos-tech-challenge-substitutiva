package com.github.rafaelfernandes.employee.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeJpaEntity, UUID> {

    Optional<EmployeeJpaEntity> findByCellphone(String cellphone);


}
