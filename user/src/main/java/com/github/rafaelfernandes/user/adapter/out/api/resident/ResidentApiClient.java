package com.github.rafaelfernandes.user.adapter.out.api.resident;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "resident", url = "${resident.url}", dismiss404 = true)
public interface ResidentApiClient {

    @GetMapping("/cellphone/{cellphone}")
    List<ResidentResponse> findByCellphone(@PathVariable String cellphone);

    @GetMapping("/apartment/{apartment}")
    Optional<ResidentResponse> findByApartment(@PathVariable Integer apartment);

    @GetMapping("/{id}")
    ResidentResponse findById(@PathVariable String id);

    @PostMapping("")
    ResidentIdResponse create(@RequestBody ResidentRequest residentRequest);

}
