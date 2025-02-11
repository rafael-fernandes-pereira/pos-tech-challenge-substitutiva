package com.github.rafaelfernandes.delivery.adapter.in.web.response;


public record DeliveryDataResponse(
        String destinationName,
        String packageDescription,
        String residentName,
        String residentCellphone
){
}
