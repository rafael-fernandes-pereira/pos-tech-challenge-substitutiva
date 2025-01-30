package com.github.rafaelfernandes.employee.adapter.out.persistence;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import com.github.rafaelfernandes.employee.application.port.out.ManageEmployeePort;
import com.github.rafaelfernandes.employee.common.annotations.PersistenceAdapter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@AllArgsConstructor
public class EmployeePersistenceAdapter implements ManageEmployeePort {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) {
        var entityToSave = EmployeeMapper.toJpaEntity(employee);

        var saved = employeeRepository.save(entityToSave);

        return EmployeeMapper.toDomain(saved);
    }

    @Override
    public Page<Employee> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper::toDomain);
    }

    @Override
    public Optional<Employee> findById(String residentId) {
        var id = UUID.fromString(residentId);

        var residentEntity = employeeRepository.findById(id);

        return residentEntity.map(EmployeeMapper::toDomain);
    }

    @Override
    public void delete(Employee employee) {
        var entityToDelete = EmployeeMapper.toJpaEntity(employee);

        employeeRepository.delete(entityToDelete);
    }

    @Override
    public void update(Employee.EmployeeId id, Employee employee) {
        var entityToUpdate = EmployeeMapper.toJpaEntity(employee);

        employeeRepository.save(entityToUpdate);
    }

    @Override
    public Optional<Employee> findByCellphone(String cellphone) {
        return employeeRepository.findByCellphone(cellphone).map(EmployeeMapper::toDomain);
    }
}
