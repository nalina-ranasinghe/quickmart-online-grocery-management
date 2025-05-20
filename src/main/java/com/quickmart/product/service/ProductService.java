package com.quickmart.product.service;

import com.quickmart.product.model.Product;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private static final String PRODUCTS_FILE = "products.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Product> getAllProducts() {
        try {
            File file = new File(PRODUCTS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading products file", e);
        }
    }

    public List<Product> getProductsByCategory(String category) {
        return getAllProducts().stream()
                .filter(product -> product.getCategory().equals(category))
                .toList();
    }

    public Product getProductById(Long id) {
        return getAllProducts().stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product addProduct(Product product) {
        List<Product> products = getAllProducts();
        product.setId(idGenerator.getAndIncrement());
        products.add(product);
        saveProducts(products);
        return product;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        List<Product> products = getAllProducts();
        Product existingProduct = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSubcategory(updatedProduct.getSubcategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImage(updatedProduct.getImage());

        saveProducts(products);
        return existingProduct;
    }

    public void deleteProduct(Long id) {
        List<Product> products = getAllProducts();
        products.removeIf(product -> product.getId().equals(id));
        saveProducts(products);
    }

    private void saveProducts(List<Product> products) {
        try {
            objectMapper.writeValue(new File(PRODUCTS_FILE), products);
        } catch (IOException e) {
            throw new RuntimeException("Error saving products file", e);
        }
    }
} 