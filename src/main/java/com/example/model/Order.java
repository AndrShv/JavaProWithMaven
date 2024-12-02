package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String customerName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Product> products;

    public Order() {
        this.products = new ArrayList<>();
    }

    public Order(int id, String customerName) {
        this.id = id;
        this.customerName = customerName;
        this.products = new ArrayList<>();
    }
}
