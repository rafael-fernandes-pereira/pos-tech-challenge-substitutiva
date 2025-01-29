package com.github.rafaelfernandes.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

     Optional<UserEntity> findByCellphone(String cellphone);

}
