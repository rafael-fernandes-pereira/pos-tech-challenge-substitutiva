package com.github.rafaelfernandes.delivery.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeliveryRequest(

        @Schema(example = "101")
        Integer apartment,

        @Schema(minimum = "17", maximum = "17", example = "+99 99 99999-9999")
        String employeeCellphone,

        @Schema(minimum = "3", maximum = "100", example = "Rafael Fernandes")
        String nameDestination,

        @Schema(minimum = "3", maximum = "100", example = "Entrega de pacote com formato redondo")
        String packageDescription
) {
}
