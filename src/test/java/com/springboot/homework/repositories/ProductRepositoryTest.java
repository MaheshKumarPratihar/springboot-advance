package com.springboot.homework.repositories;

import com.springboot.homework.models.entities.Product;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init(){
        seedProducts();
    }

    private void seedProducts() {
        if (this.productRepository.findByName("Product1").isEmpty()) {
            this.productRepository.save(Product.builder()
                    .name("Product1")
                    .price(new BigDecimal("10.99"))
                    .build());
        }

        if (this.productRepository.findByName("Product2").isEmpty()) {
            this.productRepository.save(Product.builder()
                    .name("Product2")
                    .price(new BigDecimal("20.49"))
                    .build());
        }

        if (this.productRepository.findByName("Product3").isEmpty()) {
            this.productRepository.save(Product.builder()
                    .name("Product3")
                    .price(new BigDecimal("30.00"))
                    .build());
        }
    }

    @Test
    void testFindByName() {
        List<Product> products = this.productRepository.findByName("Product1");

        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Product1");
        assertThat(products.get(0).getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void testFindByNameShouldReturnEmptyForNonExistentProduct() {
        List<Product> products = this.productRepository.findByName("NonExistentProduct");

        assertThat(products).isEmpty();
    }

    @Test
    void testSaveProduct() {
        Product newProduct = Product.builder()
                .name("Product4")
                .price(new BigDecimal("15.99"))
                .build();

        Product savedProduct = this.productRepository.save(newProduct);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
        assertThat(savedProduct.getName()).isEqualTo("Product4");
        assertThat(savedProduct.getPrice()).isEqualByComparingTo(new BigDecimal("15.99"));
    }

    @Test
    void testFindById() {
        Optional<Product> product = this.productRepository.findById(1);

        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Product1");
        assertThat(product.get().getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;

        this.productRepository.deleteById(productId);
        Optional<Product> deletedProduct = this.productRepository.findById(productId);

        assertThat(deletedProduct).isNotPresent();
    }

    @Test
    void testUpdateProduct() {
        Optional<Product> productOptional = this.productRepository.findById(1);
        assertThat(productOptional).isPresent();
        Product productToUpdate = productOptional.get();

        productToUpdate.setPrice(new BigDecimal("40.00"));
        Product updatedProduct = this.productRepository.save(productToUpdate);

        assertThat(updatedProduct.getPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
    }
}