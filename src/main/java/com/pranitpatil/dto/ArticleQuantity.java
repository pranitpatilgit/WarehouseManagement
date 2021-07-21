package com.pranitpatil.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleQuantity {

    @JsonProperty("art_id")
    private String articleId;

    @JsonProperty("amount_of")
    private int quantity;


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
