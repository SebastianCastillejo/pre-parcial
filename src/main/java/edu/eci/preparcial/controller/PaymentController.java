package edu.eci.preparcial.controller;

import edu.eci.preparcial.controller.DTO.PaymentDTO;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Order;
import edu.eci.preparcial.core.model.enums.EstadoOrden;
import edu.eci.preparcial.core.service.PaymentService;
import edu.eci.preparcial.persistence.nonrelational.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pagos y Órdenes", description = "Proceso de pago y gestión de órdenes de compra")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    private UUID getAuthenticatedUserId() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SportLifeException(
                        "Usuario no encontrado", HttpStatus.NOT_FOUND))
                .getId();
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Procesar pago",
            description = "Genera una orden y procesa el pago. PAID actualiza stock, REJECTED permite reintentar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago aprobado - orden PAID"),
            @ApiResponse(responseCode = "400", description = "Carrito vacío o datos inválidos"),
            @ApiResponse(responseCode = "402", description = "Pago rechazado - orden REJECTED"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente al momento del pago")
    })
    public ResponseEntity<Order> processPayment(@Valid @RequestBody PaymentDTO dto) {
        Order order = paymentService.processPayment(getAuthenticatedUserId(), dto);
        if (order.getStatus() == EstadoOrden.PAID) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(order);
    }
}
