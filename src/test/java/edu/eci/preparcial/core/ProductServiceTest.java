package edu.eci.preparcial.core;

import edu.eci.preparcial.controller.DTO.ProductDTO;
import edu.eci.preparcial.controller.mapper.ProductMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Product;
import edu.eci.preparcial.core.model.enums.CategoriaProducto;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import edu.eci.preparcial.core.service.ProductService;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository productRepository;
    @Mock ProductMapper productMapper;
    @InjectMocks ProductService productService;

    @Test
    void getById_ProductoNoExiste_LanzaNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> productService.getById(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void getById_ProductoInactivo_LanzaNotFound() {
        UUID id = UUID.randomUUID();
        ProductDocument doc = new ProductDocument();
        doc.setId(id);
        doc.setStatus(EstadoProducto.INACTIVO);

        when(productRepository.findById(id)).thenReturn(Optional.of(doc));

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> productService.getById(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void getById_ProductoActivo_RetornaProducto() {
        UUID id = UUID.randomUUID();
        ProductDocument doc = new ProductDocument();
        doc.setId(id);
        doc.setStatus(EstadoProducto.ACTIVO);

        when(productRepository.findById(id)).thenReturn(Optional.of(doc));
        when(productMapper.toModel(doc)).thenReturn(new Product());

        assertDoesNotThrow(() -> productService.getById(id));
    }

    @Test
    void getAllProducts_RetornaListaActivos() {
        ProductDocument doc = new ProductDocument();
        doc.setStatus(EstadoProducto.ACTIVO);

        when(productRepository.findByStatus(EstadoProducto.ACTIVO)).thenReturn(List.of(doc));
        when(productMapper.toModel(any())).thenReturn(new Product());

        var result = productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void create_ProductoValido_GuardaYRetorna() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Camiseta");
        dto.setDescription("Desc");
        dto.setCategory(CategoriaProducto.GIMNASIO);
        dto.setPrice(50000);
        dto.setStock(10);
        dto.setStatus(EstadoProducto.ACTIVO);

        when(productRepository.save(any())).thenReturn(new ProductDocument());
        when(productMapper.toModel(any())).thenReturn(new Product());

        assertDoesNotThrow(() -> productService.create(dto));
        verify(productRepository, times(1)).save(any());
    }
}
