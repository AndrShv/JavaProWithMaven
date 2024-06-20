package starter;
//что б запустить:
//docker build -t test .; docker run -it test

import iplm.GameStoreFunctionsImpl;
import model.Game;
import util.Messages;


import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class GameStore {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GameStoreFunctionsImpl gameStore = new GameStoreFunctionsImpl();

    public static void main(String[] args) throws ClassNotFoundException {


        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addGame();
                    break;
                case 2:
                    deleteGame();
                    break;
                case 3:
                    searchGameByName();
                    break;
                case 4:
                    filterGamesByCost();
                    break;
                case 5:
                    filterGamesByRating();
                    break;
                case 6:
                    getAllGamesSortedByCreationDate();
                    break;
                case 7:
                    getAllGames();
                    break;
                case 8:
                    System.out.println(Messages.EXITING.getMessage());
                    return;
                default:
                    System.out.println(Messages.INVALID_CHOICE.getMessage());
            }
        }
    }

    public static void printMenu() {
        System.out.println(Messages.MENU_HEADER.getMessage());
        System.out.println(Messages.MENU_OPTION_1.getMessage());
        System.out.println(Messages.MENU_OPTION_2.getMessage());
        System.out.println(Messages.MENU_OPTION_3.getMessage());
        System.out.println(Messages.MENU_OPTION_4.getMessage());
        System.out.println(Messages.MENU_OPTION_5.getMessage());
        System.out.println(Messages.MENU_OPTION_6.getMessage());
        System.out.println(Messages.MENU_OPTION_7.getMessage());
        System.out.println(Messages.MENU_OPTION_8.getMessage());
        System.out.print(Messages.MENU_PROMPT.getMessage());
    }

    public static void addGame() {
        System.out.print(Messages.ADD_GAME_NAME.getMessage());
        String name = scanner.nextLine();
        System.out.print(Messages.ADD_GAME_DATE.getMessage());
        Date releaseDate = Date.valueOf(scanner.nextLine());
        System.out.print(Messages.ADD_GAME_RATING.getMessage());
        float rating = Float.parseFloat(scanner.nextLine());
        System.out.print(Messages.ADD_GAME_COST.getMessage());
        double cost = Double.parseDouble(scanner.nextLine());
        System.out.print(Messages.ADD_GAME_DESCRIPTION.getMessage());
        String description = scanner.nextLine();

        Game game = new Game(0, name, releaseDate, rating, cost, description, null);
        gameStore.addGame(game);
        System.out.println(Messages.GAME_ADDED.getMessage());
    }

    public static void deleteGame() {
        System.out.print(Messages.DELETE_GAME_ID.getMessage());
        int id = Integer.parseInt(scanner.nextLine());
        gameStore.deleteGame(id);
        System.out.println(Messages.GAME_DELETED.getMessage());
    }

    public static void searchGameByName() {
        System.out.print(Messages.SEARCH_GAME_NAME.getMessage());
        String name = scanner.nextLine();
        Game game = gameStore.searchGameByName(name);
        if (game != null) {
            printGameDetails(game);
        } else {
            System.out.println(Messages.GAME_NOT_FOUND.getMessage());
        }
    }

    public static void filterGamesByCost() {
        System.out.print(Messages.FILTER_MIN_COST.getMessage());
        double minCost = Double.parseDouble(scanner.nextLine());
        System.out.print(Messages.FILTER_MAX_COST.getMessage());
        double maxCost = Double.parseDouble(scanner.nextLine());
        List<Game> games = gameStore.filterGamesByCost(minCost, maxCost);
        printGameList(games);
    }

    public static void filterGamesByRating() {
        System.out.print(Messages.FILTER_MIN_RATING.getMessage());
        float minRating = Float.parseFloat(scanner.nextLine());
        List<Game> games = gameStore.filterGamesByRating(minRating);
        printGameList(games);
    }

    public static void getAllGamesSortedByCreationDate() {
        List<Game> games = gameStore.getAllGamesSortedByCreationDate();
        printGameList(games);
    }

    private static void getAllGames() {
        List<Game> games = gameStore.getAllGames();
        printGameList(games);
    }

    public static void printGameDetails(Game game) {
        System.out.println("ID: " + game.getId());
        System.out.println("Name: " + game.getName());
        System.out.println("Release Date: " + game.getReleaseDate());
        System.out.println("Rating: " + game.getRating());
        System.out.println("Cost: " + game.getCost());
        System.out.println("Description: " + game.getDescription());
        System.out.println("Creation Date: " + game.getCreationDate());
        System.out.println();
    }

    public static void printGameList(List<Game> games) {
        if (games.isEmpty()) {
            System.out.println(Messages.NO_GAMES_FOUND.getMessage());
        } else {
            for (Game game : games) {
                printGameDetails(game);
            }
        }
    }
}
