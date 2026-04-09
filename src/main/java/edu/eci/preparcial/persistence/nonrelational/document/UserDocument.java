package edu.eci.preparcial.persistence.nonrelational.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "users")
public class UserDocument {

    @Id
    private UUID id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String role = "CUSTOMER";

    private LocalDateTime createdAt = LocalDateTime.now();
}
