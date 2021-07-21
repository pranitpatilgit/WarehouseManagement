package com.pranitpatil.service;

import com.pranitpatil.dto.Article;

import java.io.IOException;

public interface InventoryService {

    Article getArticleById(String id);

    void loadArticlesFromFile(String path) throws IOException;

}
