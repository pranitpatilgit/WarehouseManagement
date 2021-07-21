package com.pranitpatil.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.config.WarehouseManagementProperties;
import com.pranitpatil.dao.ProductRepository;
import com.pranitpatil.entity.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseManagementProperties warehouseManagementProperties;

    private ProductServiceImpl productService;

    @Before
    public void before(){
        productService = new ProductServiceImpl(objectMapper, productRepository, modelMapper, warehouseManagementProperties);
    }


    @Test
    public void givenProductId_whenFindProductById_thenGetProduct(){

        Product productEntity = new Product();
        productEntity.setId(1L);
        productEntity.setName("P1");
        productEntity.setPrice(1.01);

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        com.pranitpatil.dto.Product productDto = productService.getProductById(1);

        Assert.assertEquals(productDto.getId(), productEntity.getId());
        Assert.assertEquals(productDto.getName(), productEntity.getName());
        Assert.assertEquals(productDto.getPrice(), productEntity.getPrice(), 0);
    }
}
