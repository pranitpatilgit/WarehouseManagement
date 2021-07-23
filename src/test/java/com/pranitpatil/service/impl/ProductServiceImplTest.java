package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.dao.ArticleRepository;
import com.pranitpatil.dao.ProductRepository;
import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.entity.Article;
import com.pranitpatil.entity.ArticleQuantity;
import com.pranitpatil.entity.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private WarehouseManagementProperties warehouseManagementProperties;

    private ProductServiceImpl productService;

    @Before
    public void before(){
        productService = new ProductServiceImpl(objectMapper, productRepository, modelMapper,
                warehouseManagementProperties, articleRepository);
    }


    @Test
    public void givenProductId_whenFindProductById_thenGetProduct(){

        Product productEntity = new Product();
        productEntity.setId(1L);
        productEntity.setName("P1");
        productEntity.setPrice(1.01);

        ArticleQuantity articleQuantity = new ArticleQuantity(productEntity, "1", 10);
        productEntity.setArticles(Arrays.asList(articleQuantity));

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        com.pranitpatil.dto.Product productDto = productService.getProductById(1);

        Assert.assertEquals(productDto.getId(), productEntity.getId());
        Assert.assertEquals(productDto.getName(), productEntity.getName());
        Assert.assertEquals(productDto.getPrice(), productEntity.getPrice(), 0);
        Assert.assertEquals(productDto.getArticleQuantities().get(0).getArticleId(), articleQuantity.getArticleid());
    }

    @Test
    public void givenProducts_whenGetAllAvailableProducts_thenGetAvailableProducts(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1);

        List<ArticleQuantity> quantities = new ArrayList<>();
        quantities.add(new ArticleQuantity(product, "1", 2));
        quantities.add(new ArticleQuantity(product, "2", 3));

        product.setArticles(quantities);
        products.add(product);

        Article article = new Article();
        article.setStock(4);

        when(productRepository.findAll()).thenReturn(products);
        when(articleRepository.findById(anyString())).thenReturn(Optional.of(article));

        List<AvailableProduct> availableProducts = productService.getAllAvailableProducts();

        Assert.assertEquals(1, availableProducts.get(0).getAvailableQuantity());
    }

    @Test
    public void givenProducts_whenGetAllAvailableProducts_thenGetAvailableProductsWithZeroQuatity(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1);

        List<ArticleQuantity> quantities = new ArrayList<>();
        quantities.add(new ArticleQuantity(product, "1", 2));
        quantities.add(new ArticleQuantity(product, "2", 3));

        product.setArticles(quantities);
        products.add(product);

        Article article = new Article();
        article.setStock(2);

        when(productRepository.findAll()).thenReturn(products);
        when(articleRepository.findById(anyString())).thenReturn(Optional.of(article));

        List<AvailableProduct> availableProducts = productService.getAllAvailableProducts();

        Assert.assertEquals(0, availableProducts.get(0).getAvailableQuantity());
    }
}
