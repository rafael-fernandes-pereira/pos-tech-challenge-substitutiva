package com.github.rafaelfernandes.delivery.adapter.in.web;

import com.github.rafaelfernandes.delivery.adapter.in.web.request.DeliveredRequest;
import com.github.rafaelfernandes.delivery.adapter.in.web.request.DeliveryRequest;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryDataResponse;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryIdResponse;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryResponse;
import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.application.port.in.DeliveryUseCase;
import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryControllerTest {

    @Mock
    private DeliveryUseCase deliveryUseCase;

    @InjectMocks
    private DeliveryController deliveryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDelivery() {
        var request = new DeliveryRequest(
                101,
                "+12 34 56789-9675",
                "John Doe",
                "Package Description"
        );

        var deliveryId = new Delivery.DeliveryId("123e4567-e89b-12d3-a456-426614174000");

        var deliveryIdResponse = new DeliveryIdResponse("123e4567-e89b-12d3-a456-426614174000");

        when(deliveryUseCase.create(
                request.apartment(),
                request.employeeCellphone(),
                request.nameDestination(),
                request.packageDescription()
        )).thenReturn(deliveryId);

        ResponseEntity<DeliveryIdResponse> response = deliveryController.registerDelivery(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deliveryIdResponse, response.getBody());
        verify(deliveryUseCase).create(
                request.apartment(),
                request.employeeCellphone(),
                request.nameDestination(),
                request.packageDescription()
        );
    }

    @Test
    void testGetAllByApartment() {
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

        when(deliveryUseCase.getAllByApartment(101, DeliveryStatus.TO_DELIVER.name())).thenReturn(List.of(delivery1, delivery2));

        ResponseEntity<List<DeliveryResponse>> response = deliveryController.getAllByApartment(101, DeliveryStatus.TO_DELIVER.name());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(delivery1.getId().id(), response.getBody().get(0).id());
        assertEquals(delivery2.getId().id(), response.getBody().get(1).id());
        verify(deliveryUseCase).getAllByApartment(101, DeliveryStatus.TO_DELIVER.name());
    }

    @Test
    void testReadNotification() {
        String deliveryId = "123e4567-e89b-12d3-a456-426614174000";

        ResponseEntity<Void> response = deliveryController.readNotification(deliveryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deliveryUseCase).updateNotificationStatus(deliveryId, NotificationStatus.READ.name());
    }


    @Test
    void testSentNotification() {
        String deliveryId = "123e4567-e89b-12d3-a456-426614174000";

        ResponseEntity<Void> response = deliveryController.sentNotification(deliveryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deliveryUseCase).updateNotificationStatus(deliveryId, NotificationStatus.SENT.name());
    }

    @Test
    void testDelivered() {
        String deliveryId = "123e4567-e89b-12d3-a456-426614174000";
        DeliveredRequest request = new DeliveredRequest("Jane Doe");

        ResponseEntity<Void> response = deliveryController.delivered(deliveryId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deliveryUseCase).delivered(deliveryId, request.receiverName());
    }

    @Test
    void testGetById() {
        String deliveryId = "123e4567-e89b-12d3-a456-426614174000";

        var resident = new Resident(
                new Resident.ResidentId("resident-id"),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var delivery = Delivery.of(
                deliveryId,
                resident,
                null,
                "John Doe",
                "Package Description",
                "TO_DELIVER",
                "TO_SEND",
                null,
                null,
                null
        );

        when(deliveryUseCase.getById(deliveryId)).thenReturn(delivery);

        ResponseEntity<DeliveryDataResponse> response = deliveryController.getById(deliveryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().destinationName());
        assertEquals("Package Description", response.getBody().packageDescription());
        assertEquals("John Doe", response.getBody().residentName());
        assertEquals("+12 34 56789-9675", response.getBody().residentCellphone());
        verify(deliveryUseCase).getById(deliveryId);
    }
}