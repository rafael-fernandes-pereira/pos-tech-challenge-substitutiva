package com.github.rafaelfernandes.user.adapter.in;

import com.github.rafaelfernandes.user.adapter.in.response.ResponseError;
import com.github.rafaelfernandes.user.common.exceptions.ResidentApartmentExistsException;
import com.github.rafaelfernandes.user.common.exceptions.ResidentCellphoneExistsException;
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

    @ExceptionHandler({ResidentCellphoneExistsException.class})
    public ResponseEntity<ResponseError> errorValidation(ResidentCellphoneExistsException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler({ResidentApartmentExistsException.class})
    public ResponseEntity<ResponseError> errorValidation(ResidentApartmentExistsException exception){
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(new ResponseError(exception.getMessage(), exception.getStatus()));
    }
}
