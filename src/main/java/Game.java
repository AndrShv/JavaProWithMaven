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
}
