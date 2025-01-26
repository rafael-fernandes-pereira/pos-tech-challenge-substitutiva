package com.github.rafaelfernandes.login.adapter.in.web;

import com.github.rafaelfernandes.login.adapter.in.web.request.LoginRequest;
import com.github.rafaelfernandes.login.adapter.in.web.response.LoginTokenResponse;
import com.github.rafaelfernandes.user.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
@Tag(name = "Login", description = "Login Endpoint")
public class LoginController {

    @Operation(summary = "Authenticate a Resident")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginTokenResponse.class)
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            )),
            @ApiResponse(description = "Authenticate error", responseCode = "401", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Authenticate error\",\"status\":401}")
            ))
    })
    @PostMapping(
            path = "/resident",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(new LoginTokenResponse("token"));
    }


}
