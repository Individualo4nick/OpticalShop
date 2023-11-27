package com.example.gateway.controller;

import com.example.gateway.controller.exc.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler({WebClientResponseException.class})
    public ResponseEntity<ErrorResponse> handleErrorOtherService(Exception ex) {
        return ResponseEntity.status(500)
                .body(new ErrorResponse("Something went wrong..."));
    }
}
