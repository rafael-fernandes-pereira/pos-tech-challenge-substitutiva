package com.github.rafaelfernandes.user.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequest(
        @Schema(minimum = "3", maximum = "100")
        String name,

        @Schema(minimum = "11", maximum = "14")
        String document,

        @Schema(minimum = "17", maximum = "17")
        String cellphone,

        @Schema(implementation = Integer.class)
        Integer apartment

) {
}
