package com.github.rafaelfernandes.delivery.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeliveredRequest(
        @Schema(minimum = "3", maximum = "100", example = "Rafael Fernandes")
        String receiverName
) {
}
