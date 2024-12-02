package com.example.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name; // Назва страви, наприклад, "Піца", "Борщ"
    private String type;
    private String compound;// Тип їжі, наприклад, "Вегетаріанська", "М'ясна"
    private int calories; // Кількість калорій на порцію
    private double price; // Ціна за страву
    private String description; // Опис страви, наприклад, "Традиційний український борщ"
    private boolean isVegetarian; // Чи є страва вегетаріанською

    public Food() {}

    public Food(int id, String name, String type, String compound, int calories, double price, String description, boolean isVegetarian) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.compound = compound;
        this.calories = calories;
        this.price = price;
        this.description = description;
        this.isVegetarian = isVegetarian;
    }
}
