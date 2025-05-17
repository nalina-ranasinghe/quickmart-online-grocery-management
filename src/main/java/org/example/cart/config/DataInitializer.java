package org.example.cart.config;

import org.example.cart.model.Product;
import org.example.cart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            // Add sample products only if the database is empty
            if (productRepository.count() == 0) {
                Product laptop = new Product();
                laptop.setName("Laptop");
                laptop.setDescription("High-performance laptop with 16GB RAM and 512GB SSD");
                laptop.setPrice(999.99);
                laptop.setQuantity(10);
                productRepository.save(laptop);

                Product smartphone = new Product();
                smartphone.setName("Smartphone");
                smartphone.setDescription("Latest model with 5G support and 128GB storage");
                smartphone.setPrice(499.99);
                smartphone.setQuantity(20);
                productRepository.save(smartphone);

                Product headphones = new Product();
                headphones.setName("Wireless Headphones");
                headphones.setDescription("Noise-cancelling wireless headphones with 30-hour battery life");
                headphones.setPrice(149.99);
                headphones.setQuantity(30);
                productRepository.save(headphones);

                Product tablet = new Product();
                tablet.setName("Tablet");
                tablet.setDescription("10-inch tablet with retina display and Apple Pencil support");
                tablet.setPrice(299.99);
                tablet.setQuantity(15);
                productRepository.save(tablet);

                Product smartwatch = new Product();
                smartwatch.setName("Smartwatch");
                smartwatch.setDescription("Fitness tracking smartwatch with heart rate monitor");
                smartwatch.setPrice(199.99);
                smartwatch.setQuantity(25);
                productRepository.save(smartwatch);
            }
        };
    }
} 