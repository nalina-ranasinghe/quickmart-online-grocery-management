package com.admin.order.service;

import com.admin.order.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final String JSON_FILE_PATH = "orders.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public OrderService() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        initializeJsonFile();
    }

    private void initializeJsonFile() {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            try {
                objectMapper.writeValue(file, new ArrayList<Order>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Order> readOrders() {
        lock.readLock().lock();
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                initializeJsonFile();
            }
            return objectMapper.readValue(file, new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void writeOrders(List<Order> orders) {
        lock.writeLock().lock();
        try {
            File file = new File(JSON_FILE_PATH);
            objectMapper.writeValue(file, orders);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Order> getAllOrders(String status, String search) {
        List<Order> orders = readOrders();
        
        if (status != null && !status.equalsIgnoreCase("all")) {
            orders = orders.stream()
                    .filter(o -> o.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        
        if (search != null && !search.isEmpty()) {
            String q = search.toLowerCase();
            orders = orders.stream().filter(o ->
                    o.getCustomerName().toLowerCase().contains(q) ||
                    o.getCustomerEmail().toLowerCase().contains(q) ||
                    o.getCustomerPhone().toLowerCase().contains(q) ||
                    o.getAddress().toLowerCase().contains(q) ||
                    o.getId().toString().contains(q)
            ).collect(Collectors.toList());
        }
        
        return orders;
    }

    public Optional<Order> getOrder(Long id) {
        return readOrders().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    public Order createOrder(Order order) {
        List<Order> orders = readOrders();
        // Generate a new ID if not provided
        if (order.getId() == null) {
            Long maxId = orders.stream()
                    .mapToLong(Order::getId)
                    .max()
                    .orElse(0L);
            order.setId(maxId + 1);
        }
        orders.add(order);
        writeOrders(orders);
        return order;
    }

    public Optional<Order> updateOrder(Long id, Order order) {
        List<Order> orders = readOrders();
        Optional<Order> existingOrder = orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();

        if (existingOrder.isPresent()) {
            order.setId(id);
            orders = orders.stream()
                    .map(o -> o.getId().equals(id) ? order : o)
                    .collect(Collectors.toList());
            writeOrders(orders);
            return Optional.of(order);
        }

        return Optional.empty();
    }

    public void deleteOrder(Long id) {
        List<Order> orders = readOrders();
        orders = orders.stream()
                .filter(o -> !o.getId().equals(id))
                .collect(Collectors.toList());
        writeOrders(orders);
    }
} 