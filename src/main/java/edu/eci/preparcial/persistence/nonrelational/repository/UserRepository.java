package edu.eci.preparcial.persistence.nonrelational.repository;

import edu.eci.preparcial.persistence.nonrelational.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<UserDocument, UUID> {
    Optional<UserDocument> findByEmail(String email);
    boolean existsByEmail(String email);
}
