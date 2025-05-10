package com.springboot.homework.services;

import com.springboot.homework.models.dtos.ProductDTO;
import com.springboot.homework.models.entities.Product;
import com.springboot.homework.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Integer create(ProductDTO productDTO) {
        return this.productRepository
                .save(this.convertToProduct(productDTO))
                .getId();
    }

    public Optional<Product> getById(Integer id) {
        return this.productRepository.findById(id);
    }

    public ProductDTO update(ProductDTO productDTO) {
        Product updatedProduct = this.productRepository.save(this.convertToProduct(productDTO));
        return this.convertToProductDTO(updatedProduct);
    }

    public String delete(Product product) {
        this.productRepository.delete(product);
        return "Deleted!";
    }

    public ProductDTO convertToProductDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public Product convertToProduct(ProductDTO productDTO){
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
    }
}
