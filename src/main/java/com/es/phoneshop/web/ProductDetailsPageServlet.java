package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.cartService.CartService;
import com.es.phoneshop.service.cartService.CartServiceImpl;
import com.es.phoneshop.service.productService.ProductService;
import com.es.phoneshop.service.productService.ProductServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductService productService;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getProductService();
        cartService = CartServiceImpl.getCartService();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        String quantity = request.getParameter("quantity");

        request.setAttribute("cart", cartService.getCart());

        cartService.add(Long.valueOf(productId.substring(1)), Integer.parseInt(quantity));

        doGet(request, response);
    }

    private Product setProductAttribute(Product product, HttpServletRequest request) {
        request.setAttribute("product", product);
        return product;
    }
}
