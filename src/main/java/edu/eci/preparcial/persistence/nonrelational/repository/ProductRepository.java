package edu.eci.preparcial.persistence.nonrelational.repository;

import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends MongoRepository<ProductDocument, UUID> {
    List<ProductDocument> findByStatus(EstadoProducto status);
    List<ProductDocument> findByStatusAndCategory(EstadoProducto status, CategoriaProducto category);
    List<ProductDocument> findByStatusAndNameContainingIgnoreCase(EstadoProducto status, String name);
}
