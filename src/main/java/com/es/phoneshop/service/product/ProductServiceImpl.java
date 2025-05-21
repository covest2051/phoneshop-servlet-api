package com.es.phoneshop.service.product;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SearchResult;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.util.ProductUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.es.phoneshop.util.ProductUtils.findQueryAndDescriptionAllMatch;
import static com.es.phoneshop.util.ProductUtils.findQueryAndDescriptionAnyMatch;

public class ProductServiceImpl implements ProductService {
    private static final ProductServiceImpl INSTANCE = new ProductServiceImpl();
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    public synchronized static ProductServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productDao.getById(id);
    }

    private final Map<SortField, Comparator<Product>> comparators = Map.of(
            SortField.DESCRIPTION, Comparator.comparing(Product::getDescription),
            SortField.PRICE, Comparator.comparing(Product::getPrice),
            SortField.DEFAULT, Comparator.comparing(Product::getId)
    );

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
        if (query != null && !query.isEmpty()) {
            products = products.stream()
                    .filter(product -> findQueryAndDescriptionAllMatch(query, product))
                    .toList();
        }

        if (query != null && !query.isEmpty() && sortField == null && sortOrderStr == null) {
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
    public List<Product> advancedFindProducts(String query, String match, String minPrice, String maxPrice, String sortFieldStr, String sortOrderStr) {
        SortField sortField = Optional.ofNullable(sortFieldStr)
                .map(String::toUpperCase)
                .map(SortField::valueOf)
                .orElse(null);
        SortOrder sortOrder = Optional.ofNullable(sortOrderStr)
                .map(String::toUpperCase)
                .map(SortOrder::valueOf)
                .orElse(null);

        List<Product> products = productDao.findProducts(query, sortField, sortOrder);

        if (query != null && !query.isEmpty()) {
            String[] splitQuery = query.split(" ");
            products = products.stream()
                    .filter(product -> {
                        if("all".equalsIgnoreCase(match)) {
                            return findQueryAndDescriptionAllMatch(query, product);
                        } else {
                            return findQueryAndDescriptionAnyMatch(query, product);
                        }
                    })
                    .map(product -> ProductUtils.createSearchResult(product, splitQuery))
                    .filter(searchResult -> searchResult.getRelevance() > 0)
                    .sorted(Comparator.comparingInt(SearchResult::getRelevance).reversed())
                    .map(SearchResult::getProduct)
                    .toList();
        } else {
            /* Сортировка работает только если нет поиска по Description */
            if (sortField != null && sortOrder != null) {
                Comparator<Product> comparator = Optional.ofNullable(comparators.get(sortField))
                        .orElse( comparators.get(SortField.DEFAULT));
                if (comparator != null) {
                    if (sortOrder == SortOrder.DESC) {
                        comparator = comparator.reversed();
                    }
                    products = products.stream()
                            .sorted(comparator)
                            .toList();
                }
            }
        }

        if (minPrice != null && !minPrice.isEmpty()) {
            BigDecimal bigDecimalMinPrice = new BigDecimal(minPrice);
            products = products.stream()
                    .filter(product -> product.getPrice().compareTo(bigDecimalMinPrice) >= 0)
                    .toList();
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            BigDecimal bigDecimalMaxPrice = new BigDecimal(maxPrice);
            products = products.stream()
                    .filter(product -> product.getPrice().compareTo(bigDecimalMaxPrice) <= 0)
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
        products.forEach(productDao::save);
    }

    @Override
    public void delete(Long id) {
        productDao.delete(id);
    }
}
