package com.admin.order.service;

import com.admin.order.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class OrderProductService {
    private final String JSON_FILE_PATH = "products.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public OrderProductService() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        initializeJsonFile();
    }

    private void initializeJsonFile() {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            try {
                objectMapper.writeValue(file, new ArrayList<Product>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Product> readProducts() {
        lock.readLock().lock();
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                initializeJsonFile();
            }
            return objectMapper.readValue(file, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void writeProducts(List<Product> products) {
        lock.writeLock().lock();
        try {
            File file = new File(JSON_FILE_PATH);
            objectMapper.writeValue(file, products);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Product> getAllProducts() {
        return readProducts();
    }
} 