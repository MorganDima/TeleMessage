package com.example.telemessage.controller;


import com.example.telemessage.dto.ProductDto;
import com.example.telemessage.entity.Product;
import com.example.telemessage.repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@EnableCaching
public class ProductController {


    private final ProductRepo productRepo;
    private final RedisTemplate redisTemplate;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Product> createProducts(@RequestBody ProductDto productDto) {

        Long redisId = redisTemplate.opsForValue().increment("my_key", 1);

        for (int i = 0; i <= 100; i++) {
            Product product = Product.builder()
                    .id(redisId+i)
                    .name(productDto.name()+i)
                    .qty(productDto.qty()+i)
                    .price(Integer.parseInt(productDto.price()+i))
                    .build();

            productRepo.save(product);
        }


        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_all")
    @CachePut(value = "Product")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/get_id/{id}")
    public Product getByID(@PathVariable int id) {
        return productRepo.findProductById(id);
    }

    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "Product")
    public String deleteByID(@PathVariable int id) {
        return productRepo.deleteProduct(id);
    }

    @PutMapping("/update/{id}")
    @CachePut(value = "Product")
    public ResponseEntity<Boolean> updateProduct(@RequestBody ProductDto productDto, @PathVariable Long id) {

        Product product = Product.builder()
                .id(id)
                .name(productDto.name())
                .qty(productDto.qty())
                .price(Integer.parseInt(productDto.price()))
                .build();

        return ResponseEntity.ok().body(productRepo.updateProduct(product));
    }

    @PatchMapping("/patch/{id}")
    @CachePut(value = "Product")
    public ResponseEntity<Boolean> updatePartProduct(@RequestBody ProductDto productDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(productRepo.updatePartProduct(productDto, id));
    }

}
