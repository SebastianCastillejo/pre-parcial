package edu.eci.preparcial.persistence.nonrelational.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "carts")
public class CartDocument {

    @Id
    private UUID id;

    private UUID userId;

    private List<CartItemEmbedded> items = new ArrayList<>();

    private int total;

    private LocalDateTime createdAt = LocalDateTime.now();
}
