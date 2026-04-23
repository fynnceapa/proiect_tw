package com.mobylab.springbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<ErrorObject> handleBadRequest(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorObject> handleNotFound(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.NOT_FOUND.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ForbiddenException.class })
    public ResponseEntity<ErrorObject> handleForbidden(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.FORBIDDEN.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ InternalServerErrorException.class })
    public ResponseEntity<ErrorObject> handleInternalServerError(RuntimeException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(ex.getMessage())
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorObject> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorObject errorObject = new ErrorObject()
                .setStatusCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(message)
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
}
