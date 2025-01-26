package com.github.rafaelfernandes.user.adapter.in;

import com.github.rafaelfernandes.user.adapter.in.request.UserRequest;
import com.github.rafaelfernandes.user.adapter.in.response.ResponseError;
import com.github.rafaelfernandes.user.adapter.in.response.UserCreatedResponse;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.in.UserUseCase;
import com.github.rafaelfernandes.user.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "User", description = "User Endpoint")
public class UserController {

    private final UserUseCase userUseCase;

    @Operation(summary = "Create a Resident")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserCreatedResponse.class)
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

    public UserCreatedResponse create(@Parameter @RequestBody UserRequest userRequest) {

        var resident = new Resident(
                userRequest.name(),
                userRequest.document(),
                userRequest.cellphone(),
                userRequest.apartment()
        );

        var createdUserPassword = userUseCase.createResident(resident);

        return new UserCreatedResponse(
                resident.getCellphone(),
                createdUserPassword
        );


    }

}
