package com.es.phoneshop.model.product;

public class Feedback {
    private double rating;

    private String text;

    public Feedback(String text, double rating) {
        this.text = text;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
