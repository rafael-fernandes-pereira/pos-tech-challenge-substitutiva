package com.github.rafaelfernandes.resident.adapter.out.persistence;

import com.github.rafaelfernandes.resident.adapter.out.persistence.entity.ResidentJpaEntity;
import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResidentMapper {

    ResidentJpaEntity toCreateEntity(Resident resident){
        return ResidentJpaEntity.builder()
                .name(resident.getName())
                .document(resident.getDocument())
                .cellphone(resident.getCellphone())
                .apartment(resident.getApartment())
                .build();
    }

    ResidentJpaEntity toUpdateEntity(Resident.ResidentId id, Resident resident){
        return ResidentJpaEntity.builder()
                .id(UUID.fromString(id.id()))
                .name(resident.getName())
                .document(resident.getDocument())
                .cellphone(resident.getCellphone())
                .apartment(resident.getApartment())
                .build();
    }

    Resident toDomain(ResidentJpaEntity residentJpaEntity){

        return Resident.of(
                residentJpaEntity.getId().toString(),
                residentJpaEntity.getName(),
                residentJpaEntity.getDocument(),
                residentJpaEntity.getCellphone(),
                residentJpaEntity.getApartment()
        );
    }

}
