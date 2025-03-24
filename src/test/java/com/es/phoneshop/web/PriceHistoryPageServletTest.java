package com.es.phoneshop.web;

import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PriceHistoryPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;

    private PriceHistoryPageServlet servlet = new PriceHistoryPageServlet();
    private ProductService productService;

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcher);
        productService = ProductServiceImpl.getProductService();
    }

    @Test
    public void testDoGetProductFound() throws ServletException, IOException {
        Currency usd = Currency.getInstance("USD");
        Product productForSave = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(productForSave);
        productForSave.getPriceHistory().add(new PriceHistory(new Date(), new BigDecimal("250")));

        when(request.getPathInfo()).thenReturn("/" + productForSave.getId());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any(Product.class));
        verify(request).setAttribute(eq("priceHistory"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDoGetProductNotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1000");
        servlet.doGet(request, response);
    }
}
