package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getCartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Map<Long, String> errors = (Map<Long, String>) session.getAttribute("errors");

        if (errors != null) {
            request.setAttribute("errors", errors);
            session.removeAttribute("errors");
        }
        setCartAttribute(request);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            try {
                int quantity = getQuantity(quantities[i], request);
                if (quantity <= 0) {
                    errors.put(productId, "Can't be negative or zero");
                    continue;
                }
                cartService.update(cart, productId, quantity);
            } catch (ParseException exception) {
                handleFailure(errors, productId, "Not a number");
            } catch (OutOfStockException exception) {
                if (exception.getStockRequested() <= 0) {
                    handleFailure(errors, productId, "Can't be negative or zero");
                } else {
                    handleFailure(errors, productId, "Out of stock, max available in your cart: " + exception.getStockAvailable());
                }
            }
        }

        HttpSession session = request.getSession();
        if (errors.isEmpty()) {
            session.setAttribute("message", "Cart updated successfully");
        } else {
            session.setAttribute("errors", errors);
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cart cart = cartService.getCart(request);
        String productId = request.getParameter("productId");

        cartService.delete(cart, Long.valueOf(productId));

        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }

    private void handleFailure(Map<Long, String> errors, Long productId, String message) {
        errors.put(productId, message);
    }

    private void setCartAttribute(HttpServletRequest request) {
        request.setAttribute("cart", cartService.getCart(request));
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }
}
