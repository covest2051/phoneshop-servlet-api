package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.order.ArrayListOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;
    private OrderService orderService;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Test
    public void testGetOrder() {
        Order order = new Order();
        orderDao.save(order);

        Optional<Order> orderToFind = orderDao.getById(order.getId());

        assertTrue(orderToFind.isPresent());
        assertEquals(order.getId(), orderToFind.get().getId());
    }

    @Test
    public void testGetOrderBySecureId() {
        Order order = new Order();
        orderDao.save(order);

        orderService.placeOrder(order);

        Optional<Order> orderToCompare = orderDao.getOrderBySecureId(order.getSecureId());

        assertTrue(orderToCompare.isPresent());
        assertEquals(order.getId(), orderToCompare.get().getId());
    }
}
