package com.github.rafaelfernandes.login.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;


public record LoginRequest(
        @Schema(minimum = "17", maximum = "17")
        String cellphone,

        @Schema(minimum = "6", maximum = "10")
        String password
) {
}
