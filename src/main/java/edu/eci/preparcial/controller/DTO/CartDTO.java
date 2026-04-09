package edu.eci.preparcial.controller.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CartDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private UUID productId;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int quantity;
}
