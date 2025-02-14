package com.github.rafaelfernandes.resident.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

public record ResponseError(
        @Schema(example = "Not Found")
        String message,

        @Schema(implementation = HttpStatusCode.class, example = "404")
        Integer status
) {
}
