package edu.eci.preparcial.controller.mapper;

import edu.eci.preparcial.core.model.Order;
import edu.eci.preparcial.persistence.nonrelational.document.OrderDocument;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toModel(OrderDocument document) {
        Order order = new Order();
        order.setId(document.getId());
        order.setUserId(document.getUserId());
        order.setTotal(document.getTotal());
        order.setStatus(document.getStatus());
        order.setTransactionId(document.getTransactionId());
        order.setCreatedAt(document.getCreatedAt());
        return order;
    }
}
