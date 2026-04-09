package edu.eci.preparcial.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SportLifeException extends RuntimeException {

    private final HttpStatus status;

    public SportLifeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
