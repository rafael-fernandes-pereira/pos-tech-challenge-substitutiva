package com.github.rafaelfernandes.login.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;


public record LoginRequest(
        @Schema(minimum = "17", maximum = "17", example = "+55 11 99999-9999")
        String cellphone,

        @Schema(minimum = "6", maximum = "10", example = "aA@123456")
        String password
) {
}
