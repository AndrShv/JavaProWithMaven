package model;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {
    private int id;
    private String name;
    private Date releaseDate;
    private float rating;
    private double cost;
    private String description;
    private Timestamp creationDate;

    public Game(int id, String gameName, java.util.Date releaseDate, float rating, double cost, String description) {
        this.id = id;
        this.name = gameName;
        this.releaseDate = new java.sql.Date(releaseDate.getTime());
        this.rating = rating;
        this.cost = cost;
        this.description = description;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }
}
