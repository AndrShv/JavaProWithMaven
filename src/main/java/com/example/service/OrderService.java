package com.example.service;

import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order getOrderById(int id) {
        Optional<Order> order = orderRepository.findById((long) id);
        return order.orElse(null);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(int id, Order order) {
        if (orderRepository.existsById((long) id)) {
            order.setId(id);
            return orderRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(int id) {
        if (orderRepository.existsById((long) id)) {
            orderRepository.deleteById((long) id);
            return true;
        }
        return false;
    }

    public Order addProductToOrder(int id, Product product) {
        Optional<Order> optionalOrder = orderRepository.findById((long) id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            productRepository.save(product); // Save the product first
            order.getProducts().add(product);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order removeProductFromOrder(int id, int productId) {
        Optional<Order> optionalOrder = orderRepository.findById((long) id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.getProducts().removeIf(p -> p.getId() == productId);
            return orderRepository.save(order);
        }
        return null;
    }
}
