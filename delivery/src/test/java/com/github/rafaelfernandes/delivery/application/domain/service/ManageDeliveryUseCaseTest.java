package com.github.rafaelfernandes.delivery.application.domain.service;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.delivery.application.port.out.EmployeePort;
import com.github.rafaelfernandes.delivery.application.port.out.NotificationPort;
import com.github.rafaelfernandes.delivery.application.port.out.ResidentPort;
import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import com.github.rafaelfernandes.delivery.common.exception.ApartmentNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.DeliveryNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.EmployeeNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManageDeliveryUseCaseTest {

    @Mock
    private ResidentPort residentPort;

    @Mock
    private EmployeePort employeePort;

    @Mock
    private DeliveryPort deliveryPort;

    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private ManageDeliveryUseCase manageDeliveryUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_Success() {
        Integer apartment = 101;
        String employeeCellphone = "+12 34 56789-9675";
        String nameDestination = "John Doe";
        String packageDescription = "Package Description";

        var resident = mock(Resident.class);
        var employee = mock(Employee.class);

        var deliverySavedId = mock(Delivery.DeliveryId.class);

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.of(resident));
        when(employeePort.findByCellphone(employeeCellphone)).thenReturn(Optional.of(employee));
        when(deliveryPort.save(any(Delivery.class))).thenReturn(deliverySavedId);


        var deliveryId = manageDeliveryUseCase.create(apartment, employeeCellphone, nameDestination, packageDescription);

        assertEquals(deliverySavedId, deliveryId);
        verify(residentPort).getByApartment(apartment);
        verify(employeePort).findByCellphone(employeeCellphone);
        verify(deliveryPort).save(any(Delivery.class));
        verify(notificationPort).notifyPackge(any());
    }

    @Test
    public void testCreate_ApartmentNotFound() {
        Integer apartment = 101;
        String employeeCellphone = "+12 34 56789-9675";
        String nameDestination = "John Doe";
        String packageDescription = "Package Description";

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () ->
                manageDeliveryUseCase.create(apartment, employeeCellphone, nameDestination, packageDescription)
        );

        verify(residentPort).getByApartment(apartment);
        verify(employeePort, never()).findByCellphone(anyString());
        verify(deliveryPort, never()).save(any(Delivery.class));
        verify(notificationPort, never()).notifyPackge(any());
    }

    @Test
    public void testCreate_EmployeeNotFound() {
        Integer apartment = 101;
        String employeeCellphone = "+12 34 56789-9675";
        String nameDestination = "John Doe";
        String packageDescription = "Package Description";

        var resident = mock(Resident.class);

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.of(resident));
        when(employeePort.findByCellphone(employeeCellphone)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                manageDeliveryUseCase.create(apartment, employeeCellphone, nameDestination, packageDescription)
        );

        verify(residentPort).getByApartment(apartment);
        verify(employeePort).findByCellphone(employeeCellphone);
        verify(deliveryPort, never()).save(any(Delivery.class));
        verify(notificationPort, never()).notifyPackge(any());
    }

    @Test
    public void testGetAllByApartment_Success() {
        Integer apartment = 101;
        String deliveryStatus = "TO_DELIVER";

        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                apartment
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.of(resident));
        when(deliveryPort.getAllByResident(resident)).thenReturn(List.of(delivery));
        when(employeePort.findById(anyString())).thenReturn(Optional.of(employee));

        var result = manageDeliveryUseCase.getAllByApartment(apartment, deliveryStatus);

        assertEquals(1, result.size());
        verify(residentPort).getByApartment(apartment);
        verify(deliveryPort).getAllByResident(resident);
        verify(employeePort).findById(anyString());
    }

    @Test
    public void testGetAllByApartment_ApartmentNotFound() {
        Integer apartment = 101;
        String deliveryStatus = "DELIVERED";

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () ->
                manageDeliveryUseCase.getAllByApartment(apartment, deliveryStatus)
        );

        verify(residentPort).getByApartment(apartment);
        verify(deliveryPort, never()).getAllByResident(any());
        verify(employeePort, never()).findById(anyString());
    }

    @Test
    public void testGetAllByApartment_EmployeeNotFound() {
        Integer apartment = 101;
        String deliveryStatus = "TO_DELIVER";

        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                apartment
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(residentPort.getByApartment(apartment)).thenReturn(Optional.of(resident));
        when(deliveryPort.getAllByResident(resident)).thenReturn(List.of(delivery));
        when(employeePort.findById(any())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                manageDeliveryUseCase.getAllByApartment(apartment, deliveryStatus)
        );

        verify(residentPort).getByApartment(apartment);
        verify(deliveryPort).getAllByResident(resident);
        verify(employeePort).findById(anyString());
    }

    @Test
    public void testUpdateNotificationStatus_Success() {
        String deliveryId = UUID.randomUUID().toString();
        String notificationStatus = "SENT";
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(residentPort.getById(anyString())).thenReturn(Optional.of(resident));
        when(employeePort.findById(anyString())).thenReturn(Optional.of(employee));

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));

        manageDeliveryUseCase.updateNotificationStatus(deliveryId, notificationStatus);

        verify(deliveryPort).getById(deliveryId);
        verify(deliveryPort).updateNotificationStatus(deliveryId, NotificationStatus.SENT);
    }

    @Test
    public void testUpdateNotificationStatus_DeliveryNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        String notificationStatus = "SENT";

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.empty());

        assertThrows(DeliveryNotFoundException.class, () ->
                manageDeliveryUseCase.updateNotificationStatus(deliveryId, notificationStatus)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(deliveryPort, never()).updateNotificationStatus(anyString(), any(NotificationStatus.class));
    }

    @Test
    public void testDelivered_Success() {
        String deliveryId = UUID.randomUUID().toString();
        String receiverName = "Jane Doe";

        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(anyString())).thenReturn(Optional.of(mock(Resident.class)));
        when(employeePort.findById(anyString())).thenReturn(Optional.of(mock(Employee.class)));

        manageDeliveryUseCase.delivered(deliveryId, receiverName);

        verify(deliveryPort).getById(deliveryId);
        verify(deliveryPort).delivered(deliveryId, receiverName);
    }

    @Test
    public void testDelivered_DeliveryNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        String receiverName = "Jane Doe";

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.empty());

        assertThrows(DeliveryNotFoundException.class, () ->
                manageDeliveryUseCase.delivered(deliveryId, receiverName)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(deliveryPort, never()).delivered(anyString(), anyString());
    }

    @Test
    public void testDelivered_ApartmentNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        String receiverName = "Jane Doe";

        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(anyString())).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () ->
                manageDeliveryUseCase.delivered(deliveryId, receiverName)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(residentPort).getById(anyString());
        verify(deliveryPort, never()).delivered(anyString(), anyString());
    }

    @Test
    public void testDelivered_EmployeeNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        String receiverName = "Jane Doe";

        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
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

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(anyString())).thenReturn(Optional.of(mock(Resident.class)));
        when(employeePort.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                manageDeliveryUseCase.delivered(deliveryId, receiverName)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(residentPort).getById(anyString());
        verify(employeePort).findById(anyString());
        verify(deliveryPort, never()).delivered(anyString(), anyString());
    }

    @Test
    public void testGetById_Success() {
        String deliveryId = UUID.randomUUID().toString();
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675"
        );

        var delivery = Delivery.of(
                deliveryId,
                resident,
                employee,
                "John Doe",
                "Package Description",
                "TO_DELIVER",
                "TO_SEND",
                null,
                null,
                null
        );

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(resident.residentId().id())).thenReturn(Optional.of(resident));
        when(employeePort.findById(employee.employeeId().id())).thenReturn(Optional.of(employee));

        var result = manageDeliveryUseCase.getById(deliveryId);

        assertEquals(delivery.getId(), result.getId());
        verify(deliveryPort).getById(deliveryId);
        verify(residentPort).getById(resident.residentId().id());
        verify(employeePort).findById(employee.employeeId().id());
    }

    @Test
    public void testGetById_DeliveryNotFound() {
        String deliveryId = UUID.randomUUID().toString();

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.empty());

        assertThrows(DeliveryNotFoundException.class, () ->
                manageDeliveryUseCase.getById(deliveryId)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(residentPort, never()).getById(anyString());
        verify(employeePort, never()).findById(anyString());
    }

    @Test
    public void testGetById_ApartmentNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675"
        );

        var delivery = Delivery.of(
                deliveryId,
                resident,
                employee,
                "John Doe",
                "Package Description",
                "TO_DELIVER",
                "TO_SEND",
                null,
                null,
                null
        );

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(anyString())).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () ->
                manageDeliveryUseCase.getById(deliveryId)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(residentPort).getById(anyString());
        verify(employeePort, never()).findById(anyString());
    }

    @Test
    public void testGetById_EmployeeNotFound() {
        String deliveryId = UUID.randomUUID().toString();
        var resident = new Resident(
                new Resident.ResidentId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675",
                101
        );

        var employee = new Employee(
                new Employee.EmployeeId(UUID.randomUUID().toString()),
                "John Doe",
                "219.436.850-70",
                "+12 34 56789-9675"
        );

        var delivery = Delivery.of(
                deliveryId,
                resident,
                employee,
                "John Doe",
                "Package Description",
                "TO_DELIVER",
                "TO_SEND",
                null,
                null,
                null
        );

        when(deliveryPort.getById(deliveryId)).thenReturn(Optional.of(delivery));
        when(residentPort.getById(anyString())).thenReturn(Optional.of(resident));
        when(employeePort.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () ->
                manageDeliveryUseCase.getById(deliveryId)
        );

        verify(deliveryPort).getById(deliveryId);
        verify(residentPort).getById(anyString());
        verify(employeePort).findById(anyString());
    }
}