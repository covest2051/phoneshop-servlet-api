package com.es.phoneshop.service.cartService;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.productService.ProductService;
import com.es.phoneshop.service.productService.ProductServiceImpl;

import java.util.Optional;

public class CartServiceImpl implements CartService {
    private Cart cart = new Cart();

    private ProductService productService;

    private static final CartService CART_SERVICE = new CartServiceImpl();

    public synchronized static CartService getCartService() {
        return CART_SERVICE;
    }

    public CartServiceImpl() {
        this.productService = ProductServiceImpl.getProductService();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) {
        Optional<Product> productForAdding = productService.getProduct(productId);
        if(productForAdding.isPresent()) {
            cart.getItems().add(new CartItem(productForAdding.get(), quantity));
        }
    }
}
