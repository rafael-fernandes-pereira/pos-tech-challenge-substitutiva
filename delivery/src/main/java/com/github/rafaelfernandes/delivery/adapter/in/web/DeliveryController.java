package com.github.rafaelfernandes.delivery.adapter.in.web;

import com.github.rafaelfernandes.delivery.adapter.in.web.request.DeliveryRequest;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryIdResponse;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.delivery.application.port.in.DeliveryUseCase;
import com.github.rafaelfernandes.delivery.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/delivery")
@AllArgsConstructor
@Tag(name = "Delivery", description = "Delivery Endpoint")
public class DeliveryController {

    private final DeliveryUseCase useCase;

    @Operation(summary = "Register a delivery to an apartment")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DeliveryIdResponse.class)
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
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<DeliveryIdResponse> registerDelivery(@Parameter @RequestBody DeliveryRequest request) {

        var deliveryId = useCase.create(request.apartment(),
                request.employeeCellphone(),
                request.nameDestination(),
                request.packageDescription()
        );

        return ResponseEntity.ok(new DeliveryIdResponse(deliveryId.id()));



    }

}
