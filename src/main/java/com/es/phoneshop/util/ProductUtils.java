package com.es.phoneshop.util;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SearchResult;

import java.util.Arrays;
import java.util.List;

public class ProductUtils {
    public static SearchResult createSearchResult(Product product, String[] splitQuery) {
        int relevance = getRelevance(product, splitQuery);
        return new SearchResult(product, relevance);
    }

    private static int getRelevance(Product product, String[] splitQuery) {
        String productDescription = product.getDescription().toLowerCase();
        int descriptionLength = productDescription.split(" ").length;
        int relevance = 0;

        for (String word : splitQuery) {
            if (productDescription.contains(word.toLowerCase()))
                relevance++;
        }
        return (int) (((double) relevance / descriptionLength) * 100);
    }

    public static boolean findQueryAndDescriptionMatch(String query, Product product) {
        return
        Arrays.stream(query.split(" "))
                .allMatch(word -> product.getDescription().toLowerCase().contains(word.toLowerCase()));
    }
}
