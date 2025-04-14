package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.OrderServiceImpl;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DefaultOrderServiceTest {
    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService = CartServiceImpl.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProductService productService;

    private OrderService orderService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
        orderService = OrderServiceImpl.getInstance();
    }

    @Test
    public void testGetOrder() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");

        when(productService.getProduct(product.getId())).thenReturn(java.util.Optional.of(product));

        Cart cart = cartService.getCart(request);

        cartService.add(cart, product.getId(), 1);

        Order order = orderService.getOrder(cart);

        assertEquals(1, order.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(200), order.getSubtotal());
        assertEquals(BigDecimal.valueOf(5), order.getDeliveryCost());
        assertEquals(BigDecimal.valueOf(205), order.getTotalCost());
    }

    @Test
    public void getPaymentMethods() {
        assertEquals(List.of(PaymentMethod.CASH, PaymentMethod.CARD), List.of(PaymentMethod.values()));
    }
}
