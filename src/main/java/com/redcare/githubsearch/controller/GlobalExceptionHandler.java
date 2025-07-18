package com.redcare.githubsearch.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
    log.warn("Constraint violation: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String path = violation.getPropertyPath().toString();
              String message = violation.getMessage();
              errors.put(path, message);
            });
    return errors;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, String> handleOtherExceptions(Exception ex) {
    log.error("Unhandled exception: ", ex);
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return error;
  }
}
