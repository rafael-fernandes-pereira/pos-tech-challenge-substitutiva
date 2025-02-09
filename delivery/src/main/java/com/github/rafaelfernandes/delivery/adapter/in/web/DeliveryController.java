package com.github.rafaelfernandes.delivery.adapter.in.web;

import com.github.rafaelfernandes.delivery.adapter.in.web.request.DeliveryRequest;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryIdResponse;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.DeliveryResponse;
import com.github.rafaelfernandes.delivery.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.delivery.application.port.in.DeliveryUseCase;
import com.github.rafaelfernandes.delivery.common.annotations.WebAdapter;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @Operation(summary = "Show all deliveries by apartment and delivery status")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DeliveryResponse[].class)
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
            path = "/apartment/{apartment}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<List<DeliveryResponse>> getAllByApartment(
            @Parameter(description = "Apartment number") @PathVariable Integer apartment,
            @Parameter(description = "Delivery status") @RequestParam(required = false) String deliveryStatus
    ) {

        var deliveries = useCase.getAllByApartment(apartment, deliveryStatus);

        List<DeliveryResponse> deliveriesResponse = new ArrayList<>();

        for (var delivery: deliveries){

            var deliveryResponse = new DeliveryResponse(
                delivery.getId().id(),
                delivery.getResident().apartment(),
                delivery.getDeliveryStatus(),
                delivery.getEmployee().name(),
                delivery.getDestinationName(),
                delivery.getPackageDescription(),
                delivery.getReceiverName(),
                delivery.getNotificationStatus(),
                delivery.getEnterDate(),
                delivery.getExitDate()
            );

            deliveriesResponse.add(deliveryResponse);


        }

        return ResponseEntity.ok(deliveriesResponse);

    }


}
