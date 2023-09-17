package com.example.order_management_system.controller;

import com.example.order_management_system.model.Product;
import com.example.order_management_system.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest({ProductController.class})
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveProduct() throws Exception {
        Product savedProduct = Product.builder()
                .id(1L)
                .name("Sample Product")
                .skuCode("SkuCode123")
                .unitPrice(BigDecimal.valueOf(19.99))
                .build();

        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sample Product\",\"skuCode\":\"SkuCode123\",\"unitPrice\":19.99}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sample Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skuCode").value("SkuCode123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitPrice").value(19.99));
    }


}



