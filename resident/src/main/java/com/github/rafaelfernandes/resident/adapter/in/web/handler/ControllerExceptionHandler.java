package com.github.rafaelfernandes.resident.adapter.in.web.handler;

import com.github.rafaelfernandes.resident.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.resident.common.exception.ResidentApartmentExistsException;
import com.github.rafaelfernandes.resident.common.exception.ResidentNotFoundException;
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

    @ExceptionHandler({ResidentApartmentExistsException.class})
    public ResponseEntity<ResponseError> errorValidation(ResidentApartmentExistsException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler({ResidentNotFoundException.class})
    public ResponseEntity<ResponseError> errorValidation(ResidentNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }
}
