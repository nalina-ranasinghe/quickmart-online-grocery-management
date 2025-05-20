package com.quickmart.product.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class Product implements Serializable {
    private Long id;
    private String name;
    private String category;
    private String subcategory;
    private Double price;
    private String image;
} 