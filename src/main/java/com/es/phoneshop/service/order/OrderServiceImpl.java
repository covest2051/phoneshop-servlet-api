package com.es.phoneshop.service.order;

import com.es.phoneshop.dao.order.ArrayListOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    private OrderDao orderDao;

    public synchronized static OrderServiceImpl getInstance() {
        return INSTANCE;
    }

    public OrderServiceImpl() {
        this.orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream()
                .map(CartItem::clone)
                .toList());

        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        order.setTotalQuantity(totalQuantity);

        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return List.of(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(String.valueOf(UUID.randomUUID()));
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}
