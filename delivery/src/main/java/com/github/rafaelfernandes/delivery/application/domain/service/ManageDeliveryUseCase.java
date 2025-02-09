package com.github.rafaelfernandes.delivery.application.domain.service;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.port.in.DeliveryUseCase;
import com.github.rafaelfernandes.delivery.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.delivery.application.port.out.EmployeePort;
import com.github.rafaelfernandes.delivery.application.port.out.NotificationPort;
import com.github.rafaelfernandes.delivery.application.port.out.ResidentPort;
import com.github.rafaelfernandes.delivery.common.annotations.UseCase;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import com.github.rafaelfernandes.delivery.common.exception.ApartmentNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.EmployeeNotFoundException;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class ManageDeliveryUseCase implements DeliveryUseCase {

    private final ResidentPort residentPort;
    private final EmployeePort employeePort;
    private final DeliveryPort deliveryPort;


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

        return deliverySaved;



    }
}
