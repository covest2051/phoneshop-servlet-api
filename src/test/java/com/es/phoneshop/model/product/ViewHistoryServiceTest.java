package com.es.phoneshop.model.product;

import com.es.phoneshop.service.product.ViewHistoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewHistoryServiceTest {

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    private ViewHistoryServiceImpl service = ViewHistoryServiceImpl.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testViewHistoryAddNewProductWhenHistoryIsEmpty() {
        when(session.getAttribute("viewHistory")).thenReturn(null);

        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        service.viewHistory(product, request);

        verify(session).setAttribute(eq("viewHistory"), any(List.class));
    }

    @Test
    public void testViewHistoryAddDuplicatedProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        List<Product> viewHistory = new ArrayList<>();
        viewHistory.add(product);

        when(session.getAttribute("viewHistory")).thenReturn(viewHistory);

        service.viewHistory(product, request);

        assertEquals(1, viewHistory.size());
    }

    @Test
    public void testViewHistoryAddNewProductWhenViewHistorySizeIsFull() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("test-product-code2", "Samsung Galaxy S II", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product3 = new Product("test-product-code3", "Samsung Galaxy S III", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        List<Product> viewHistory = new ArrayList<>();
        viewHistory.add(product1);
        viewHistory.add(product2);
        viewHistory.add(product3);

        when(session.getAttribute("viewHistory")).thenReturn(viewHistory);

        Product product4 = new Product("test-product-code4", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        service.viewHistory(product4, request);

        assertTrue(viewHistory.contains(product4));
        assertFalse(viewHistory.contains(product1));
    }
}
