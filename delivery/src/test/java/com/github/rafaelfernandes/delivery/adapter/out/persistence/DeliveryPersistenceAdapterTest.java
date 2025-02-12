package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.adapter.out.persistence.entity.DeliveryJpaEntity;
import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DeliveryPersistenceAdapterTest {

    @Mock
    private DeliveryRepositpory deliveryRepositpory;

    @InjectMocks
    private DeliveryPersistenceAdapter deliveryPersistenceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
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

        var savedDeliveryJpaEntity = DeliveryMapper.toSavedEntity(delivery);

        when(deliveryRepositpory.save(any(DeliveryJpaEntity.class))).thenReturn(savedDeliveryJpaEntity);

        var result = deliveryPersistenceAdapter.save(delivery);

        assertEquals(savedDeliveryJpaEntity.getId().toString(), result.id());
        verify(deliveryRepositpory).save(any(DeliveryJpaEntity.class));
    }

    @Test
    void testGetAllByResident() {
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

        var delivery1 = Delivery.of(
                UUID.randomUUID().toString(),
                resident,
                employee,
                "John Doe",
                "Package Description 1",
                DeliveryStatus.TO_DELIVER.name(),
                NotificationStatus.TO_SEND.name(),
                LocalDateTime.now(),
                null,
                null
        );

        var delivery2 = Delivery.of(
                UUID.randomUUID().toString(),
                resident,
                employee,
                "Jane Doe",
                "Package Description 2",
                DeliveryStatus.DELIVERED.name(),
                NotificationStatus.SENT.name(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "John Doe"
        );

        var deliveryJpaEntity1 = DeliveryMapper.toSavedEntity(delivery1);
        var deliveryJpaEntity2 = DeliveryMapper.toSavedEntity(delivery2);

        when(deliveryRepositpory.findByResidentId(resident.residentId().id())).thenReturn(List.of(deliveryJpaEntity1, deliveryJpaEntity2));

        var result = deliveryPersistenceAdapter.getAllByResident(resident);

        assertEquals(2, result.size());
        assertEquals(delivery1.getId().id(), result.get(0).getId().id());
        assertEquals(delivery2.getId().id(), result.get(1).getId().id());
        verify(deliveryRepositpory).findByResidentId(resident.residentId().id());
    }

    @Test
    void testGetById() {
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

        when(deliveryRepositpory.findById(deliveryJpaEntity.getId())).thenReturn(Optional.of(deliveryJpaEntity));

        var result = deliveryPersistenceAdapter.getById(deliveryJpaEntity.getId().toString());

        assertTrue(result.isPresent());
        assertEquals(deliveryJpaEntity.getId().toString(), result.get().getId().id());
        verify(deliveryRepositpory).findById(deliveryJpaEntity.getId());
    }

    @Test
    void testUpdateNotificationStatus() {
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

        var updatedNotificationStatus = NotificationStatus.SENT;

        when(deliveryRepositpory.findById(deliveryJpaEntity.getId())).thenReturn(Optional.of(deliveryJpaEntity));

        deliveryPersistenceAdapter.updateNotificationStatus(deliveryJpaEntity.getId().toString(), updatedNotificationStatus);

        verify(deliveryRepositpory).save(argThat(savedEntity ->
                savedEntity.getNotificationStatus().equals(updatedNotificationStatus.name())
        ));
    }

    @Test
    void testDelivered() {
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

        var receiverName = "Jane Doe";

        when(deliveryRepositpory.findById(deliveryJpaEntity.getId())).thenReturn(Optional.of(deliveryJpaEntity));

        deliveryPersistenceAdapter.delivered(deliveryJpaEntity.getId().toString(), receiverName);

        verify(deliveryRepositpory).save(argThat(savedEntity ->
                savedEntity.getDeliveryStatus().equals(DeliveryStatus.DELIVERED.name()) &&
                        savedEntity.getReceiverName().equals(receiverName) &&
                        savedEntity.getExitDate() != null
        ));
    }
}