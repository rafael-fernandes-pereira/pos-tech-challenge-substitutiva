package com.github.rafaelfernandes.user.adapter.out.api.resident;

import com.github.rafaelfernandes.user.adapter.out.api.resident.request.ResidentRequest;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.port.out.ResidentPort;
import com.github.rafaelfernandes.common.annotations.ApiClientAdapter;
import lombok.RequiredArgsConstructor;

@ApiClientAdapter
@RequiredArgsConstructor
public class ResidentCallAPIAdapter implements ResidentPort {

    private final ResidentApiClient residentApiClient;

    @Override
    public Boolean exitsByCellphone(String cellphone) {
        return !residentApiClient.findByCellphone(cellphone).isEmpty();
    }

    @Override
    public Boolean exitsByApartment(Integer apartment) {
        return residentApiClient.findByApartment(apartment).isPresent();
    }


    @Override
    public String save(Resident resident) {

        var saved = residentApiClient.create(new ResidentRequest(
                resident.getName(),
                resident.getDocument(),
                resident.getCellphone(),
                resident.getApartment()
        ));

        return saved.resident_id().toString();

    }

    @Override
    public Resident getById(String id) {

        var residentResponse = residentApiClient.findById(id);

        return Resident.of(
                residentResponse.getId(),
                residentResponse.getName(),
                residentResponse.getDocument(),
                residentResponse.getCellphone(),
                residentResponse.getApartment()
        );
    }
}
