package com.example.service;

import com.example.model.Order;
import com.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
    private Map<Integer, Order> orderRepository = new HashMap<>();
    private int currentId = 1;

    public Order getOrderById(int id) {
        return orderRepository.get(id);
    }

    public Order createOrder(Order order) {
        order.setId(currentId++);
        orderRepository.put(order.getId(), order);
        return order;
    }

    public Order updateOrder(int id, Order order) {
        if (orderRepository.containsKey(id)) {
            order.setId(id);
            orderRepository.put(id, order);
            return order;
        }
        return null;
    }

    public boolean deleteOrder(int id) {
        return orderRepository.remove(id) != null;
    }

    public Order addProductToOrder(int id, Product product) {
        Order order = orderRepository.get(id);
        if (order != null) {
            order.getProducts().add(product);
            return order;
        }
        return null;
    }

    public Order removeProductFromOrder(int id, int productId) {
        Order order = orderRepository.get(id);
        if (order != null) {
            order.getProducts().removeIf(p -> p.getId() == productId);
            return order;
        }
        return null;
    }
}
