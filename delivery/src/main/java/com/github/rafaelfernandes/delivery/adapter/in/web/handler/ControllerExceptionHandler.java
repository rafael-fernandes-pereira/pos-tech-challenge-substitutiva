package com.github.rafaelfernandes.delivery.adapter.in.web.handler;


import com.github.rafaelfernandes.delivery.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.delivery.common.exception.ApartmentNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.DeliveryNotFoundException;
import com.github.rafaelfernandes.delivery.common.exception.EmployeeCellphoneExistsException;
import com.github.rafaelfernandes.delivery.common.exception.EmployeeNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> errorValidation(ValidationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({ApartmentNotFoundException.class})
    public ResponseEntity<ResponseError> errorValidation(ApartmentNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler({EmployeeCellphoneExistsException.class})
    public ResponseEntity<ResponseError> errorValidation(EmployeeCellphoneExistsException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity<ResponseError> errorValidation(EmployeeNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler({DeliveryNotFoundException.class})
    public ResponseEntity<ResponseError> errorValidation(DeliveryNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }
}
