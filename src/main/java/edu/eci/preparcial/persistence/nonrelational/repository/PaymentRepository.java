package edu.eci.preparcial.persistence.nonrelational.repository;

import edu.eci.preparcial.persistence.nonrelational.document.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PaymentRepository extends MongoRepository<PaymentDocument, UUID> {
}
