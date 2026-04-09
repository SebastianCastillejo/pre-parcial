package edu.eci.preparcial.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Validacion de inputs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String firstError = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Error de validacion");
        log.warn("Validation error: {}", firstError);
        return new ResponseEntity<>(
                new ErrorResponse(firstError, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 400, 404, 409 - Errores de negocio
    @ExceptionHandler(SportLifeException.class)
    public ResponseEntity<ErrorResponse> handleBusinessErrors(SportLifeException ex) {
        log.warn("Business error: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), ex.getStatus().value()),
                ex.getStatus()
        );
    }

    // 403 - Sin permisos de rol
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("No tienes el rol necesario para esta accion", HttpStatus.FORBIDDEN.value()),
                HttpStatus.FORBIDDEN
        );
    }

    // 401 - Token invalido
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationError(AuthenticationCredentialsNotFoundException ex) {
        log.warn("Authentication error: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Token invalido o no proporcionado", HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 500 - Error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralErrors(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
