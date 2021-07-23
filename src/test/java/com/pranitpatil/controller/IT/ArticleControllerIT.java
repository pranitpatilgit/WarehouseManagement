package com.pranitpatil.controller.IT;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.dto.Article;
import com.pranitpatil.dto.AvailableProduct;
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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationTest.yml")
@DirtiesContext
public class ArticleControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void givenIncreaseArticleStock_whenGetAvailableProduct_thenStatus200AndIncreasedQuantity() throws Exception {

        verifyProductQuantity("Dining Chair", 2);
        verifyProductQuantity("Dinning Table", 1);

        Article article1 = new Article();
        article1.setId("2");
        article1.setName("screw");
        article1.setStock(40);

        Article article2 = new Article();
        article2.setId("3");
        article2.setName("seat");
        article2.setStock(10);

        mvc.perform(MockMvcRequestBuilders.put("/rest/article")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(article1)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mvc.perform(MockMvcRequestBuilders.put("/rest/article")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(article2)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verifyProductQuantity("Dining Chair", 3);
        verifyProductQuantity("Dinning Table", 1);
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
