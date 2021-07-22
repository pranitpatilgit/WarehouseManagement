package com.pranitpatil.controller;

import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.Product;
import com.pranitpatil.exception.ValidationException;
import com.pranitpatil.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "rest/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

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

    @PostMapping("sell/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void sellProduct(@PathVariable long productId,
                            @RequestParam(required = false, defaultValue = "1") int quantity){

        if(quantity == 0){
            throw new ValidationException("Sold quantity cannot be 0.");
        }

        logger.info("Sold {} numbers of product {}.", quantity, productId);
    }

    @GetMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable long productId){
        return productService.getProductById(productId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Product addNewProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }
}
