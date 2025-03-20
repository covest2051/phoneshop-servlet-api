package com.es.phoneshop.service.productService;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.util.ProductUtils;
import com.es.phoneshop.model.product.SearchResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.es.phoneshop.util.ProductUtils.findQueryAndDescriptionMatch;

public class ProductServiceImpl implements ProductService {
    private static final ProductServiceImpl PRODUCT_SERVICE_IMPL = new ProductServiceImpl();
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = ArrayListProductDao.getArrayListProductDao();
    }

    public synchronized static ProductServiceImpl getProductService() {
        return PRODUCT_SERVICE_IMPL;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productDao.getProduct(id);
    }

    @Override
    public List<Product> findProducts(String query, String sortFieldStr, String sortOrderStr) {
        SortField sortField = Optional.ofNullable(sortFieldStr)
                .map(String::toUpperCase)
                .map(SortField::valueOf)
                .orElse(null);
        SortOrder sortOrder = Optional.ofNullable(sortOrderStr)
                .map(String::toUpperCase)
                .map(SortOrder::valueOf)
                .orElse(null);

        List<Product> products = productDao.findProducts(query, sortField, sortOrder);
        if(query != null && !query.isEmpty()) {
            products = products.stream()
                    .filter(product -> findQueryAndDescriptionMatch(query, product))
                    .toList();
        }

        if(query != null && !query.isEmpty() && sortField == null && sortOrderStr == null) {
            String[] splitQuery = query.split(" ");
            return productDao.findProducts(query, null, null).stream()
                    .map(product -> ProductUtils.createSearchResult(product, splitQuery))
                    .filter(searchResult -> searchResult.getRelevance() > 0)
                    .sorted(Comparator.comparingInt(SearchResult::getRelevance).reversed())
                    .map(SearchResult::getProduct)
                    .toList();
        }
        return products;
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public void saveAll(List<Product> products) {
        productDao.saveAll(products);
    }

    @Override
    public void delete(Long id) {
        productDao.delete(id);
    }
}
