package com.admin_product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String category;
    private String subcategory;
    private Double price;
    private String image;
    private String description;
    private String tags;
    private LocalDateTime addedOn;
    private LocalDateTime updatedOn;
} 