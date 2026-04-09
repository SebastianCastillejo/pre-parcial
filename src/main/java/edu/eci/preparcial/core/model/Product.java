package edu.eci.preparcial.core.model;

import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String name;
    private String description;
    private CategoriaProducto category;
    private EstadoProducto status;
    private int price;
    private int stock;
    private List<String> images;
}
