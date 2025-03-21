package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

public interface ViewHistoryService {
    void viewHistory(Product product, HttpServletRequest request);
}
