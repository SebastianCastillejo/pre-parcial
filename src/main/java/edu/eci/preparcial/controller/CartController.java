package edu.eci.preparcial.controller;

import edu.eci.preparcial.controller.DTO.CartDTO;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Cart;
import edu.eci.preparcial.core.service.CartService;
import edu.eci.preparcial.persistence.nonrelational.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private UUID getAuthenticatedUserId() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SportLifeException(
                        "Usuario no encontrado", HttpStatus.NOT_FOUND))
                .getId();
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody CartDTO dto) {
        return ResponseEntity.ok(cartService.addToCart(getAuthenticatedUserId(), dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart(getAuthenticatedUserId()));
    }

    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Cart> removeFromCart(@PathVariable UUID productId) {
        return ResponseEntity.ok(cartService.removeFromCart(getAuthenticatedUserId(), productId));
    }
}
