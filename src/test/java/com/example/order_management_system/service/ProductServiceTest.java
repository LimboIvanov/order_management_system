package com.example.order_management_system.service;

import com.example.order_management_system.model.Product;
import com.example.order_management_system.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    public void testSaveProduct() {
        Product productToSave = Product.builder()
                .name("Sample Product")
                .skuCode("SkuCode123")
                .unitPrice(BigDecimal.valueOf(19.99))
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Sample Product")
                .skuCode("SkuCode123")
                .unitPrice(BigDecimal.valueOf(19.99))
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product returnedProduct = productService.saveProduct(productToSave);

        verify(productRepository, times(1)).save(productToSave);

        assertNotNull(returnedProduct);
        assertEquals(1L, returnedProduct.getId());
        assertEquals("Sample Product", returnedProduct.getName());
        assertEquals("SkuCode123", returnedProduct.getSkuCode());
        assertEquals(BigDecimal.valueOf(19.99), returnedProduct.getUnitPrice());
    }
}