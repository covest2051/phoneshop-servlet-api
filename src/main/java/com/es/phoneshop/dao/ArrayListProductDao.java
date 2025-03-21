package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListProductDao implements ProductDao {
    private static final ArrayListProductDao ARRAY_LIST_PRODUCT_DAO = new ArrayListProductDao();

    private AtomicLong productId = new AtomicLong(1);
    private List<Product> products;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public synchronized static ArrayListProductDao getArrayListProductDao() {
        return ARRAY_LIST_PRODUCT_DAO;
    }

    private final Map<SortField, Comparator<Product>> comparators = Map.of(
            SortField.DESCRIPTION, Comparator.comparing(Product::getDescription),
            SortField.PRICE, Comparator.comparing(Product::getPrice)
    );


    @Override
    public Optional<Product> getProduct(Long id) {
        lock.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .map(Product::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator;
            if (sortField == null) {
                comparator = Comparator.comparing(Product::getDescription);
            } else {
                comparator = comparators.get(sortField);
            }
            if (sortOrder == SortOrder.DESC) {
                comparator = comparator.reversed();
            }

            return products.stream()
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
    public void save(Product product) {
        lock.writeLock().lock();
        try {
            if (product.getId() != null) {
                Optional<Product> optionalProductForUpdate = getProduct(product.getId());
                if (optionalProductForUpdate.isPresent()) {
                    Product productForUpdate = optionalProductForUpdate.get();
                    if (!product.getPrice().equals(productForUpdate.getPrice())) {
                        productForUpdate.getPriceHistory().add(new PriceHistory(new Date(), product.getPrice()));
                    }
                    productForUpdate.setCode(product.getCode());
                    productForUpdate.setDescription(product.getDescription());
                    productForUpdate.setPrice(product.getPrice());
                    productForUpdate.setCurrency(product.getCurrency());
                    productForUpdate.setStock(product.getStock());
                    productForUpdate.setImageUrl(product.getImageUrl());
                } else
                    throw new NoSuchElementException("Product with ID " + product.getId() + " not found.");
            } else {
                product.setId(productId.incrementAndGet());
                products.add(product);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void saveAll(List<Product> products) {
        products.forEach(this::save);
    }


    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        try {
            Optional<Product> productForDelete = getProduct(id);
            productForDelete.ifPresent(products::remove);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
