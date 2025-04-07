package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CartServiceImpl;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.OrderServiceImpl;
import com.es.phoneshop.util.OrderUtils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
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

        validateStringParameter(request, "firstName", OrderUtils.nameAndSurnameRegex)
                .ifPresentOrElse(error -> errors.put("firstName", error),
                        () -> order.setFirstName(request.getParameter("firstName")));

        validateStringParameter(request, "lastName", OrderUtils.nameAndSurnameRegex)
                .ifPresentOrElse(
                        error -> errors.put("lastName", error),
                        () -> order.setLastName(request.getParameter("lastName"))
                );

        validateStringParameter(request, "phone", OrderUtils.phoneRegex)
                .ifPresentOrElse(
                        error -> errors.put("phone", error),
                        () -> order.setPhone(request.getParameter("phone"))
                );

        validateStringParameter(request, "deliveryAddress", OrderUtils.deliveryAddressRegex)
                .ifPresentOrElse(
                        error -> errors.put("deliveryAddress", error),
                        () -> order.setDeliveryAddress(request.getParameter("deliveryAddress"))
                );
        setDeliveryDate(request)
                .ifPresentOrElse(
                        error -> errors.put("deliveryDate", error),
                        () -> order.setDeliveryDate(LocalDate.parse(request.getParameter("deliveryDate")))
                );
        setPaymentMethod(request)
                .ifPresentOrElse(
                        error -> errors.put("paymentMethod", error),
                        () -> order.setPaymentMethod(PaymentMethod.valueOf(request.getParameter("paymentMethod")))
                );

        handleResults(request, response, errors, order);
    }

    private void handleResults(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors, Order order) throws ServletException, IOException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    public Optional<String> validateStringParameter(HttpServletRequest request, String parameterName, String regex) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isEmpty()) {
            return Optional.of("Field \"" + parameterName + "\" should not be empty");
        }

        if (!OrderUtils.validateString(value, regex)) {
            return Optional.of("Field \"" + parameterName + "\" does not match the required format");
        }
        return Optional.empty();
    }

    private Optional<String> setDeliveryDate(HttpServletRequest request) {
        String value = request.getParameter("deliveryDate");

        if (value == null || value.isEmpty()) {
            return Optional.of("Field \"deliveryDate\" should not be empty");
        }
        return Optional.empty();
    }

    private Optional<String> setPaymentMethod(HttpServletRequest request) {
        String value = request.getParameter("paymentMethod");

        if (value == null || value.isEmpty()) {
            return Optional.of("Field \"paymentMethod\" should not be empty");
        }
        return Optional.empty();
    }
}
