package com.es.phoneshop.service.cartService;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.productService.ProductService;
import com.es.phoneshop.service.productService.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
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
        if(cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Optional<Product> productForAdding = productService.getProduct(productId);

        if (productForAdding.isPresent()) {
            Product product = productForAdding.get();
            int productsInCartCount = (int) cart.getItems().stream()
                    .map(CartItem::getProduct)
                    .filter(cartItemProduct -> cartItemProduct.equals(product))
                    .count();
            if (product.getStock() - productsInCartCount < quantity)
                throw new OutOfStockException(product, quantity, product.getStock());

            Optional<CartItem> existedCartItem = cart.getItems().stream()
                            .filter(cartItem -> cartItem.getProduct().equals(product))
                                    .findFirst();

            if(existedCartItem.isPresent()) {
                existedCartItem.get().setQuantity(existedCartItem.get().getQuantity() + quantity);
            } else {
                cart.getItems().add(new CartItem(productForAdding.get(), quantity));
            }
        }
    }
}
