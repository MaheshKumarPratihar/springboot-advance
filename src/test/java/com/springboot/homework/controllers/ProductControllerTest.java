package com.springboot.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.homework.models.dtos.ProductDTO;
import com.springboot.homework.models.entities.Product;
import com.springboot.homework.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private Product product;

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Disable CSRF for tests
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

            return http.build();
        }
    }

    @BeforeEach
    void setUp() {
        productDTO = ProductDTO.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();

        product = Product.builder()
                .id(1)
                .name("Test Product")
                .price(new BigDecimal("10.99"))
                .build();
    }

    @Test
    void testCreateProduct() throws Exception {

        Mockito.when(productService.create(any(ProductDTO.class))).thenReturn(1);

        // Perform POST request
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))) // Convert to JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(1))); // Ensure the response body contains the ID


        Mockito.verify(productService).create(any(ProductDTO.class));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.of(product));
        Mockito.when(productService.convertToProductDTO(product)).thenReturn(productDTO);

        // Perform GET request
        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(10.99)));

        // Verify
        Mockito.verify(productService).getById(1);
        Mockito.verify(productService).convertToProductDTO(product);
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.empty());

        // Perform GET request
        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        Mockito.verify(productService).getById(1);
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.of(product));
        Mockito.when(productService.update(any(ProductDTO.class))).thenReturn(productDTO);

        // Perform PUT request
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(10.99)));

        // Verify
        Mockito.verify(productService).getById(1);
        Mockito.verify(productService).update(any(ProductDTO.class));
    }

    @Test
    void testUpdateProduct_NotFound() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.empty());

        // Perform PUT request
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());

        // Verify
        Mockito.verify(productService).getById(1);
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.of(product));
        Mockito.when(productService.delete(product)).thenReturn("Deleted!");

        // Perform DELETE request
        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted!"));

        // Verify
        Mockito.verify(productService).getById(1);
        Mockito.verify(productService).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        // Mock service behavior
        Mockito.when(productService.getById(1)).thenReturn(Optional.empty());

        // Perform DELETE request
        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        Mockito.verify(productService).getById(1);
        Mockito.verify(productService, never()).delete(any(Product.class));
    }
}