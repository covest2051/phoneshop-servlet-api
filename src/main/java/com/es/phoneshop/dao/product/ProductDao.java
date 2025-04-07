package com.es.phoneshop.dao.product;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends GenericDao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void delete(Long id);
}
