package com.fraga.projectManager.exception.advice;

import com.fraga.projectManager.exception.HttpClientException;
import com.fraga.projectManager.exception.IlegalArgumentException;
import com.fraga.projectManager.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return errorResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IlegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIlegalArgumentException(IlegalArgumentException ex) {
        log.error("Ilegal argument: {}", ex.getMessage(), ex);
        return errorResponseEntity(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientException(HttpClientException ex) {
        log.error("Not access: {}", ex.getMessage(), ex);
        return errorResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Argument invalid: {}", ex.getDetailMessageArguments(), ex);
        return errorResponseEntity(HttpStatus.BAD_REQUEST, Arrays.toString(ex.getDetailMessageArguments()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal state: {}", ex, ex);
        return errorResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> errorResponseEntity(HttpStatus httpStatus, String message) {
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setStatusCode(httpStatus.value());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
