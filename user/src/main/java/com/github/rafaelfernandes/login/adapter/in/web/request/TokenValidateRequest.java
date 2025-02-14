package com.github.rafaelfernandes.login.adapter.in.web.request;

import com.github.rafaelfernandes.common.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

public record TokenValidateRequest(
        @Schema(example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9")
        String token,

        @Schema(implementation = UserType.class, example = "EMPLOYEE")
        String role
) {
}
