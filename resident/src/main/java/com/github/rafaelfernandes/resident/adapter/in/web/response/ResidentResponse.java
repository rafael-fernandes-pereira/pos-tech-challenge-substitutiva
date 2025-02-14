package com.github.rafaelfernandes.resident.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ResidentResponse(

        @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(minimum = "3", maximum = "100", example = "Rafael Fernandes")
        String name,

        @Schema(minimum = "11", maximum = "14", example = "123.456.789-00")
        String document,

        @Schema(minimum = "17", maximum = "17", example = "+55 11 99999-9999")
        String cellphone,

        @Schema(implementation = Integer.class, minimum = "1", maximum = "999", example = "101")
        Integer apartment
) {

    public static ResidentResponse fromDomain(com.github.rafaelfernandes.resident.application.domain.model.Resident resident) {
        return new ResidentResponse(
                resident.getResidentId().id(),
                resident.getName(),
                resident.getDocument(),
                resident.getCellphone(),
                resident.getApartment()
        );
    }
}
