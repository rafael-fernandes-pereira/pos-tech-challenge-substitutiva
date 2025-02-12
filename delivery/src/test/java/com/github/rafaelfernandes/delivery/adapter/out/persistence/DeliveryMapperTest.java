package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.adapter.out.persistence.entity.DeliveryJpaEntity;
import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryMapperTest {

    @Test
    void testToEntity() {
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "Jane Doe",
                "219.436.850-70",
                "+12 34 56789-9675"
        );

        var delivery = Delivery.of(
                UUID.randomUUID().toString(),
                resident,
                employee,
                "John Doe",
                "Package Description",
                DeliveryStatus.TO_DELIVER.name(),
                NotificationStatus.TO_SEND.name(),
                LocalDateTime.now(),
                null,
                null
        );

        DeliveryJpaEntity deliveryJpaEntity = DeliveryMapper.toEntity(delivery);

        assertEquals(delivery.getResident().residentId().id(), deliveryJpaEntity.getResidentId());
        assertEquals(delivery.getEmployee().employeeId().id(), deliveryJpaEntity.getEmployeeId());
        assertEquals(delivery.getDeliveryStatus(), deliveryJpaEntity.getDeliveryStatus());
        assertEquals(delivery.getNotificationStatus(), deliveryJpaEntity.getNotificationStatus());
        assertEquals(delivery.getEnterDate(), deliveryJpaEntity.getEnterDate());
        assertEquals(delivery.getReceiverName(), deliveryJpaEntity.getReceiverName());
        assertEquals(delivery.getDestinationName(), deliveryJpaEntity.getDestinationName());
        assertEquals(delivery.getPackageDescription(), deliveryJpaEntity.getPackageDescription());
    }

    @Test
    void testToSavedEntity() {
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "Jane Doe",
                "219.436.850-70",
                "+12 34 56789-9675"
        );

        var delivery = Delivery.of(
                UUID.randomUUID().toString(),
                resident,
                employee,
                "John Doe",
                "Package Description",
                DeliveryStatus.TO_DELIVER.name(),
                NotificationStatus.TO_SEND.name(),
                LocalDateTime.now(),
                null,
                null
        );

        DeliveryJpaEntity deliveryJpaEntity = DeliveryMapper.toSavedEntity(delivery);

        assertEquals(UUID.fromString(delivery.getId().id()), deliveryJpaEntity.getId());
        assertEquals(delivery.getResident().residentId().id(), deliveryJpaEntity.getResidentId());
        assertEquals(delivery.getEmployee().employeeId().id(), deliveryJpaEntity.getEmployeeId());
        assertEquals(delivery.getDeliveryStatus(), deliveryJpaEntity.getDeliveryStatus());
        assertEquals(delivery.getNotificationStatus(), deliveryJpaEntity.getNotificationStatus());
        assertEquals(delivery.getEnterDate(), deliveryJpaEntity.getEnterDate());
        assertEquals(delivery.getReceiverName(), deliveryJpaEntity.getReceiverName());
        assertEquals(delivery.getDestinationName(), deliveryJpaEntity.getDestinationName());
        assertEquals(delivery.getPackageDescription(), deliveryJpaEntity.getPackageDescription());
    }

    @Test
    void testToDomain() {
        var deliveryJpaEntity = DeliveryJpaEntity.builder()
                .id(UUID.randomUUID())
                .residentId(UUID.randomUUID().toString())
                .employeeId(UUID.randomUUID().toString())
                .deliveryStatus(DeliveryStatus.TO_DELIVER.name())
                .notificationStatus(NotificationStatus.TO_SEND.name())
                .enterDate(LocalDateTime.now())
                .receiverName("John Doe")
                .destinationName("Jane Doe")
                .packageDescription("Package Description")
                .build();

        Delivery delivery = DeliveryMapper.toDomain(deliveryJpaEntity);

        assertEquals(deliveryJpaEntity.getId().toString(), delivery.getId().id());
        assertEquals(deliveryJpaEntity.getResidentId(), delivery.getResident().residentId().id());
        assertEquals(deliveryJpaEntity.getEmployeeId(), delivery.getEmployee().employeeId().id());
        assertEquals(deliveryJpaEntity.getDeliveryStatus(), delivery.getDeliveryStatus());
        assertEquals(deliveryJpaEntity.getNotificationStatus(), delivery.getNotificationStatus());
        assertEquals(deliveryJpaEntity.getEnterDate(), delivery.getEnterDate());
        assertEquals(deliveryJpaEntity.getReceiverName(), delivery.getReceiverName());
        assertEquals(deliveryJpaEntity.getDestinationName(), delivery.getDestinationName());
        assertEquals(deliveryJpaEntity.getPackageDescription(), delivery.getPackageDescription());
    }
}