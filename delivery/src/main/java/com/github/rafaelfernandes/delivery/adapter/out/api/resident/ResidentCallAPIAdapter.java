package com.github.rafaelfernandes.delivery.adapter.out.api.resident;


import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.application.port.out.ResidentPort;
import com.github.rafaelfernandes.delivery.common.annotations.ApiClientAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApiClientAdapter
@RequiredArgsConstructor
public class ResidentCallAPIAdapter implements ResidentPort {

    private final ResidentApiClient residentApiClient;

    @Override
    public Optional<Resident> getByApartment(Integer apartment) {

        var resident = residentApiClient.findByApartment(apartment);

        return resident.map(residentResponse -> new Resident(
                new Resident.ResidentId(residentResponse.getId()),
                residentResponse.getName(),
                residentResponse.getDocument(),
                residentResponse.getCellphone(),
                residentResponse.getApartment()
        ));
    }
}
