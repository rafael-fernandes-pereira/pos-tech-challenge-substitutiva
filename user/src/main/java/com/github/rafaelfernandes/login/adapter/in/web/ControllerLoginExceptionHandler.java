package com.github.rafaelfernandes.login.adapter.in.web;

import com.github.rafaelfernandes.common.exceptions.InvalidTokenException;
import com.github.rafaelfernandes.common.exceptions.UnauthorizedException;
import com.github.rafaelfernandes.user.adapter.in.web.response.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.github.rafaelfernandes.login")
public class ControllerLoginExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> errorValidation(ValidationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseError> unauthorizedException(UnauthorizedException exception){

        var response = new ResponseError(exception.getMessage(), exception.getStatus());

        return ResponseEntity
                .status(exception.getStatus())
                .body(response)
                ;

    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseError> unauthorizedException(ExpiredJwtException exception){

        var response = new ResponseError(exception.getMessage(), 403);

        return ResponseEntity
                .status(403)
                .body(response)
                ;

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseError> unauthorizedException(InvalidTokenException exception){

        var response = new ResponseError(exception.getMessage(), exception.getStatus());

        return ResponseEntity
                .status(exception.getStatus())
                .body(response)
                ;

    }
}
