package edu.eci.preparcial.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String name;
    private String email;
    private String role;

    @JsonIgnore
    private String password;

    private LocalDateTime createdAt;
}
