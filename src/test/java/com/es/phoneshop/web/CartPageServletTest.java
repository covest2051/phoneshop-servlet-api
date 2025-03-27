package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();
    @Captor
    private ArgumentCaptor<Cart> cartArgumentCaptor;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws Exception {
        Cart cart = new Cart();

        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doGet(request, response);

        verify(request).setAttribute(Mockito.eq("cart"), cartArgumentCaptor.capture());
        assertEquals(cart, cartArgumentCaptor.getValue());
        verify(requestDispatcher).forward(request, response);
    }

}
