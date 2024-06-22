
import java.util.List;

public interface GameStoreFunctions {
    void addGame(Game game);
    void deleteGame(int id);
    Game searchGameByName(String name);
    List<Game> filterGamesByCost(double minCost, double maxCost);
    List<Game> filterGamesByRating(float minRating);
    List<Game> getAllGamesSortedByCreationDate();
    List<Game> getAllGames();
}