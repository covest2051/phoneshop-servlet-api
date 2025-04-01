package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import com.es.phoneshop.service.product.ViewHistoryServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductService productService;
    private CartService cartService;
    private ViewHistoryServiceImpl viewHistoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getProductService();
        cartService = CartServiceImpl.getCartService();
        viewHistoryService = ViewHistoryServiceImpl.getViewHistoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        Optional<Product> optionalProduct = productService.getProduct(Long.valueOf(productId.substring(1)));

        optionalProduct
                .map(p -> setProductAttribute(p, request))
                .orElseThrow(ProductNotFoundException::new);

        request.setAttribute("cart", cartService.getCart(request));

        viewHistoryService.viewHistory(optionalProduct.get(), request);

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getPathInfo();

        Optional<Product> optionalProduct = productService.getProduct(Long.valueOf(productId.substring(1)));

        optionalProduct
                .map(p -> setProductAttribute(p, request))
                .orElseThrow(ProductNotFoundException::new);
        String quantityStr = request.getParameter("quantity");
        int quantity;
        Cart cart = cartService.getCart(request);
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityStr).intValue();
            if (quantity <= 0) {
                throw new IllegalArgumentException();
            }
            cartService.add(cart, Long.valueOf(productId.substring(1)), quantity);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (OutOfStockException e) {
            int productQuantityInCart = cartService.getProductQuantityInCart(cart, optionalProduct.get().getId());
            response.sendRedirect(request.getContextPath() + "/products" + productId + "?error=Not enough stock, available to buy: " + (e.getStockAvailable() - productQuantityInCart));
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products" + productId + "?message=Product added to cart");
    }

    private Product setProductAttribute(Product product, HttpServletRequest request) {
        request.setAttribute("product", product);
        return product;
    }
}
