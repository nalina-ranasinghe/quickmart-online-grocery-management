package com.quickmart.product;

import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class SimpleQueue {
    private CartItem[] items;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public SimpleQueue() {
        this.capacity = 10; // Initial capacity
        this.items = new CartItem[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    // Add item to the queue (enqueue)
    public void addItem(CartItem item) {
        // Check if item already exists
        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            if (items[index].getId().equals(item.getId())) {
                // Update existing item
                items[index].setQuantity(items[index].getQuantity() + item.getQuantity());
                items[index].setTimestamp(System.currentTimeMillis());
                return;
            }
        }

        // Add new item
        if (isFull()) {
            resize();
        }
        
        rear = (rear + 1) % capacity;
        items[rear] = item;
        size++;
    }

    // Remove item from the queue
    public void removeItem(Long itemId) {
        if (isEmpty()) {
            return;
        }

        int currentSize = size;
        for (int i = 0; i < currentSize; i++) {
            CartItem item = dequeue();
            if (!item.getId().equals(itemId)) {
                enqueue(item);
            }
        }
    }

    // Update item quantity
    public void updateQuantity(Long itemId, int quantity) {
        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            if (items[index].getId().equals(itemId)) {
                items[index].setQuantity(quantity);
                items[index].setTimestamp(System.currentTimeMillis());
                return;
            }
        }
    }

    // Get all items in timestamp order
    public CartItem[] getAllItems() {
        if (isEmpty()) {
            return new CartItem[0];
        }

        // Create array to hold items
        CartItem[] result = new CartItem[size];
        for (int i = 0; i < size; i++) {
            result[i] = items[(front + i) % capacity];
        }

        // Sort by timestamp using bubble sort
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (result[j].getTimestamp() > result[j + 1].getTimestamp()) {
                    CartItem temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        return result;
    }

    // Calculate total price
    public double getTotalPrice() {
        double total = 0.0;
        for (int i = 0; i < size; i++) {
            CartItem item = items[(front + i) % capacity];
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // Get total number of items
    public int getTotalItems() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            CartItem item = items[(front + i) % capacity];
            total += item.getQuantity();
        }
        return total;
    }

    // Add item to the queue (enqueue)
    private void enqueue(CartItem item) {
        if (isFull()) {
            resize();
        }
        
        rear = (rear + 1) % capacity;
        items[rear] = item;
        size++;
    }

    // Remove item from the queue (dequeue)
    private CartItem dequeue() {
        if (isEmpty()) {
            return null;
        }

        CartItem item = items[front];
        items[front] = null;
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    // Peek at the front item without removing it
    public CartItem peek() {
        if (isEmpty()) {
            return null;
        }
        return items[front];
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check if queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Get current size of queue
    public int size() {
        return size;
    }

    // Resize the queue when it's full
    private void resize() {
        int newCapacity = capacity * 2;
        CartItem[] newItems = new CartItem[newCapacity];
        
        // Copy items to new array
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(front + i) % capacity];
        }
        
        items = newItems;
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
    }

    // Clear the queue
    public void clear() {
        items = new CartItem[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }
} 