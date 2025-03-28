package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.Copyable;
import com.es.phoneshop.model.GenericEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Product extends GenericEntity implements Serializable, Copyable<Product> {
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> priceHistory;
    private List<Feedback> feedbackList;
    private double rating;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this();
        this.setId(id);
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(new PriceHistory(new Date(), price));
        feedbackList = new ArrayList<>();
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this();
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(new PriceHistory(new Date(), price));
        feedbackList = new ArrayList<>();
    }

    // c-tor копирования
    public Product(Product other) {
        this.setId(other.getId());
        this.code = other.code;
        this.description = other.description;
        this.price = other.price;
        this.currency = other.currency;
        this.stock = other.stock;
        this.imageUrl = other.imageUrl;
        this.priceHistory = other.priceHistory;
        this.feedbackList = other.feedbackList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceHistory> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistory> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) &&
                Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), code);
    }

    @Override
    public Product copy() {
        return new Product(this);
    }
}