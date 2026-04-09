package edu.eci.preparcial.persistence.nonrelational.document;

import edu.eci.preparcial.core.model.enums.EstadoOrden;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "payments")
public class PaymentDocument {

    @Id
    private UUID id;

    private UUID orderId;

    private String method;

    private EstadoOrden status;

    private UUID transactionId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
