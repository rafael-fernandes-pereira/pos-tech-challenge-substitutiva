package com.github.rafaelfernandes.delivery.adapter.in.web.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record DeliveryDataResponse(
        @Schema(example = "Rafa Fernandes")
        String destinationName,

        @Schema(example = "Entrega de pacote com formato redondo")
        String packageDescription,

        @Schema(example = "Rafa Fernandes")
        String residentName,

        @Schema(minimum = "17", maximum = "17", example = "+99 99 99999-9999")
        String residentCellphone
){
}
