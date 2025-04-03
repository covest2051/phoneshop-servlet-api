package com.es.phoneshop.dao.order;

import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private static final ArrayListOrderDao ORDER_DAO = new ArrayListOrderDao();

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }

    public synchronized static ArrayListOrderDao getInstance() {
        return ORDER_DAO;
    }

    private AtomicLong orderId = new AtomicLong(1);
    private List<Order> orders;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Optional<Order> getOrder(Long id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return orders.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .map(Order::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Order order) throws OrderNotFoundException {
        lock.writeLock().lock();
        try {
            if(order.getId() != null) {
                orders.remove(getOrder(order.getId()));
                orders.add(order);
            } else {
                order.setId(orderId.incrementAndGet());
                orders.add(order);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
