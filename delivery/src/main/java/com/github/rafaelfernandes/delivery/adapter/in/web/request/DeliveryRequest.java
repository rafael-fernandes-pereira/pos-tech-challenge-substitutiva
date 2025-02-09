package com.github.rafaelfernandes.delivery.adapter.in.web.request;

public record DeliveryRequest(
        Integer apartment,
        String employeeCellphone,
        String nameDestination,
        String packageDescription
) {
}
