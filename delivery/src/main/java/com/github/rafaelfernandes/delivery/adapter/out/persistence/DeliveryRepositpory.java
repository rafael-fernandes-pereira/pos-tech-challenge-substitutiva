package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepositpory extends JpaRepository<DeliveryJpaEntity, UUID> {
}
