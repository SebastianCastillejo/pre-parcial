package edu.eci.preparcial.core.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private UUID id;
    private UUID userId;
    private List<CartItem> items;
    private int total;
    private LocalDateTime createdAt;
}
