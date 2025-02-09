package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class DeliveryMapper {

    public static DeliveryJpaEntity toEntity(Delivery delivery) {
        return DeliveryJpaEntity.builder()
                .residentId(delivery.getResident().residentId().id())
                .employeeId(delivery.getEmployee().employeeId().id())
                .deliveryStatus(delivery.getDeliveryStatus())
                .notificationStatus(delivery.getNotificationStatus())
                .enterDate(delivery.getEnterDate())
                .receiverName(delivery.getReceiverName())
                .destinationName(delivery.getDestinationName())
                .packageDescription(delivery.getPackageDescription())
                .build();
    }

    public static DeliveryJpaEntity toSavedEntity(Delivery delivery) {
        return DeliveryJpaEntity.builder()
                .id(UUID.fromString(delivery.getId().id()))
                .residentId(delivery.getResident().residentId().id())
                .employeeId(delivery.getEmployee().employeeId().id())
                .deliveryStatus(delivery.getDeliveryStatus())
                .notificationStatus(delivery.getNotificationStatus())
                .enterDate(delivery.getEnterDate())
                .receiverName(delivery.getReceiverName())
                .destinationName(delivery.getDestinationName())
                .packageDescription(delivery.getPackageDescription())
                .build();
    }

    public static Delivery toDomain(DeliveryJpaEntity deliveryJpaEntity) {

        var resident = new Resident(
                new Resident.ResidentId(deliveryJpaEntity.getResidentId()),
                null,
                null,
                null,
                null
        );

        var employee = new Employee(
                new Employee.EmployeeId(deliveryJpaEntity.getEmployeeId()),
                null,
                null,
                null
        );

        return Delivery.of(
                deliveryJpaEntity.getId().toString(),
                resident,
                employee,
                deliveryJpaEntity.getDestinationName(),
                deliveryJpaEntity.getPackageDescription(),
                deliveryJpaEntity.getDeliveryStatus(),
                deliveryJpaEntity.getNotificationStatus(),
                deliveryJpaEntity.getEnterDate(),
                deliveryJpaEntity.getExitDate(),
                deliveryJpaEntity.getReceiverName()
        );

    }


}
