package org.example.cart.config;

import org.example.cart.model.Product;
import org.example.cart.service.FileProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private FileProductService productService;

    @Override
    public void run(String... args) {
        // Initialize empty products list if it doesn't exist
        List<Product> existingProducts = productService.getAllProducts();
        if (existingProducts == null) {
            productService.saveEmptyProductsList();
        }
    }
} 