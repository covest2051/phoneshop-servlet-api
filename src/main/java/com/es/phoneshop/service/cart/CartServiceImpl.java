package com.es.phoneshop.service.cart;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Optional;

public class CartServiceImpl implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = CartServiceImpl.class.getName() + "cart";

    private ProductService productService;

    private static final CartService CART_SERVICE = new CartServiceImpl();

    public synchronized static CartService getCartService() {
        return CART_SERVICE;
    }

    public CartServiceImpl() {
        this.productService = ProductServiceImpl.getProductService();
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            productService.getProduct(productId)
                    .ifPresent(product -> createOrUpdateCartItem(cart, product, quantity +
                            getCartItem(cart, product)
                                    .map(CartItem::getQuantity).orElse(0))
                    );
            recalculateCartCost(cart);
            recalculateCartTotalQuantity(cart);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            productService.getProduct(productId)
                    .ifPresent(product -> createOrUpdateCartItem(cart, product, quantity));
            recalculateCartCost(cart);
            recalculateCartTotalQuantity(cart);
        }
    }

    private void createOrUpdateCartItem(Cart cart, Product product, int quantity) {
        synchronized (cart) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Optional<CartItem> existedCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findFirst();
            if (existedCartItem.isPresent()) {
                if (product.getStock() < quantity) {
                    throw new OutOfStockException(product, quantity, product.getStock());
                }

                existedCartItem.get().setQuantity(quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        synchronized (cart) {
            cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));
            recalculateCartCost(cart);
            recalculateCartTotalQuantity(cart);
        }
    }

    @Override
    public Optional<CartItem> getCartItem(Cart cart, Product product) {
        synchronized (cart) {
            return cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findFirst();
        }
    }


    @Override
    public int getProductQuantityInCart(Cart cart, Long productId) {
        synchronized (cart) {
            return cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        }
    }

    @Override
    public void recalculateCartTotalQuantity(Cart cart) {
        synchronized (cart) {
            cart.setTotalQuantity(cart.getItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum());
        }
    }

    @Override
    public void recalculateCartCost(Cart cart) {
        synchronized (cart) {
            cart.setTotalCost(cart.getItems().stream()
                    .map(cartItem -> cartItem.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
    }
}
