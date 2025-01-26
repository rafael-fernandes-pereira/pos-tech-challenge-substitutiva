package com.github.rafaelfernandes.user.adapter.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserCreatedResponse(
        @Schema(minimum = "17", maximum = "17")
        String cellphone,

        String password
) {
}
