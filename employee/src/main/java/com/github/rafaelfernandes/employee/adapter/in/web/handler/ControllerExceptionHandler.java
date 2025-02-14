package com.github.rafaelfernandes.employee.adapter.in.web.handler;


import com.github.rafaelfernandes.employee.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.employee.common.exception.EmployeeCellphoneExistsException;
import com.github.rafaelfernandes.employee.common.exception.EmployeeNotFoundException;
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
}
