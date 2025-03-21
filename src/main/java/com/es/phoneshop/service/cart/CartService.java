package com.es.phoneshop.service.cart;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.OutOfStockException;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
    int getProductQuantityInCart(Cart cart, Long productId);
}
