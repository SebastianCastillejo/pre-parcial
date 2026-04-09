package edu.eci.preparcial.persistence.nonrelational.document;

import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "products")
public class ProductDocument {

    @Id
    private UUID id;

    private String name;
    private String description;
    private CategoriaProducto category;
    private int price;
    private int stock;
    private EstadoProducto status;
    private List<String> images;
}
