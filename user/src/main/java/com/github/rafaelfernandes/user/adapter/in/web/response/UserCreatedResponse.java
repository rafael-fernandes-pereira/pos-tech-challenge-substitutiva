package com.github.rafaelfernandes.user.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserCreatedResponse(
        @Schema(minimum = "17", maximum = "17")
        String cellphone,

        @Schema(implementation = String.class, example = "aA@123456")
        String password
) {
}
