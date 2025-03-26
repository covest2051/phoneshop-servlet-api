package com.es.phoneshop.service.cart;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void delete(Cart cart, Long productId);
    Optional<CartItem> getCartItem(Cart cart, Product product);
    int getProductQuantityInCart(Cart cart, Long productId);
    void recalculateCartCost(Cart cart);
    void recalculateCartTotalQuantity(Cart cart);
}
