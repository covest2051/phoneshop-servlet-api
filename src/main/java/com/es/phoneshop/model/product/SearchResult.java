package com.es.phoneshop.model.product;

public class SearchResult {
    private Product product;
    private int relevance;

    public SearchResult(Product product, int relevance) {
        this.product = product;
        this.relevance = relevance;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }
}
