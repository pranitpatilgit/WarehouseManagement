package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.dao.ArticleRepository;
import com.pranitpatil.dto.Article;
import com.pranitpatil.dto.Inventory;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private ObjectMapper objectMapper;
    private ArticleRepository articleRepository;
    private ModelMapper modelMapper;
    private WarehouseManagementProperties warehouseManagementProperties;

    @Autowired
    public InventoryServiceImpl(ObjectMapper objectMapper, ArticleRepository articleRepository,
                                ModelMapper modelMapper, WarehouseManagementProperties warehouseManagementProperties) {
        this.objectMapper = objectMapper;
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.warehouseManagementProperties = warehouseManagementProperties;
    }

    @PostConstruct
    void loadArticles() throws IOException{
        loadArticlesFromFile(warehouseManagementProperties.getInventory().getPath());

        logger.debug(this.getArticleById("1").toString());
    }

    @Override
    public Article getArticleById(String id) {
        com.pranitpatil.entity.Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("Article with id - " + id + " is not found."));

        return modelMapper.map(article, Article.class);
    }

    @Override
    public void loadArticlesFromFile(String path) throws IOException {
        Inventory inventory = objectMapper.readValue(new File(path), Inventory.class);

        List<com.pranitpatil.entity.Article> articles = inventory.getInventory()
                .stream()
                .map(article -> modelMapper.map(article, com.pranitpatil.entity.Article.class))
                .collect(Collectors.toList());

        articleRepository.saveAll(articles);
    }

}
