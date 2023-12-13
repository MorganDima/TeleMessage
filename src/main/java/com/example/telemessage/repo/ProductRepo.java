package com.example.telemessage.repo;

import com.example.telemessage.dto.ProductDto;
import com.example.telemessage.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {

    public static final String HASH_KEY = "Product";

    @Autowired
    private RedisTemplate template;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    public Product save(Product product) {
        template.opsForHash().putIfAbsent(HASH_KEY, String.valueOf(product.getId()), product);
        return product;
    }

    public List<Product> findAll() {
        return template.opsForHash().values(HASH_KEY);
    }

    public Product findProductById(long id) {
        return (Product) template.opsForHash().get(HASH_KEY, String.valueOf(id));
    }

    public String deleteProduct(int id) {
        template.opsForHash().delete(HASH_KEY, String.valueOf(id));
        return "product removed !!";
    }

    public boolean updateProduct(Product product) {
        if (this.findProductById(product.getId()) != null) {
            template.opsForHash().put(HASH_KEY, String.valueOf(product.getId()), product);
            return true;
        }
        return false;
    }

    public boolean updatePartProduct(ProductDto productDto, long id) {

        Product existingProduct = this.findProductById(id);
        if (existingProduct != null) {
            if (productDto.name() != null) {
                existingProduct.setName(productDto.name());
            }
            if (productDto.qty() != null) {
                existingProduct.setQty(productDto.qty());
            }
            if (productDto.price() != null) {
                existingProduct.setPrice(Integer.parseInt(productDto.price()));
            }
            template.opsForHash().put(HASH_KEY, String.valueOf(id), existingProduct);
            return true;
        }
        return false;
    }
}
