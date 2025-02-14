package com.github.rafaelfernandes.login.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginTokenResponse(
        @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.7")
        String token
) {
}
