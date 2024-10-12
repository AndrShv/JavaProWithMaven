package com.example.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String typeRam; //Например: ddr4/ddr5
    private String typeStorage;// Например: ssd/hdd
    private String storageQuantity;// Например: 512gb/1tb
    private double screenDiagonal; //Например: 15.6(laptop)/6.1(mob.ph.)

    public Component() {}

    public Component(int id, String typeRam, String typeStorage, String storageQuantity, double screenDiagonal) {
        this.id = id;
        this.typeRam = typeRam;
        this.typeStorage = typeStorage;
        this.storageQuantity = storageQuantity;
        this.screenDiagonal = screenDiagonal;
    }
}
