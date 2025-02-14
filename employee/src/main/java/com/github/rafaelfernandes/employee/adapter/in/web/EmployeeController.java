package com.github.rafaelfernandes.employee.adapter.in.web;

import com.github.rafaelfernandes.employee.adapter.in.web.request.EmployeeRequest;
import com.github.rafaelfernandes.employee.adapter.in.web.response.EmployeeIdResponse;
import com.github.rafaelfernandes.employee.adapter.in.web.response.EmployeeResponse;
import com.github.rafaelfernandes.employee.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import com.github.rafaelfernandes.employee.application.port.in.EmployeeUseCase;
import com.github.rafaelfernandes.employee.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@WebAdapter
@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
@Tag(name = "Employee", description = "Employee Endpoint")
public class EmployeeController {

    private final EmployeeUseCase useCase;

    @Operation(summary = "Create a Employee")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeIdResponse.class)
            )),
            @ApiResponse(description = "Employee with cellphone has already been exists", responseCode = "409", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Employee with cellphone +99 99 99999-9999 already exists\",\"status\":409}")
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
    ResponseEntity<EmployeeIdResponse> create(@Parameter @RequestBody EmployeeRequest request){
        var employeeToModel = new Employee(
                request.name(),
                request.document(),
                request.cellphone()
        );

        var newEmployee = useCase.create(employeeToModel);

        var response = new EmployeeIdResponse(UUID.fromString(newEmployee.id()));

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @Operation(summary = "Show all Employees")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Page.class)
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
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<EmployeeResponse>> getAll(
            @Parameter(description = "Pageable data", required = true, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) {

        var employees = useCase.getAll(pageable);

        var employeesResponse = employees.map(EmployeeResponse::fromDomain).stream().toList();

        Page<EmployeeResponse> page = new PageImpl<>(employeesResponse, pageable, employees.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Operation(summary = "Find Employee by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeResponse.class)
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            )),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class)
            )),
            @ApiResponse(description = "Authenticate error", responseCode = "401", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Authenticate error\",\"status\":401}")
            ))
    })
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<EmployeeResponse> getById(
            @Parameter(description = "Employee id", required = true, schema = @Schema(implementation = UUID.class), example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id
    ) {

        var employee = useCase.findById(id);

        var response = EmployeeResponse.fromDomain(employee);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Operation(summary = "Delete Employee by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            )),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class)
            )),
            @ApiResponse(description = "Authenticate error", responseCode = "401", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Authenticate error\",\"status\":401}")
            ))
    })
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> deleteById(
            @Parameter(description = "Employee id", required = true, schema = @Schema(implementation = UUID.class), example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id
    ) {
        useCase.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update Employee by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            )),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class)
            )),
            @ApiResponse(description = "Authenticate error", responseCode = "401", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Authenticate error\",\"status\":401}")
            ))
    })
    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> updateById(
            @Parameter(description = "Employee id", required = true, schema = @Schema(implementation = UUID.class), example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @RequestBody EmployeeRequest request) {

        var employee = useCase.findById(id);

        var employeeModel = new Employee(
                request.name(),
                request.document(),
                request.cellphone()
        );

        useCase.update(employee.getEmployeeId(), employeeModel);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @Operation(summary = "Find Employee by Cellphone")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeResponse.class)
            )),
            @ApiResponse(description = "Business and Internal problems", responseCode = "500", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Business and Internal problems\",\"status\":500}")
            )),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class)
            )),
            @ApiResponse(description = "Authenticate error", responseCode = "401", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseError.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"message\":\"Authenticate error\",\"status\":401}")
            ))
    })
    @GetMapping(
            path = "/cellphone/{cellphone}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<EmployeeResponse> getByCellphone(
            @Parameter(description = "Employee cellphone", required = true, schema = @Schema(implementation = String.class), example = "+99 99 99999-9999")
            @PathVariable String cellphone) {

        var employee = useCase.findByCellphone(cellphone);

        var response = EmployeeResponse.fromDomain(employee);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
