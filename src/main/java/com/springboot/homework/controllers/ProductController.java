package com.springboot.homework.controllers;

import com.springboot.homework.dtos.ProductDTO;
import com.springboot.homework.entities.Product;
import com.springboot.homework.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productService.create(productDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Integer id){
        return this.productService
                .getById(id)
                .map(product -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .body(this.productService.convertToProductDTO(product))
                )
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDTO productDTO){
        Optional<Product> product = this.productService.getById(id);
        if(product.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        productDTO.setId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.update(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        return this.productService.getById(id)
                .map(product -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.productService.delete(product))
                )
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build()
                );
    }
}
