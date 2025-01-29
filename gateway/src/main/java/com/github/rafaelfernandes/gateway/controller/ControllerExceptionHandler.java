package com.github.rafaelfernandes.gateway.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.github.rafaelfernandes.gateway.exception.*;


@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseError> unauthorizedException(UnauthorizedException exception){
        
        var response = new ResponseError(exception.getMessage(), exception.getStatus());

        return ResponseEntity
            .status(exception.getStatus())
            .body(response)
        ;

    }




}
