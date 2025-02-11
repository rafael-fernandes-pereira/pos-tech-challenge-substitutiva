package com.github.rafaelfernandes.notification.adapter.out.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

@FeignClient(name = "delivery", url = "${delivery.url}", dismiss404 = true)
public interface DeliveryApiClient {

    @GetMapping(path = "/{id}", produces = "application/json")
    Optional<DeliveryDataResponse> findById(@PathVariable String id);

    @GetMapping("/{id}")
    String findByIdd(@PathVariable String id);

    @PutMapping("/{id}/notification/sent")
    void sentNotification(@PathVariable String id);

}
