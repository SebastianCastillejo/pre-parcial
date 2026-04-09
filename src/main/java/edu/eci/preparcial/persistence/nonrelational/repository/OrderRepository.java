package edu.eci.preparcial.persistence.nonrelational.repository;

import edu.eci.preparcial.persistence.nonrelational.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<OrderDocument, UUID> {
    List<OrderDocument> findByUserId(UUID userId);
}
