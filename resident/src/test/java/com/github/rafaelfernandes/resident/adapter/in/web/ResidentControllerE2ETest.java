package com.github.rafaelfernandes.resident.adapter.in.web;

import com.github.rafaelfernandes.resident.adapter.in.web.request.ResidentRequest;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResidentIdResponse;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResidentResponse;
import com.github.rafaelfernandes.resident.adapter.out.persistence.ResidentJpaEntity;
import com.github.rafaelfernandes.resident.adapter.out.persistence.ResidentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ResidentControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ResidentRepository residentRepository;

    @Test
    void testCreateAndRetrieveResident() {
        // Criação de um novo residente
        ResidentRequest resident = new ResidentRequest(
                "Jane Doe",
                "261.754.700-01",
                "+55 11 95431-9876",
                202
            );

        // Envio da requisição POST para criar um residente
        ResponseEntity<ResidentIdResponse> response = restTemplate.postForEntity("/api/resident", resident, ResidentIdResponse.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        ResidentIdResponse createdResident = response.getBody();
        assert createdResident != null;
        assertThat(createdResident.resident_id()).isNotNull();

        // Envio da requisição GET para recuperar os residentes
        ResponseEntity<CustomPageImpl<ResidentResponse>> responseList = restTemplate.exchange(
                "/api/resident",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomPageImpl<ResidentResponse>>() {
        });

        assertThat(responseList.getStatusCodeValue()).isEqualTo(200);
        var residents = responseList.getBody().getContent();
        assert residents != null;
        assertThat(residents.size()).isGreaterThan(0);
        assertThat(residents.get(0).name()).isEqualTo("Jane Doe");
    }
}
