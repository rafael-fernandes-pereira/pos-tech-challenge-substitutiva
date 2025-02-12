package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.adapter.out.persistence.entity.DeliveryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepositpory extends JpaRepository<DeliveryJpaEntity, UUID> {

    List<DeliveryJpaEntity> findByResidentId(String residentId);

}
