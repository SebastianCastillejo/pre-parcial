package edu.eci.preparcial.controller.mapper;

import edu.eci.preparcial.core.model.Product;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toModel(ProductDocument document) {
        Product product = new Product();
        product.setId(document.getId());
        product.setName(document.getName());
        product.setDescription(document.getDescription());
        product.setCategory(document.getCategory());
        product.setPrice(document.getPrice());
        product.setStock(document.getStock());
        product.setStatus(document.getStatus());
        product.setImages(document.getImages());
        return product;
    }

    public ProductDocument toDocument(Product product) {
        ProductDocument document = new ProductDocument();
        document.setId(product.getId());
        document.setName(product.getName());
        document.setDescription(product.getDescription());
        document.setCategory(product.getCategory());
        document.setPrice(product.getPrice());
        document.setStock(product.getStock());
        document.setStatus(product.getStatus());
        document.setImages(product.getImages());
        return document;
    }
}
