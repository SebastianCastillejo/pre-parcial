package edu.eci.preparcial.core.model;

import lombok.Data;
import java.util.UUID;

@Data
public class CartItem {
    private UUID productId;
    private int quantity;
    private int subtotal;
}
