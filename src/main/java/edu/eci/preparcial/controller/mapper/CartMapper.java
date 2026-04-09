package edu.eci.preparcial.controller.mapper;

import edu.eci.preparcial.core.model.Cart;
import edu.eci.preparcial.core.model.CartItem;
import edu.eci.preparcial.persistence.nonrelational.document.CartDocument;
import edu.eci.preparcial.persistence.nonrelational.document.CartItemEmbedded;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public Cart toModel(CartDocument document) {
        Cart cart = new Cart();
        cart.setId(document.getId());
        cart.setUserId(document.getUserId());
        cart.setTotal(document.getTotal());
        cart.setCreatedAt(document.getCreatedAt());
        cart.setItems(document.getItems().stream()
                .map(this::toItemModel)
                .toList());
        return cart;
    }

    private CartItem toItemModel(CartItemEmbedded embedded) {
        CartItem item = new CartItem();
        item.setProductId(embedded.getProductId());
        item.setQuantity(embedded.getQuantity());
        item.setSubtotal(embedded.getSubtotal());
        return item;
    }
}
