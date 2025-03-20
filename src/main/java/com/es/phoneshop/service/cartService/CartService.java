package com.es.phoneshop.service.cartService;

import com.es.phoneshop.model.cart.Cart;

public interface CartService {
    Cart getCart();
    void add(Long productId, int quantity);
}
