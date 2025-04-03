package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.OrderServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getCartService();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();

        setRequiredParameterForString(request, "firstName", errors, order::setFirstName);
        setRequiredParameterForString(request, "lastName", errors, order::setLastName);
        setRequiredParameterForString(request, "phone", errors, order::setPhone);
        setDeliveryDate(request, errors, order);
        setRequiredParameterForString(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order);

        handleErrors(request, response, errors, order);
    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors, Order order) throws ServletException, IOException {
        if(errors.isEmpty()) {
            orderService.placeOrder(order);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredParameterForString(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Input should not be empty");
        } else {
            consumer.accept(value);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("deliveryDate");

        if (value == null || value.isEmpty()) {
            errors.put("deliveryDate", "Input should not be empty");
        } else {
            order.setDeliveryDate(LocalDate.parse(value));
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");

        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", "Input should not be empty");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
