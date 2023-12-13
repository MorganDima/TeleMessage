package com.example.telemessage.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Product")
@Builder
public class Product implements Serializable {

    @Id
    private Long id;
    private String name;
    private String qty;
    private int price;

}