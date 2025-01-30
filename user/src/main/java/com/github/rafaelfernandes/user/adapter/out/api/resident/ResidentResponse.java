package com.github.rafaelfernandes.user.adapter.out.api.resident;

import lombok.Data;

@Data
public class ResidentResponse {
        private final String id;

        private final String name;

        private final String document;

        private final String cellphone;

        private final Integer apartment;
}
