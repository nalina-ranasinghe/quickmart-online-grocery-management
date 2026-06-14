package com.admin_order.service;

import com.admin_order.model.Order;
import com.admin_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders(String status, String search) {
        List<Order> orders = orderRepository.findAll();
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
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrder(Long id, Order order) {
        return orderRepository.findById(id).map(existing -> {
            order.setId(id);
            return orderRepository.save(order);
        });
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
} 