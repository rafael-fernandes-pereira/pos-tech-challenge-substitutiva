package com.github.rafaelfernandes.login.adapter.in.web;

import com.github.rafaelfernandes.login.adapter.in.web.request.LoginRequest;
import com.github.rafaelfernandes.login.adapter.in.web.request.TokenValidateRequest;
import com.github.rafaelfernandes.login.adapter.in.web.response.LoginTokenResponse;
import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.port.in.AuthenticateUseCase;
import com.github.rafaelfernandes.login.application.port.in.TokenUseCase;
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

    private final AuthenticateUseCase authenticateUseCase;
    private final TokenUseCase tokenUseCase;

    @Operation(summary = "Authenticate")
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
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest loginRequest) {

        var login = new Login(loginRequest.cellphone(), loginRequest.password());

        var authenticate = authenticateUseCase.authenticate(login);

        return ResponseEntity.ok(new LoginTokenResponse(authenticate));
    }

    @Operation(summary = "Validate token")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Boolean.class)
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            ))
    })
    @PostMapping(
            path = "/validate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Boolean> validateToken(@RequestBody TokenValidateRequest request) {

        authenticateUseCase.isInvalid(request.token());

        tokenUseCase.roleIsValid(request.token(), request.role());

        return ResponseEntity.ok(true);
    }


}
