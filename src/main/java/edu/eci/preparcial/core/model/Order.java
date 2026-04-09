package edu.eci.preparcial.core.model;

import edu.eci.preparcial.core.model.enums.EstadoOrden;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private UUID userId;
    private int total;
    private EstadoOrden status;
    private UUID transactionId;
    private LocalDateTime createdAt;
}
