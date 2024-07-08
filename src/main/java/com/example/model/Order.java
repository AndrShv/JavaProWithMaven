package com.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private int id;
    private String customerName;
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
