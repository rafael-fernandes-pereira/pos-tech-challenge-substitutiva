package com.github.rafaelfernandes.gateway.service;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ValidateTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${login.api}")
    private String loginApi;

    public boolean validate(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);

        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                loginApi + "/api/validacao",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getStatusCode().is2xxSuccessful();
    }

}
