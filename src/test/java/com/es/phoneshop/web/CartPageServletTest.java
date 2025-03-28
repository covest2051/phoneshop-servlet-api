package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    @Captor
    private ArgumentCaptor<String> redirectCaptor;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet_NoErrors() throws Exception {
        Cart cart = new Cart();

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("errors")).thenReturn(null);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doGet(request, response);

        verify(request, never()).setAttribute(eq("errors"), any());
        verify(session, never()).removeAttribute("errors");

        verify(request).setAttribute(eq("cart"), cartArgumentCaptor.capture());
        assertEquals(cart, cartArgumentCaptor.getValue());

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoDelete_RemoveProductAndRedirect() throws IOException {
        Cart cart = new Cart();
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getContextPath()).thenReturn("/test-context");

        servlet.doDelete(request, response);

        verify(cartService).delete(cart, 1L);
        verify(response).sendRedirect(redirectCaptor.capture());

        String expectedRedirect = "/test-context/cart?message=Cart item removed successfully";
        assertEquals(expectedRedirect, redirectCaptor.getValue());
    }
}
