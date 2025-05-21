package com.es.phoneshop.web;

import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import com.es.phoneshop.util.ValidationUtils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        String query = request.getParameter("query");
        String match = request.getParameter("match");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        if (minPrice != null && !minPrice.isEmpty()) {
            Optional<String> minPriceError = validatePriceParameter(minPrice, ValidationUtils.PRICE_REGEX);
            minPriceError.ifPresent(error -> errors.put("minPrice", error));
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            Optional<String> maxPriceError = validatePriceParameter(maxPrice, ValidationUtils.PRICE_REGEX);
            maxPriceError.ifPresent(error -> errors.put("maxPrice", error));
        }

        if(!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("query", query);
            request.setAttribute("match", match);
            request.setAttribute("minPrice", minPrice);
            request.setAttribute("maxPrice", maxPrice);

            request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
            return;
        }

        request.setAttribute("products", productService.advancedFindProducts(query, match, minPrice, maxPrice, sortField, sortOrder));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    public Optional<String> validatePriceParameter(String priceValue, String regex) {
        if (!ValidationUtils.validatePrice(priceValue, regex)) {
            return Optional.of("Field \"" + priceValue + "\" is not a number");
        }
        return Optional.empty();
    }
}
