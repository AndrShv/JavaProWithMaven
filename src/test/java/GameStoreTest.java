import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
public class GameStoreTest {
    @InjectMocks
    private GameStoreFunctionsImpl gameStore;
    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;
    @Mock
    private PreparedStatement preparedStatement;


    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        gameStore = new GameStoreFunctionsImpl();
        gameStore.createTable();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddGame() {
        Game game = new Game(0, "Test Game", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game);

        Game retrievedGame = gameStore.searchGameByName("Test Game");
        assertNotNull(retrievedGame);
        assertEquals("Test Game", retrievedGame.getName());
    }

    @Test
    public void testAddGameWithDuplicateName() {
        Game game1 = new Game(0, "Duplicate Game", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        Game game2 = new Game(0, "Duplicate Game", Date.valueOf("2023-01-02"), 4.0f, 49.99,
                "Another description", null);

        gameStore.addGame(game1);
    }

    @Test
    public void testDeleteGame() {
        Game game = new Game(0, "Delete Test Game", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game);

        Game retrievedGame = gameStore.searchGameByName("Delete Test Game");
        assertNotNull(retrievedGame);

        gameStore.deleteGame(retrievedGame.getId());
        retrievedGame = gameStore.searchGameByName("Delete Test Game");
        assertNull(retrievedGame);
    }

    @Test
    public void testSearchGameByName() {
        Game game = new Game(0, "Search Test Game", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game);

        Game retrievedGame = gameStore.searchGameByName("Search Test Game");
        assertNotNull(retrievedGame);
        assertEquals("Search Test Game", retrievedGame.getName());
    }

    @BeforeEach
    public void setUp1() {
        gameStore = new GameStoreFunctionsImpl();
        gameStore.clearGames();
    }


    @Test
    public void testFilterGamesByCost() {
        Game game1 = new Game(0, "Cost Test Game 1", Date.valueOf("2023-01-01"), 4.5f, 29.99,
                "Test description", null);
        Game game2 = new Game(0, "Cost Test Game 2", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game1);
        gameStore.addGame(game2);

        List<Game> games = gameStore.filterGamesByCost(20.00, 40.00);
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Cost Test Game 1", games.get(0).getName());
    }

    @Test
    public void testFilterGamesByRating() {
        Game game1 = new Game(0, "Rating Test Game 1", Date.valueOf("2023-01-01"), 3.5f, 29.99,
                "Test description", null);
        Game game2 = new Game(0, "Rating Test Game 2", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game1);
        gameStore.addGame(game2);

        List<Game> games = gameStore.filterGamesByRating(4.0f);
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Rating Test Game 2", games.get(0).getName());
    }


    @Test
    public void testGetAllGames() {
        Game game1 = new Game(0, "All Games Test Game 1", Date.valueOf("2023-01-01"), 4.5f, 29.99,
                "Test description", null);
        Game game2 = new Game(0, "All Games Test Game 2", Date.valueOf("2023-01-01"), 4.5f, 59.99,
                "Test description", null);
        gameStore.addGame(game1);
        gameStore.addGame(game2);

        List<Game> games = gameStore.getAllGames();
        assertNotNull(games);
        assertEquals(2, games.size());
    }
    @Test
    public void testGetAllGamesSortedByCreationDate() throws Exception {
        String sql = "SELECT * FROM Games ORDER BY creation_date DESC";

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(sql)).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("Game 1", "Game 2");
        when(resultSet.getDate("release_date")).thenReturn(Date.valueOf("2022-01-01"), Date.valueOf("2023-01-01"));
        when(resultSet.getDouble("rating")).thenReturn(4.5, 4.0);
        when(resultSet.getDouble("cost")).thenReturn(59.99, 49.99);
        when(resultSet.getString("description")).thenReturn("Description 1", "Description 2");
        when(resultSet.getDate("creation_date")).thenReturn(Date.valueOf("2021-01-01"), Date.valueOf("2022-01-01"));

        List<Game> games = gameStore.getAllGamesSortedByCreationDate();


        Game game1 = games.get(0);
        assertEquals(2, game1.getId());
        assertEquals("Game 2", game1.getName());

        Game game2 = games.get(1);
        assertEquals(1, game2.getId());
        assertEquals("Game 1", game2.getName());
    }
    @Test
    public void testSearchGameById_GameFound() throws Exception {
        Timestamp creationDate = Timestamp.valueOf("2024-01-01 00:00:00");
        int gameId = 1;
        String sql = "SELECT * FROM Games WHERE id = ?";

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(gameId);
        when(resultSet.getString("name")).thenReturn("Test Game");
        when(resultSet.getDouble("cost")).thenReturn(29.99);
        when(resultSet.getFloat("rating")).thenReturn(4.5f);
        when(resultSet.getDate("release_date")).thenReturn(Date.valueOf("2023-01-01"));
        Game expectedGame = new Game(gameId, "Test Game", Date.valueOf("2023-01-01"), 4.5f, 29.99,
                "null", creationDate);

        Game actualGame = gameStore.searchGameById(gameId);


    }

    @Test
    public void testSearchGameById_GameNotFound() throws Exception {
        int gameId = 1;
        String sql = "SELECT * FROM Games WHERE id = ?";


        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        Game actualGame = gameStore.searchGameById(gameId);
        assertNull(actualGame);
    }
}


