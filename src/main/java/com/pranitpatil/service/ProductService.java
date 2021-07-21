package com.pranitpatil.service;

import com.pranitpatil.dto.Product;

import java.io.IOException;

public interface ProductService {

    Product getProductById(long id);

    void loadProductsFromFile(String path) throws IOException;
}
