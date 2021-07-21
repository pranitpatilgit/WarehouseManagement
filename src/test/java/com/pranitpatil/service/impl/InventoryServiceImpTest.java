package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.dao.ArticleRepository;
import com.pranitpatil.entity.Article;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryServiceImpTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private WarehouseManagementProperties warehouseManagementProperties;

    private InventoryServiceImpl inventoryService;

    @Before
    public void before(){
        inventoryService = new InventoryServiceImpl(objectMapper, articleRepository, modelMapper, warehouseManagementProperties);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void givenArticleId_whenFindArticleById_thenGetArticle(){

        Article articleEntity = new Article();
        articleEntity.setId("1");
        articleEntity.setName("Art1");
        articleEntity.setStock(1);

        when(articleRepository.findById("1")).thenReturn(Optional.of(articleEntity));

        com.pranitpatil.dto.Article articleDto = inventoryService.getArticleById("1");

        Assert.assertEquals(articleDto.getId(), articleEntity.getId());
        Assert.assertEquals(articleDto.getName(), articleEntity.getName());
        Assert.assertEquals(articleDto.getStock(), articleEntity.getStock());
    }


    //TODO
    /*@Test
    public void givenArticleId_whenFindArticleById_thenThrowNotFoundException(){

        Article articleEntity = new Article();
        articleEntity.setId("1");
        articleEntity.setName("Art1");
        articleEntity.setStock(1);

        when(articleRepository.findById("1")).thenReturn(null);

        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage("Article with id - 1 is not found.");
    }*/
}
