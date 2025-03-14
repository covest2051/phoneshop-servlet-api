package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.ProductServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        Optional<Product> optionalProduct = productService.getProduct(Long.valueOf(productId.substring(1)));

        optionalProduct
                .map(p -> setProductAttribute(p, request))
                .orElseThrow(ProductNotFoundException::new);

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    private Product setProductAttribute(Product product, HttpServletRequest request) {
        request.setAttribute("product", product);
        return product;
    }
}
