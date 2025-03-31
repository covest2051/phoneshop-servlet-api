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
                    .ifPresent(product -> {
                        Optional<CartItem> optionalCartItem = cart.getItems().stream()
                                .filter(cartItem -> cartItem.getProduct().equals(product))
                                .findFirst();

                        if (optionalCartItem.isPresent()) {
                            int productsInCartCount = optionalCartItem.get().getQuantity();
                            if (product.getStock() - productsInCartCount < quantity)
                                throw new OutOfStockException(product, quantity, product.getStock());
                            updateCartItem(optionalCartItem.get(), product, quantity);
                        } else {
                            cart.getItems().add(new CartItem(product, quantity));
                        }
                    });
            recalculateCartCost(cart);
            recalculateCartTotalQuantity(cart);
        }
    }

    private void updateCartItem(CartItem cartItem, Product product, int quantity) throws OutOfStockException {
        int productsInCartCount = cartItem.getQuantity();
        if (product.getStock() - productsInCartCount < quantity)
            throw new OutOfStockException(product, quantity, product.getStock());

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            Optional<Product> productForAdding = productService.getProduct(productId);

            if (productForAdding.isPresent()) {
                Product product = productForAdding.get();

                if (quantity <= 0) {
                    throw new IllegalArgumentException();
                }

                Optional<CartItem> optionalCartItem = cart.getItems().stream()
                        .filter(cartItem -> cartItem.getProduct().equals(product))
                        .findFirst();
                if (optionalCartItem.isPresent()) {
                    if (product.getStock() < quantity)
                        throw new OutOfStockException(product, quantity, product.getStock());

                    optionalCartItem.get().setQuantity(quantity);
                } else {
                    cart.getItems().add(new CartItem(productForAdding.get(), quantity));
                }
            }
            recalculateCartCost(cart);
            recalculateCartTotalQuantity(cart);
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
