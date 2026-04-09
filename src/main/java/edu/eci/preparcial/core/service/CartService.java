package edu.eci.preparcial.core.service;

import edu.eci.preparcial.controller.DTO.CartDTO;
import edu.eci.preparcial.controller.mapper.CartMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Cart;
import edu.eci.preparcial.core.model.enums.EstadoProducto;
import edu.eci.preparcial.persistence.nonrelational.document.CartDocument;
import edu.eci.preparcial.persistence.nonrelational.document.CartItemEmbedded;
import edu.eci.preparcial.persistence.nonrelational.document.ProductDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.CartRepository;
import edu.eci.preparcial.persistence.nonrelational.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public Cart addToCart(UUID userId, CartDTO dto) {
        ProductDocument product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new SportLifeException("Producto no encontrado", HttpStatus.NOT_FOUND));

        if (product.getStatus() != EstadoProducto.ACTIVO) {
            throw new SportLifeException("El producto no está disponible", HttpStatus.BAD_REQUEST);
        }
        if (product.getStock() < dto.getQuantity()) {
            throw new SportLifeException("Stock insuficiente", HttpStatus.CONFLICT);
        }

        CartDocument cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartDocument newCart = new CartDocument();
                    newCart.setId(UUID.randomUUID());
                    newCart.setUserId(userId);
                    return newCart;
                });

        cart.getItems().removeIf(i -> i.getProductId().equals(dto.getProductId()));

        CartItemEmbedded item = new CartItemEmbedded();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setQuantity(dto.getQuantity());
        item.setSubtotal(product.getPrice() * dto.getQuantity());
        cart.getItems().add(item);

        cart.setTotal(cart.getItems().stream()
                .mapToInt(CartItemEmbedded::getSubtotal).sum());

        return cartMapper.toModel(cartRepository.save(cart));
    }

    public Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .map(cartMapper::toModel)
                .orElseThrow(() -> new SportLifeException("Carrito no encontrado", HttpStatus.NOT_FOUND));
    }

    public Cart removeFromCart(UUID userId, UUID productId) {
        CartDocument cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new SportLifeException("Carrito no encontrado", HttpStatus.NOT_FOUND));
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        cart.setTotal(cart.getItems().stream()
                .mapToInt(CartItemEmbedded::getSubtotal).sum());
        return cartMapper.toModel(cartRepository.save(cart));
    }
}
