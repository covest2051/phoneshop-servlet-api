package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProduct(Long id);

    List<Product> findProducts(String query);

    void save(Product product);

    void delete(Long id);
}
