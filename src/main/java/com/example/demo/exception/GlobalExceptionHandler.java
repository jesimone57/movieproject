package com.example.demo.exception;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates well-known exceptions into structured JSON error responses
 * instead of returning raw 500 stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors thrown by service methods
     * (e.g. invalid sort value, year out of range).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        logger.warn("Bad request: {}", e.getMessage());
        return Map.of("error", e.getMessage());
    }

    /**
     * Catch-all handler — prevents stack traces from leaking to the client.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnexpected(Exception e) {
        logger.error("Unexpected error", e);
        return Map.of("error", "An unexpected error occurred. Please try again.");
    }
}

