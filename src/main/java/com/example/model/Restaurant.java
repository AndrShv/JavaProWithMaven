package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private String cuisine;
    private int rating;
    private double deliveryTime;


    public Restaurant() {}

    public Restaurant(int id, String name, String address, String cuisine, int rating, double deliveryTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisine = cuisine;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
    }
}

