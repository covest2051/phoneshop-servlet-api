package com.es.phoneshop.service.productService;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProduct(Long id);

    List<Product> findProducts(String query, String sortFieldStr, String sortOrderStr);

    void save(Product product);

    void saveAll(List<Product> products);

    void delete(Long id);
}
