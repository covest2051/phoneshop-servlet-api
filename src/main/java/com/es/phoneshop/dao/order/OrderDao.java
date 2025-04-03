package com.es.phoneshop.dao.order;

import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> getOrder(Long id) throws OrderNotFoundException;

    void save(Order order) throws OrderNotFoundException;
}
