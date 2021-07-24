package com.pranitpatil.controller;

import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.Product;
import com.pranitpatil.exception.ValidationException;
import com.pranitpatil.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
    @Operation(summary = "Get All Available Products\n" +
            "Gets all products with their available quantity.\n" +
            "\n" +
            "This is a API with pagination and sorting,\n" +
            "\n" +
            "Provide page details as query parameters.")
    public PagedResponse<AvailableProduct> getAllProducts(@PageableDefault(page = 0, size = 10)
                   @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return productService.getAllAvailableProducts(pageable);
    }

    @PostMapping("sell/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Registers a sell of product and quantity and reduces the article stock accordingly.")
    public void sellProduct(@PathVariable long productId,
                            @RequestParam(required = false, defaultValue = "1") int quantity) {

        if (quantity == 0) {
            throw new ValidationException("Sold quantity cannot be 0.");
        }

        logger.info("Selling {} numbers of product {}.", quantity, productId);

        productService.sellProduct(productId, quantity);
    }

    @GetMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets the details of the product")
    public Product getProduct(@PathVariable long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a new product and returns it")
    public Product addNewProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}
