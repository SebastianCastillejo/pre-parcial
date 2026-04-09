package edu.eci.preparcial.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentDTO {

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;
}
