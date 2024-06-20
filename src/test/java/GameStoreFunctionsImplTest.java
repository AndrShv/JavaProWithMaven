

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

import iplm.GameStoreFunctionsImpl;
import model.Game;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class GameStoreFunctionsImplTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @InjectMocks
    private GameStoreFunctionsImpl gameStore;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mock(Statement.class));
        gameStore = new GameStoreFunctionsImpl();
        gameStore.connection = mockConnection;
    }

    @Test
    public void testAddGame() throws Exception {
        Game game = new Game(1, "Test Game", new Date(), 4.5f, 59.99, "Test Description");
        doNothing().when(mockPreparedStatement).executeUpdate();
        gameStore.addGame(game);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteGame() throws Exception {
        int gameId = 1;
        doNothing().when(mockPreparedStatement).executeUpdate();
        gameStore.deleteGame(gameId);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSearchGameByName() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Game");
        when(mockResultSet.getDate("release_date"));
        when(mockResultSet.getFloat("rating")).thenReturn(4.5f);
        when(mockResultSet.getDouble("cost")).thenReturn(59.99);
        when(mockResultSet.getString("description")).thenReturn("Test Description");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        Game result = gameStore.searchGameByName("Test");

        assertNotNull(result);
        assertEquals("Test Game", result.getName());
    }

    @Test
    public void testFilterGamesByCost() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Game");
        when(mockResultSet.getDate("release_date"));
        when(mockResultSet.getFloat("rating")).thenReturn(4.5f);
        when(mockResultSet.getDouble("cost")).thenReturn(59.99);
        when(mockResultSet.getString("description")).thenReturn("Test Description");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        List<Game> games = gameStore.filterGamesByCost(50.0, 60.0);

        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getName());
    }

    @Test
    public void testFilterGamesByRating() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Game");
        when(mockResultSet.getDate("release_date"));
        when(mockResultSet.getFloat("rating")).thenReturn(4.5f);
        when(mockResultSet.getDouble("cost")).thenReturn(59.99);
        when(mockResultSet.getString("description")).thenReturn("Test Description");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        List<Game> games = gameStore.filterGamesByRating(4.0f);

        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getName());
    }

    @Test
    public void testGetAllGamesSortedByCreationDate() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Game");
        when(mockResultSet.getDate("release_date"));
        when(mockResultSet.getFloat("rating")).thenReturn(4.5f);
        when(mockResultSet.getDouble("cost")).thenReturn(59.99);
        when(mockResultSet.getString("description")).thenReturn("Test Description");

        Statement mockStatement = mock(Statement.class);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        List<Game> games = gameStore.getAllGamesSortedByCreationDate();

        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getName());
    }

    @Test
    public void testGetAllGames() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Test Game");
        when(mockResultSet.getDate("release_date"));
        when(mockResultSet.getFloat("rating")).thenReturn(4.5f);
        when(mockResultSet.getDouble("cost")).thenReturn(59.99);
        when(mockResultSet.getString("description")).thenReturn("Test Description");

        Statement mockStatement = mock(Statement.class);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        List<Game> games = gameStore.getAllGames();

        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getName());
    }
}
