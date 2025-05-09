package com.springboot.homework.services;

import com.springboot.homework.models.dtos.ProductDTO;
import com.springboot.homework.models.entities.Product;
import com.springboot.homework.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();

        Product savedProduct = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Integer productId = productService.create(productDTO);


        assertThat(productId).isEqualTo(1);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());


        Product capturedProduct = productCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo("Test Product");
        assertThat(capturedProduct.getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void testGetById() {
        Product product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Product");
        assertThat(result.get().getPrice()).isEqualTo(new BigDecimal("10.99"));
        verify(productRepository).findById(1); // Verify repository interaction
    }

    @Test
    void testUpdateProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1)
                .name("Updated Product")
                .price(new BigDecimal("20.99"))
                .build();

        Product savedProduct = Product.builder()
                .id(1)
                .name("Updated Product")
                .price(new BigDecimal("20.99"))
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO updatedProductDTO = productService.update(productDTO);

        assertThat(updatedProductDTO.getId()).isEqualTo(1);
        assertThat(updatedProductDTO.getName()).isEqualTo("Updated Product");
        assertThat(updatedProductDTO.getPrice()).isEqualTo(new BigDecimal("20.99"));
        verify(productRepository).save(any(Product.class)); // Verify repository interaction
    }

    @Test
    void testDeleteProduct() {
        Product product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();

        doNothing().when(productRepository).delete(any(Product.class));

        String result = productService.delete(product);

        assertThat(result).isEqualTo("Deleted!"); // Validate return message
        verify(productRepository).delete(product); // Verify repository interaction
    }

    @Test
    void testConvertToProductDTO() {
        Product product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();

        ProductDTO result = productService.convertToProductDTO(product);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void testConvertToProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();

        Product result = productService.convertToProduct(productDTO);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("10.99"));
    }
}