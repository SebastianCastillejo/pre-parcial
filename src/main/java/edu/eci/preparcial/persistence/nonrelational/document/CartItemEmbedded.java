package edu.eci.preparcial.persistence.nonrelational.document;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemEmbedded {
    private UUID productId;
    private String productName;
    private int quantity;
    private int subtotal;
}
