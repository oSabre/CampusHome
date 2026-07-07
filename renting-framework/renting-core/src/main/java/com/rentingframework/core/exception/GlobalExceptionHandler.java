package com.rentingframework.core.exception;
 
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
/**
 * Picked up automatically by every app once its main class component-scans
 * com.rentingframework.core (already required for the core services and
 * repositories to be found at all). One error-response shape for all
 * three apps, with zero duplicated @ExceptionHandler code.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
 
    // Catches the remaining plain RuntimeExceptions each service still
    // throws for business-rule violations (already requested, wrong
    // password, only PENDING can be accepted, etc). Treated as 400 for
    // now — split into a dedicated BusinessRuleViolationException later
    // if you want finer-grained status codes per rule.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
 
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}