package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import com.es.phoneshop.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CartServiceTest {
    private Currency usd = Currency.getInstance("USD");

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService = CartServiceImpl.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProductService productService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);
        Cart cart = cartService.getCart(request);
        assertNotNull(cart);
    }

    @Test
    public void testAddProduct() throws OutOfStockException {
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);
        cartService.add(cart, product.getId(), 1);

        assertEquals(1, cart.getItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductThrowsOutOfStockExceptionIfProductDoNotHaveStock() throws OutOfStockException {
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);
        cartService.add(cart, product.getId(), 100);

        assertEquals(1, cart.getItems().size());
        assertEquals(100, cart.getItems().get(0).getQuantity());

        cartService.add(cart, product.getId(), 1);

        assertEquals(100, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testDeleteProduct() throws OutOfStockException {
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);
        cartService.add(cart, 1L, 100);

        assertEquals(1, cart.getItems().size());

        cartService.delete(cart, 1L);

        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testRecalculateCartTotalQuantity() throws OutOfStockException {
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);
        cartService.add(cart, 1L, 10);

        assertEquals(cart.getTotalQuantity(), 10);

        cartService.add(cart, 1L, 5);

        assertEquals(cart.getTotalQuantity(), 15);
    }

    @Test
    public void testRecalculateCartTotalCost() throws OutOfStockException {
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);
        cartService.add(cart, 1L, 1);

        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(100));

        cartService.add(cart, 1L, 5);

        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(600));
    }
}
