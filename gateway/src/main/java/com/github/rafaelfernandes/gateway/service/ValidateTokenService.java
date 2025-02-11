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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ValidateTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${login.api}")
    private String loginApi;

    public boolean validate(String token, List<String> roles) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        for (String role: roles) {

            var request = new TokenValidateRequest(token, role);

            HttpEntity<TokenValidateRequest> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    loginApi + "/validate",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if(response.getStatusCode().is2xxSuccessful()) return true;

        }

        return false;


    }

}
