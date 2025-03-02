package com.github.rafaelfernandes.delivery.adapter.out.api.employee;


import com.github.rafaelfernandes.delivery.adapter.out.api.employee.response.EmployeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "employee", url = "${employee.url}", dismiss404 = true)
public interface EmployeeApiClient {

    @GetMapping("/cellphone/{cellphone}")
    Optional<EmployeeResponse> findByCellphone(@PathVariable String cellphone);

    @GetMapping("/{id}")
    Optional<EmployeeResponse> findById(@PathVariable String id);

}
