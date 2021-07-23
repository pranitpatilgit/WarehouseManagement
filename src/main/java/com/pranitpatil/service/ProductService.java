package com.pranitpatil.service;

import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.Product;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ProductService {

    Product getProductById(long id);

    void loadProductsFromFile(String path) throws IOException;

    PagedResponse<AvailableProduct> getAllAvailableProducts(Pageable pageable);

    Product saveProduct(Product product);

    void sellProduct(long productId, int quantity);
}
