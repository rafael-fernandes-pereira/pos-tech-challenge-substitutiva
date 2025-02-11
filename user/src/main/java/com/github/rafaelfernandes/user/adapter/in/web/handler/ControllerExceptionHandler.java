package com.github.rafaelfernandes.user.adapter.in.web.handler;

import com.github.rafaelfernandes.common.exceptions.UserCellphoneExistsException;
import com.github.rafaelfernandes.user.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.common.exceptions.ResidentApartmentExistsException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.github.rafaelfernandes.user")
public class ControllerExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> errorValidation(ValidationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({UserCellphoneExistsException.class})
    public ResponseEntity<ResponseError> errorValidation(UserCellphoneExistsException exception){
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
