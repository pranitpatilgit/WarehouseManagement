package com.pranitpatil.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.dto.ArticleQuantity;
import com.pranitpatil.dto.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationTest.yml")
@DirtiesContext
public class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEndpoint_whenGetAvailableProducts_thenStatus200andAvailableProducts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/rest/product/availableProducts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entity[0].name").value("Dining Chair"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entity[0].availableQuantity").value(2));
    }

    @Test
    public void givenProductId_whenGetProduct_thenStatus200andProduct() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/rest/product/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dining Chair"));
    }

    @Test
    public void givenProduct_whenAddNewProduct_thenStatus201andProduct() throws Exception {
        Product product = new Product();
        product.setName("New");
        ArticleQuantity articleQuantity = new ArticleQuantity();
        articleQuantity.setArticleId("1");
        articleQuantity.setQuantity(1);
        product.setArticleQuantities(Arrays.asList(articleQuantity));

        Product createdProduct = objectMapper.readValue(mvc.perform(MockMvcRequestBuilders.post("/rest/product")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New"))
                .andReturn().getResponse().getContentAsString(), Product.class);

        //Verify from REST
        mvc.perform(MockMvcRequestBuilders.get("/rest/product/" + createdProduct.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New"));
    }
}
