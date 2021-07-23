package com.pranitpatil.controller.IT;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.dto.ArticleQuantity;
import com.pranitpatil.dto.AvailableProduct;
import com.pranitpatil.dto.Product;
import org.junit.Assert;
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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationTest.yml")
@DirtiesContext
public class ProductControllerAvailabilityIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenAddNewProduct_whenGetAvailableProducts_thenStatus200andNewlyAvailableProduct() throws Exception {
        Product product = new Product();
        product.setName("New");
        ArticleQuantity articleQuantity = new ArticleQuantity();
        articleQuantity.setArticleId("1");
        articleQuantity.setQuantity(1);
        product.setArticleQuantities(Arrays.asList(articleQuantity));

        mvc.perform(MockMvcRequestBuilders.post("/rest/product")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New"));

        verifyProductQuantity("New", 12);
    }

    @Test
    public void givenProductIdAndQuantity_whenSellProduct_thenStatus200AndReducedQuantity() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/rest/product/sell/1?quantity=1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verifyProductQuantity("Dining Chair", 1);
        verifyProductQuantity("Dinning Table", 1);
        verifyProductQuantity("New", 8);
    }

    private void verifyProductQuantity(String productName, int quantity) throws Exception {
        List<AvailableProduct> availableProducts = objectMapper.readValue(mvc.perform(MockMvcRequestBuilders.get("/rest/product/availableProducts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<AvailableProduct>>() {});

        boolean found = false;
        for (AvailableProduct p : availableProducts) {
            if (p.getName().equals(productName)) {
                found = true;
                Assert.assertEquals(p.getAvailableQuantity(), quantity);
            }
        }
        Assert.assertTrue("Product not found", found);
    }
}
