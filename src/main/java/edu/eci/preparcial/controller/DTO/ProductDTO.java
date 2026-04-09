package edu.eci.preparcial.controller.DTO;

import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaProducto category;

    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private int price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @NotNull(message = "El estado es obligatorio")
    private EstadoProducto status;

    private List<String> images;
}
