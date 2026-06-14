package com.quickmart.product;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@SessionScope
public class CartQueue {
    private final Queue<CartItem> cartQueue;
    private final Map<Long, CartItem> itemMap; // For quick lookups

    public CartQueue() {
        this.cartQueue = new ConcurrentLinkedQueue<>();
        this.itemMap = new ConcurrentHashMap<>();
    }

    public void addItem(CartItem item) {
        if (itemMap.containsKey(item.getId())) {
            // Update existing item
            CartItem existingItem = itemMap.get(item.getId());
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            existingItem.setTimestamp(System.currentTimeMillis());
        } else {
            // Add new item
            cartQueue.offer(item);
            itemMap.put(item.getId(), item);
        }
    }

    public void removeItem(Long itemId) {
        CartItem item = itemMap.remove(itemId);
        if (item != null) {
            cartQueue.remove(item);
        }
    }

    public void updateQuantity(Long itemId, int quantity) {
        CartItem item = itemMap.get(itemId);
        if (item != null) {
            item.setQuantity(quantity);
            item.setTimestamp(System.currentTimeMillis());
        }
    }

    public List<CartItem> getAllItems() {
        return cartQueue.stream()
                .sorted(Comparator.comparingLong(CartItem::getTimestamp))
                .collect(Collectors.toList());
    }

    public double getTotalPrice() {
        return cartQueue.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public int getTotalItems() {
        return cartQueue.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void clear() {
        cartQueue.clear();
        itemMap.clear();
    }

    public boolean isEmpty() {
        return cartQueue.isEmpty();
    }
} 