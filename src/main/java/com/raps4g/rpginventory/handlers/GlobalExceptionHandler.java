package com.raps4g.rpginventory.handlers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.raps4g.rpginventory.exceptions.InsufficientGoldException;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.exceptions.InvalidCredentialsException;


import com.raps4g.rpginventory.exceptions.ItemCannotBeEquippedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = Map.of(
            "status", HttpStatus.BAD_REQUEST.value(),
            "error_code", "VALIDATION_FAILED",
            "message", "Validation failed for the request.",
            "field_errors", fieldErrors,
            "timestamp", Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Object> handleIllegalAccessException(IllegalAccessException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "ILLEGAL_ACCESS", ex.getMessage());
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(ItemCannotBeEquippedException.class)
    public ResponseEntity<Object> handleItemCannotBeEquippedException(ItemCannotBeEquippedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "CANNOT_EQUIP_ITEM", ex.getMessage());
    }

    @ExceptionHandler(InsufficientGoldException.class)
    public ResponseEntity<Object> handleInsufficientGoldException(InsufficientGoldException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INSUFFICIENT_GOLD", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "DATA_INTEGRITY_VIOLATION", ex.getMessage());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String errorCode, String message) {
        Map<String, Object> response = Map.of(
            "status", status.value(),
            "error_code", errorCode,
            "message", message,
            "timestamp", Instant.now()
        );

        return ResponseEntity.status(status).body(response);
    }

}
