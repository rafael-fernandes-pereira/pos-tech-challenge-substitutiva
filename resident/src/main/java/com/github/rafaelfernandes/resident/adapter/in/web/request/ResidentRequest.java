package com.github.rafaelfernandes.resident.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResidentRequest(
        @Schema(minimum = "3", maximum = "100", example = "Rafael Fernandes")
        String name,

        @Schema(minimum = "11", maximum = "14", example = "123.456.789-00")
        String document,

        @Schema(minimum = "17", maximum = "17", example = "+55 11 99999-9999")
        String cellphone,

        @Schema(implementation = Integer.class, minimum = "1", maximum = "999", example = "101")
        Integer apartment


) {
}
