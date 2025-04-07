package com.es.phoneshop.dao.product;

import com.es.phoneshop.dao.ArrayListGenericDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ArrayListProductDao extends ArrayListGenericDao<Product> implements ProductDao {
    private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();

    private ArrayListProductDao() {
        super();
    }

    public synchronized static ArrayListProductDao getInstance() {
        return INSTANCE;
    }

    private final Map<SortField, Comparator<Product>> comparators = Map.of(
            SortField.DESCRIPTION, Comparator.comparing(Product::getDescription),
            SortField.PRICE, Comparator.comparing(Product::getPrice),
            SortField.DEFAULT, Comparator.comparing(Product::getId)
    );

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            if (sortField == null) {
                sortField = SortField.DEFAULT;
            }

            Comparator<Product> comparator = Optional.ofNullable(comparators.get(sortField))
                    .orElse( comparators.get(SortField.DEFAULT));

            if (sortOrder == SortOrder.DESC) {
                comparator = comparator.reversed();
            }

            return entities.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .map(Product::new)
                    .sorted(comparator)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            Optional<Product> productForDelete = getById(id);
            productForDelete.ifPresent(entities::remove);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
