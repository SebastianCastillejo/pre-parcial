package edu.eci.preparcial.persistence.nonrelational.repository;

import edu.eci.preparcial.persistence.nonrelational.document.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends MongoRepository<CartDocument, UUID> {
    Optional<CartDocument> findByUserId(UUID userId);
}
