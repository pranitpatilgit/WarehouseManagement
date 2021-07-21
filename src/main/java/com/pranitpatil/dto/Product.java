package com.pranitpatil.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Product {

    private long id;
    private String name;
    private double price;

    @JsonProperty("contain_articles")
    private List<ArticleQuantity> articleQuantities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ArticleQuantity> getArticleQuantities() {
        return articleQuantities;
    }

    public void setArticleQuantities(List<ArticleQuantity> articleQuantities) {
        this.articleQuantities = articleQuantities;
    }
}
