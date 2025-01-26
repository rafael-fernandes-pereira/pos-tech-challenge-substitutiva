package com.github.rafaelfernandes.resident.adapter.in.web;

import com.github.rafaelfernandes.resident.adapter.in.web.request.ResidentRequest;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResidentIdResponse;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResidentResponse;
import com.github.rafaelfernandes.resident.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import com.github.rafaelfernandes.resident.application.port.in.ResidentUseCase;
import com.github.rafaelfernandes.resident.common.annotations.WebAdapter;
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

import java.util.List;
import java.util.UUID;

@WebAdapter
@RestController
@RequestMapping("/api/resident")
@AllArgsConstructor
@Tag(name = "Resident", description = "Resident Endpoint")
public class ResidentController {

    private final ResidentUseCase useCase;

    @Operation(summary = "Create a Resident")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResidentIdResponse.class)
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
    ResponseEntity<ResidentIdResponse> create(@Parameter @RequestBody ResidentRequest request){
        var residentModel = new Resident(
                request.name(),
                request.document(),
                request.cellphone(),
                request.apartment()
        );

        var newResident = useCase.create(residentModel);

        var response = new ResidentIdResponse(UUID.fromString(newResident.id()));

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @Operation(summary = "Show all Residents")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResidentResponse[].class)
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
    ResponseEntity<Page<ResidentResponse>> getAll(Pageable pageable) {

        var residents = useCase.getAll(pageable);

        var residentsResponse = residents.map(ResidentResponse::fromDomain).stream().toList();

        Page<ResidentResponse> page = new PageImpl<>(residentsResponse, pageable, residents.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Operation(summary = "Find Resident by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Resident.class)
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
    ResponseEntity<ResidentResponse> getById(@PathVariable String id) {

        var resident = useCase.findById(id);

        var response = ResidentResponse.fromDomain(resident);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Find Resident by Apartment")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Resident.class)
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
            path = "/apartment/{apartment}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResidentResponse> getByApartment(@Parameter @PathVariable Integer apartment) {

        var resident = useCase.findByApartment(apartment);

        var response = ResidentResponse.fromDomain(resident);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete Resident by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)
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
    ResponseEntity<String> deleteById(@PathVariable String id) {
        useCase.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Resident deleted successfully");
    }

    @Operation(summary = "Update Resident by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResidentResponse.class)
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
    ResponseEntity<String> updateById(@PathVariable String id, @RequestBody ResidentRequest request) {

        var resident = useCase.findById(id);

        var residentModel = new Resident(
                request.name(),
                request.document(),
                request.cellphone(),
                request.apartment()
        );

        useCase.update(resident.getResidentId(), residentModel);

        return ResponseEntity.status(HttpStatus.OK).body("Resident updated successfully");
    }


    @Operation(summary = "Find Resident by Cellphone")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Resident.class)
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
    ResponseEntity<List<ResidentResponse>> getByCellphone(@Parameter @PathVariable String cellphone) {

        var residents = useCase.findByCellphone(cellphone);

        var response = residents.stream().map(ResidentResponse::fromDomain).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
