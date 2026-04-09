package edu.eci.preparcial.core;

import edu.eci.preparcial.controller.DTO.CartDTO;
import edu.eci.preparcial.controller.mapper.CartMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Cart;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import edu.eci.preparcial.core.service.CartService;
import edu.eci.preparcial.persistence.nonrelational.document.CartDocument;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.CartRepository;
import edu.eci.preparcial.persistence.nonrelational.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock CartRepository cartRepository;
    @Mock ProductRepository productRepository;
    @Mock CartMapper cartMapper;
    @InjectMocks CartService cartService;

    @Test
    void addToCart_ProductoNoExiste_LanzaNotFound() {
        UUID userId = UUID.randomUUID();
        CartDTO dto = new CartDTO();
        dto.setProductId(UUID.randomUUID());
        dto.setQuantity(2);

        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.empty());

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> cartService.addToCart(userId, dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void addToCart_ProductoInactivo_LanzaBadRequest() {
        UUID userId = UUID.randomUUID();
        CartDTO dto = new CartDTO();
        dto.setProductId(UUID.randomUUID());
        dto.setQuantity(2);

        ProductDocument product = new ProductDocument();
        product.setStatus(EstadoProducto.INACTIVO);
        product.setStock(10);

        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.of(product));

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> cartService.addToCart(userId, dto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void addToCart_StockInsuficiente_LanzaConflict() {
        UUID userId = UUID.randomUUID();
        CartDTO dto = new CartDTO();
        dto.setProductId(UUID.randomUUID());
        dto.setQuantity(10);

        ProductDocument product = new ProductDocument();
        product.setStatus(EstadoProducto.ACTIVO);
        product.setStock(2);

        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.of(product));

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> cartService.addToCart(userId, dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void addToCart_TodoOk_GuardaCarrito() {
        UUID userId = UUID.randomUUID();
        CartDTO dto = new CartDTO();
        dto.setProductId(UUID.randomUUID());
        dto.setQuantity(2);

        ProductDocument product = new ProductDocument();
        product.setId(dto.getProductId());
        product.setName("Tenis");
        product.setStatus(EstadoProducto.ACTIVO);
        product.setStock(10);
        product.setPrice(100000);

        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenReturn(new CartDocument());
        when(cartMapper.toModel(any())).thenReturn(new Cart());

        assertDoesNotThrow(() -> cartService.addToCart(userId, dto));
        verify(cartRepository, times(1)).save(any());
    }

    @Test
    void getCart_CarritoNoExiste_LanzaNotFound() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        SportLifeException ex = assertThrows(SportLifeException.class,
                () -> cartService.getCart(userId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
