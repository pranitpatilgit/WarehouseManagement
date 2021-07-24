package com.pranitpatil.controller;

import com.pranitpatil.dto.Article;
import com.pranitpatil.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "rest/article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {

    private InventoryService inventoryService;

    @Autowired
    public ArticleController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets the details of the article")
    public Article getArticleById(@PathVariable String id){
        return inventoryService.getArticleById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates and existing article")
    public Article updateArticle(@RequestBody Article article){
        return inventoryService.updateArticle(article);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a new article and returns it.")
    public Article addArticle(@RequestBody Article article){
        return inventoryService.saveArticle(article);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes the article")
    public void removeArticle(@PathVariable String id){
        inventoryService.deleteArticle(id);
    }
}
