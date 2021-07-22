package com.pranitpatil.controller;

import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "rest/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("availableProducts")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableProduct> getAllProducts(){

        return productService.getAllAvailableProducts();
    }
}
