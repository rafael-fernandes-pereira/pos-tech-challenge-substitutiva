package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_delivery")
public class DeliveryJpaEntity {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    private UUID id;

    private String residentId;

    private String employeeId;

    private String destinationName;

    private String packageDescription;

    private String notificationStatus;

    private String deliveryStatus;

    private LocalDate enterDate;

    private LocalDate exitDate;

    private String receiverName;

}
