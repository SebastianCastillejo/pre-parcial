package edu.eci.preparcial.core.service;

import edu.eci.preparcial.controller.DTO.ProductDTO;
import edu.eci.preparcial.controller.mapper.ProductMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Product;
import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productRepository.findByStatus(EstadoProducto.ACTIVO)
                .stream().map(productMapper::toModel).toList();
    }

    public List<Product> filterByCategory(CategoriaProducto category) {
        return productRepository.findByStatusAndCategory(EstadoProducto.ACTIVO, category)
                .stream().map(productMapper::toModel).toList();
    }

    public List<Product> filterByName(String name) {
        return productRepository.findByStatusAndNameContainingIgnoreCase(EstadoProducto.ACTIVO, name)
                .stream().map(productMapper::toModel).toList();
    }

    public Product getById(UUID id) {
        return productRepository.findById(id)
                .filter(p -> p.getStatus() == EstadoProducto.ACTIVO)
                .map(productMapper::toModel)
                .orElseThrow(() -> new SportLifeException("Producto no encontrado", HttpStatus.NOT_FOUND));
    }

    public Product create(ProductDTO dto) {
        ProductDocument document = new ProductDocument();
        document.setId(UUID.randomUUID());
        document.setName(dto.getName());
        document.setDescription(dto.getDescription());
        document.setCategory(dto.getCategory());
        document.setPrice(dto.getPrice());
        document.setStock(dto.getStock());
        document.setStatus(dto.getStatus());
        document.setImages(dto.getImages());
        return productMapper.toModel(productRepository.save(document));
    }
}
