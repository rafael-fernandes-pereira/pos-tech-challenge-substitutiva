package com.github.rafaelfernandes.resident.adapter.out.persistence;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import com.github.rafaelfernandes.resident.application.port.out.ManageResidentPort;
import com.github.rafaelfernandes.resident.common.annotations.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class ResidentPersistenceAdapter implements ManageResidentPort {

    private final ResidentRepository repository;
    private final ResidentMapper mapper;

    @Override
    public Boolean existsByApartment(Integer apartment) {
        return repository.existsByApartment(apartment);
    }

    @Override
    public Resident save(Resident resident) {
        var entityToSave = mapper.toCreateEntity(resident);

        var saved = repository.save(entityToSave);

        return mapper.toDomain(saved);
    }

    @Override
    public Page<Resident> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Resident> findById(String residentId) {

        var id = UUID.fromString(residentId);

        var residentEntity = repository.findById(id);

        return residentEntity.map(mapper::toDomain);

    }

    @Override
    public Optional<Resident> findByApartment(Integer apartment) {
        var residentEntity = repository.findByApartment(apartment);

        return residentEntity.map(mapper::toDomain);
    }

    @Override
    public void delete(Resident resident) {
        var entityToDelete = mapper.toCreateEntity(resident);

        repository.delete(entityToDelete);
    }
}
