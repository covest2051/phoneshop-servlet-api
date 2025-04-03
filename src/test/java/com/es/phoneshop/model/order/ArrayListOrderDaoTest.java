package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.order.ArrayListOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.service.order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

        Order orderToFind = orderDao.getOrder(order.getId());

        assertEquals(order.getId(), orderToFind.getId());
    }

    @Test
    public void testGetOrderBySecureId() {
        Order order = new Order();
        orderDao.save(order);

        orderService.placeOrder(order);

        Order orderToCompare = orderDao.getOrderBySecureId(order.getSecureId());

        assertEquals(order.getId(), orderToCompare.getId());
    }
}
