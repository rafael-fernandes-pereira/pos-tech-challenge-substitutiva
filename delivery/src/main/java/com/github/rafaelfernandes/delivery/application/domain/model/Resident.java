package com.github.rafaelfernandes.delivery.application.domain.model;

public record Resident(
        ResidentId residentId,
        String name,
        String document,
        String cellphone,
        Integer apartment
) {

    public record ResidentId(
            String id
    ) {

    }
}
