package edu.eci.preparcial.core.service;

import edu.eci.preparcial.controller.DTO.PaymentDTO;
import edu.eci.preparcial.controller.mapper.OrderMapper;
import edu.eci.preparcial.core.exception.SportLifeException;
import edu.eci.preparcial.core.model.Order;
import edu.eci.preparcial.core.model.enums.EstadoOrden;
import edu.eci.preparcial.persistence.nonrelational.document.CartDocument;
import edu.eci.preparcial.persistence.nonrelational.document.OrderDocument;
import edu.eci.preparcial.persistence.nonrelational.document.PaymentDocument;
import edu.eci.preparcial.persistence.nonrelational.repository.CartRepository;
import edu.eci.preparcial.persistence.nonrelational.repository.OrderRepository;
import edu.eci.preparcial.persistence.nonrelational.repository.PaymentRepository;
import edu.eci.preparcial.persistence.nonrelational.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    public Order processPayment(UUID userId, PaymentDTO dto) {
        CartDocument cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new SportLifeException("Carrito no encontrado", HttpStatus.NOT_FOUND));

        if (cart.getItems().isEmpty()) {
            throw new SportLifeException("El carrito está vacío", HttpStatus.BAD_REQUEST);
        }

        // Verificar stock antes de procesar el pago
        cart.getItems().forEach(item ->
            productRepository.findById(item.getProductId()).ifPresent(p -> {
                if (p.getStock() < item.getQuantity()) {
                    throw new SportLifeException(
                        "Stock insuficiente para: " + p.getName(), HttpStatus.CONFLICT);
                }
            })
        );

        OrderDocument order = new OrderDocument();
        order.setId(UUID.randomUUID());
        order.setUserId(userId);
        order.setTotal(cart.getTotal());

        try {
            boolean paymentApproved = simulatePayment();

            if (paymentApproved) {
                UUID transactionId = UUID.randomUUID();
                order.setStatus(EstadoOrden.PAID);
                order.setTransactionId(transactionId);
                order = orderRepository.save(order);

                PaymentDocument payment = new PaymentDocument();
                payment.setId(UUID.randomUUID());
                payment.setOrderId(order.getId());
                payment.setMethod(dto.getPaymentMethod());
                payment.setStatus(EstadoOrden.PAID);
                payment.setTransactionId(transactionId);
                paymentRepository.save(payment);

                cart.getItems().forEach(item ->
                    productRepository.findById(item.getProductId()).ifPresent(p -> {
                        p.setStock(p.getStock() - item.getQuantity());
                        productRepository.save(p);
                    })
                );

                cartRepository.delete(cart);

            } else {
                order.setStatus(EstadoOrden.REJECTED);
                order = orderRepository.save(order);

                PaymentDocument payment = new PaymentDocument();
                payment.setId(UUID.randomUUID());
                payment.setOrderId(order.getId());
                payment.setMethod(dto.getPaymentMethod());
                payment.setStatus(EstadoOrden.REJECTED);
                paymentRepository.save(payment);
            }

        } catch (SportLifeException e) {
            throw e;
        } catch (Exception e) {
            order.setStatus(EstadoOrden.REJECTED);
            orderRepository.save(order);
            throw new SportLifeException("Error procesando el pago", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return orderMapper.toModel(order);
    }

    private boolean simulatePayment() {
        return Math.random() > 0.3;
    }
}
