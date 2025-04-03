package com.es.phoneshop.dao.order;

import com.es.phoneshop.dao.ArrayListGenericDao;
import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {
    private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();

    private ArrayListOrderDao() {
        super();
    }

    public synchronized static ArrayListOrderDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Order> getOrderBySecureId(String secureId) {
        lock.readLock().lock();
        try {
            return entities.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findFirst()
                    .map(Order::new);
        } finally {
            lock.readLock().unlock();
        }
    }
}
