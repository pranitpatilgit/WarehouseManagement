package com.pranitpatil.service;

import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product getProductById(long id);

    void loadProductsFromFile(String path) throws IOException;

    List<AvailableProduct> getAllAvailableProducts();
}
