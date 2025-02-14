package com.github.rafaelfernandes.user.adapter.in.web;

import com.github.rafaelfernandes.user.adapter.in.web.request.EmployeeRequest;
import com.github.rafaelfernandes.user.adapter.in.web.request.ResidentRequest;
import com.github.rafaelfernandes.user.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.user.adapter.in.web.response.UserCreatedResponse;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.port.in.UserUseCase;
import com.github.rafaelfernandes.common.annotations.WebAdapter;
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
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class)
            )),
            @ApiResponse(description = "User with cellphone has already been exists", responseCode = "409", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"User with cellphone +99 99 99999-9999 already exists\",\"status\":409}")
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

    public UserCreatedResponse create(@Parameter @RequestBody ResidentRequest residentRequest) {

        var resident = new Resident(
                residentRequest.name(),
                residentRequest.document(),
                residentRequest.cellphone(),
                residentRequest.apartment()
        );

        var createdUserPassword = userUseCase.createResident(resident);

        return new UserCreatedResponse(
                resident.getCellphone(),
                createdUserPassword
        );


    }

    @Operation(summary = "Create a Employee")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserCreatedResponse.class)
            )),
            @ApiResponse(description = "User with cellphone has already been exists", responseCode = "409", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"User with cellphone +99 99 99999-9999 already exists\",\"status\":409}")
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
            path = "/employee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public UserCreatedResponse create(@Parameter @RequestBody EmployeeRequest employeeRequest) {

        var resident = new Employee(
                employeeRequest.name(),
                employeeRequest.document(),
                employeeRequest.cellphone()
        );

        var createdUserPassword = userUseCase.createEmployee(resident);

        return new UserCreatedResponse(
                resident.getCellphone(),
                createdUserPassword
        );


    }

}
