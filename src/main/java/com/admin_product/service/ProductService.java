package com.admin_product.service;

import com.admin_product.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ProductService {
    private final String JSON_FILE_PATH = "products.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ProductService() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public List<Product> getAllProducts() {
        lock.readLock().lock();
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Product> getProductById(Long id) {
        return getAllProducts().stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product createProduct(Product product) {
        lock.writeLock().lock();
        try {
            List<Product> products = getAllProducts();
            // Generate new ID
            Long newId = products.stream()
                    .mapToLong(Product::getId)
                    .max()
                    .orElse(0) + 1;
            product.setId(newId);
            
            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            product.setAddedOn(now);
            product.setUpdatedOn(now);
            
            products.add(product);
            saveProducts(products);
            return product;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        lock.writeLock().lock();
        try {
            List<Product> products = getAllProducts();
            Optional<Product> existingProduct = products.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();

            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                product.setName(updatedProduct.getName());
                product.setCategory(updatedProduct.getCategory());
                product.setSubcategory(updatedProduct.getSubcategory());
                product.setPrice(updatedProduct.getPrice());
                product.setImage(updatedProduct.getImage());
                product.setDescription(updatedProduct.getDescription());
                product.setTags(updatedProduct.getTags());
                product.setUpdatedOn(LocalDateTime.now());
                
                saveProducts(products);
                return Optional.of(product);
            }
            return Optional.empty();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteProduct(Long id) {
        lock.writeLock().lock();
        try {
            List<Product> products = getAllProducts();
            boolean removed = products.removeIf(product -> product.getId().equals(id));
            if (removed) {
                saveProducts(products);
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveProducts(List<Product> products) {
        try {
            File file = new File(JSON_FILE_PATH);
            objectMapper.writeValue(file, products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllCategories() {
        return getAllProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
    }

    public List<String> getSubcategoriesByCategory(String category) {
        return getAllProducts().stream()
                .filter(product -> product.getCategory().equals(category))
                .map(Product::getSubcategory)
                .distinct()
                .toList();
    }
} 