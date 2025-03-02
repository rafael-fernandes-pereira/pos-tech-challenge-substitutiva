package com.github.rafaelfernandes.delivery.application.domain.service;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.port.in.DeliveryUseCase;
import com.github.rafaelfernandes.delivery.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.delivery.application.port.out.EmployeePort;
import com.github.rafaelfernandes.delivery.application.port.out.NotificationPort;
import com.github.rafaelfernandes.delivery.application.port.out.ResidentPort;
import com.github.rafaelfernandes.delivery.common.annotations.UseCase;
import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import com.github.rafaelfernandes.delivery.common.exception.ApartmentNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.DeliveryNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

@UseCase
@AllArgsConstructor
public class ManageDeliveryUseCase implements DeliveryUseCase {

    private final ResidentPort residentPort;
    private final EmployeePort employeePort;
    private final DeliveryPort deliveryPort;
    private final NotificationPort notificationPort;


    @Override
    public Delivery.DeliveryId create(Integer apartment, String employeeCellphone, String nameDestination, String packageDescription) {

        var resident = residentPort.getByApartment(apartment);

        if (resident.isEmpty()) throw new ApartmentNotFoundException();

        var employee = employeePort.findByCellphone(employeeCellphone);

        if (employee.isEmpty()) throw new EmployeeNotFoundException();

        var delivery = new Delivery(
                resident.get(),
                employee.get(),
                nameDestination,
                packageDescription
        );

        var deliverySaved =  deliveryPort.save(delivery);

        notificationPort.notifyPackge(deliverySaved.id());

        return deliverySaved;


    }

    @Override
    public List<Delivery> getAllByApartment(Integer apartment, String deliveryStatus) {

        var resident = residentPort.getByApartment(apartment);

        if (resident.isEmpty()) throw new ApartmentNotFoundException();

        var deliveries = deliveryPort.getAllByResident(resident.get());

        if (Strings.isNotEmpty(deliveryStatus)) {

            var deliveryStatusEnum = DeliveryStatus.valueOf(deliveryStatus);

            deliveries = deliveries.stream()
                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatusEnum.name()))
                    .toList();

        }

        var deliveriesComplete = new ArrayList<Delivery>();

        for (Delivery delivery : deliveries) {

            var employee = employeePort.findById(delivery.getEmployee().employeeId().id());

            if (employee.isEmpty()) throw new EmployeeNotFoundException();

            var delivertComplete = Delivery.of(
                    delivery.getId().id(),
                    resident.get(),
                    employee.get(),
                    delivery.getDestinationName(),
                    delivery.getPackageDescription(),
                    delivery.getDeliveryStatus(),
                    delivery.getNotificationStatus(),
                    delivery.getEnterDate(),
                    delivery.getExitDate(),
                    delivery.getReceiverName()
            );

            deliveriesComplete.add(delivertComplete);

        }

        return deliveriesComplete;

    }

    @Override
    public void updateNotificationStatus(String deliveryId, String notificationStatus) {

        this.getById(deliveryId);

        var notificationStatusEnum = NotificationStatus.valueOf(notificationStatus);

        deliveryPort.updateNotificationStatus(deliveryId, notificationStatusEnum);
    }

    @Override
    public void delivered(String deliveryId, String receiverName) {

        this.getById(deliveryId);

        deliveryPort.delivered(deliveryId, receiverName);

    }

    @Override
    public Delivery getById(String deliveryId) {
        var delivery = deliveryPort.getById(deliveryId);

        if (delivery.isEmpty()) throw new DeliveryNotFoundException();

        var resident = residentPort.getById(delivery.get().getResident().residentId().id());

        if (resident.isEmpty()) throw new ApartmentNotFoundException();

        var employee = employeePort.findById(delivery.get().getEmployee().employeeId().id());

        if (employee.isEmpty()) throw new EmployeeNotFoundException();

        return Delivery.of(
                deliveryId,
                resident.get(),
                employee.get(),
                delivery.get().getDestinationName(),
                delivery.get().getPackageDescription(),
                delivery.get().getDeliveryStatus(),
                delivery.get().getNotificationStatus(),
                delivery.get().getEnterDate(),
                delivery.get().getExitDate(),
                delivery.get().getReceiverName()
        );
    }
}
