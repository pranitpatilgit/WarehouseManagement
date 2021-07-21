package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.dao.ProductRepository;
import com.pranitpatil.dto.Product;
import com.pranitpatil.dto.Products;
import com.pranitpatil.entity.ArticleQuantity;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ObjectMapper objectMapper;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private WarehouseManagementProperties warehouseManagementProperties;

    @Autowired
    public ProductServiceImpl(ObjectMapper objectMapper, ProductRepository productRepository,
                              ModelMapper modelMapper, WarehouseManagementProperties warehouseManagementProperties) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.warehouseManagementProperties = warehouseManagementProperties;
    }

    @PostConstruct
    void loadProducts() throws IOException{
        loadProductsFromFile(warehouseManagementProperties.getProduct().getPath());
    }

    @Override
    public Product getProductById(long id) {
        com.pranitpatil.entity.Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product with id - " + id + " is not found."));

        return modelMapper.map(product, Product.class);
    }

    @Override
    public void loadProductsFromFile(String path) throws IOException {
        Products products = objectMapper.readValue(new File(path), Products.class);

        List<com.pranitpatil.entity.Product> productEntities = products.getProducts()
                .stream()
                .map(p -> getProduct(p))
                .collect(Collectors.toList());

        productRepository.saveAll(productEntities);
    }

    private com.pranitpatil.entity.Product getProduct(Product p){

        com.pranitpatil.entity.Product product =  modelMapper.map(p, com.pranitpatil.entity.Product.class);

        List<ArticleQuantity> articleQuantities = new ArrayList<>();

        for (com.pranitpatil.dto.ArticleQuantity aq: p.getArticleQuantities()){
            articleQuantities.add(new ArticleQuantity(product, aq.getArticleId(), aq.getQuantity()));
        }

        product.setArticles(articleQuantities);

        return product;
    }

}
