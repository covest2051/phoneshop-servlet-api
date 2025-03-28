package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private Cart cart;
    @Mock
    HttpSession session;


    private DeleteCartItemServlet servlet;

    @Before
    public void setup() throws ServletException {
        cart = new Cart();

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(100);

        CartItem cartItem = new CartItem(product, 1);
        cart.getItems().add(cartItem);

        this.servlet = new DeleteCartItemServlet();
        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getContextPath()).thenReturn("");
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        assertTrue(cart.getItems().isEmpty());

        verify(response).sendRedirect("/cart?message=Cart item removed successfully");
    }
}
