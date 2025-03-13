package com.es.phoneshop.service;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private static final ProductServiceImpl PRODUCT_SERVICE_IMPL = new ProductServiceImpl();
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = ArrayListProductDao.getArrayListProductDao();
    }

    public synchronized static ProductServiceImpl getArrayListProductDao() {
        return PRODUCT_SERVICE_IMPL;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productDao.getProduct(id);
    }

    @Override
    public List<Product> findProducts(String query) {
        if (query == null || query.isEmpty()) {
            return productDao.findProducts(query);
        }

        String[] splitQuery = query.split(" ");
        return productDao.findProducts(query).stream()
                .filter(product -> Arrays.stream(splitQuery)
                        .allMatch(word -> product.getDescription().toLowerCase().contains(word.toLowerCase())))
                .sorted((p1, p2) -> {
                    int relevance1 = getRelevance(p1, splitQuery);
                    int relevance2 = getRelevance(p2, splitQuery);
                    return Integer.compare(relevance1, relevance2);
                }).collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public void delete(Long id) {
        productDao.delete(id);
    }

    private int getRelevance(Product product, String[] splitQuery) {
        String productDescription = product.getDescription().toLowerCase();
        int descriptionLength = productDescription.split(" ").length;
        int relevance = 0;

        for (String word : splitQuery) {
            if (productDescription.contains(word.toLowerCase()))
                relevance++;
        }
        return descriptionLength - relevance;
    }
}
