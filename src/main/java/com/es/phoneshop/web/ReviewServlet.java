package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Feedback;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ReviewServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getPathInfo();

        Optional<Product> optionalProduct = productService.getProduct(Long.valueOf(productId.substring(1)));

        optionalProduct
                .map(p -> setProductAttribute(p, request))
                .orElseThrow(ProductNotFoundException::new);

        String userReview = request.getParameter("userReview");
        String ratingStr = request.getParameter("rating");

        Feedback feedback = new Feedback(userReview, Double.parseDouble(ratingStr));

        optionalProduct.ifPresent(product -> {
            productService.addFeedback(product, feedback);
        });

        optionalProduct
                .map(product -> setProductAttribute(product, request))
                .orElseThrow(ProductNotFoundException::new);

        response.sendRedirect(request.getContextPath() + "/products" + productId + "?message=Review added successfully");
    }

    private Product setProductAttribute(Product product, HttpServletRequest request) {
        request.setAttribute("product", product);
        return product;
    }
}
