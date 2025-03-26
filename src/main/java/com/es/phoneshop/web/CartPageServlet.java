package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getCartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCartAttribute(request);

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            try {
                quantity = getQuantity(quantities[i], request);
                cartService.update(cart, productId, quantity);
            } catch (ParseException | OutOfStockException exception) {
                handleFailure(errors, productId, exception);
            }
        }

        if(errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart  updated successfully");
        } else {

            request.setAttribute("errors", errors);
            setCartAttribute(request);

            request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
        }
    }

    private void handleFailure(Map<Long, String> errors, Long productId, Exception exception) {
        if(exception.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if(((OutOfStockException)exception).getStockRequested() <= 0) {
                errors.put(productId, "Can`t be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available in your cart: " + ((OutOfStockException) exception).getStockAvailable());
            }
        }
    }

    private void setCartAttribute(HttpServletRequest request) {
        request.setAttribute("cart", cartService.getCart(request));
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }


}
