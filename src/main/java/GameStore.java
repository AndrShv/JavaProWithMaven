import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class GameStore {
    private final Scanner scanner = new Scanner(System.in);
    private final GameStoreFunctionsImpl gameStore = new GameStoreFunctionsImpl();

    public static void main(String[] args) throws ClassNotFoundException {
        GameStore gameStoreApp = new GameStore();
        gameStoreApp.run();
    }

    public void run() throws ClassNotFoundException {
        gameStore.createTable();
        boolean continueRunning;
        do {
            continueRunning = processUserChoice();
        } while (continueRunning);
        System.out.println(Messages.EXITING.getMessage());
    }

    public boolean processUserChoice() {
        printMenu();
        String input = scanner.nextLine();
        if (input == null || input.isEmpty()) {
            System.out.println(Messages.INVALID_CHOICE.getMessage());
            return true;
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(Messages.INVALID_CHOICE.getMessage());
            return true;
        }

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
                return false;
            default:
                System.out.println(Messages.INVALID_CHOICE.getMessage());
        }
        return true;
    }

    public void printMenu() {
        Messages[] menuMessages = {
                Messages.MENU_HEADER,
                Messages.MENU_OPTION_1,
                Messages.MENU_OPTION_2,
                Messages.MENU_OPTION_3,
                Messages.MENU_OPTION_4,
                Messages.MENU_OPTION_5,
                Messages.MENU_OPTION_6,
                Messages.MENU_OPTION_7,
                Messages.MENU_OPTION_8

        };
        for (Messages message : menuMessages) {
            System.out.println(message.getMessage());
        }

        System.out.print(Messages.MENU_PROMPT.getMessage());
    }

    public void addGame() {
        System.out.print(Messages.ADD_GAME_NAME.getMessage());
        String name = scanner.nextLine();
        if (name == null || name.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }

        Game existingGame = gameStore.searchGameByName(name);
        if (existingGame != null) {
            System.out.println(Messages.GAME_NAME_EXISTS.getMessage());
            return;
        }

        System.out.print(Messages.ADD_GAME_DATE.getMessage());
        String dateInput = scanner.nextLine();
        if (dateInput == null || dateInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        Date releaseDate = Date.valueOf(dateInput);

        System.out.print(Messages.ADD_GAME_RATING.getMessage());
        String ratingInput = scanner.nextLine();
        if (ratingInput == null || ratingInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        float rating = Float.parseFloat(ratingInput);

        System.out.print(Messages.ADD_GAME_COST.getMessage());
        String costInput = scanner.nextLine();
        if (costInput == null || costInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        double cost = Double.parseDouble(costInput);

        System.out.print(Messages.ADD_GAME_DESCRIPTION.getMessage());
        String description = scanner.nextLine();
        if (description == null || description.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        Timestamp creationDate = Timestamp.valueOf("2024-01-01 00:00:00");


        Game game = new Game(0, name, releaseDate, rating, cost, description, creationDate);
        gameStore.addGame(game);
        System.out.println(Messages.GAME_ADDED.getMessage());
    }

    public void deleteGame() {
        System.out.print(Messages.DELETE_GAME_ID.getMessage());
        String idInput = scanner.nextLine();
        if (idInput == null || idInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        Game game = gameStore.searchGameById(Integer.parseInt(idInput));
        if (game != null) {
            printGameDetails(game);
        } else {
            System.out.println(Messages.GAME_NOT_FOUND.getMessage());
        }
    }

    public void searchGameByName() {
        System.out.print(Messages.SEARCH_GAME_NAME.getMessage());
        String name = scanner.nextLine();
        if (name == null || name.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        Game game = gameStore.searchGameByName(name);
        if (game != null) {
            printGameDetails(game);
        } else {
            System.out.println(Messages.GAME_NOT_FOUND.getMessage());
        }
    }

    public void filterGamesByCost() {
        System.out.print(Messages.FILTER_MIN_COST.getMessage());
        String minCostInput = scanner.nextLine();
        if (minCostInput == null || minCostInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        double minCost = Double.parseDouble(minCostInput);

        System.out.print(Messages.FILTER_MAX_COST.getMessage());
        String maxCostInput = scanner.nextLine();
        if (maxCostInput == null || maxCostInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        double maxCost = Double.parseDouble(maxCostInput);

        List<Game> games = gameStore.filterGamesByCost(minCost, maxCost);
        printGameList(games);
    }

    public void filterGamesByRating() {
        System.out.print(Messages.FILTER_MIN_RATING.getMessage());
        String minRatingInput = scanner.nextLine();
        if (minRatingInput == null || minRatingInput.isEmpty()) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        float minRating = Float.parseFloat(minRatingInput);

        List<Game> games = gameStore.filterGamesByRating(minRating);
        printGameList(games);
    }

    public void getAllGamesSortedByCreationDate() {
        List<Game> games = gameStore.getAllGamesSortedByCreationDate();
        printGameList(games);
    }

    private void getAllGames() {
        List<Game> games = gameStore.getAllGames();
        printGameList(games);
    }

    public void printGameDetails(Game game) {
        if (game == null) {
            System.out.println(Messages.INVALID_INPUT.getMessage());
            return;
        }
        System.out.println("ID: " + game.getId());
        System.out.println("Name: " + game.getName());
        System.out.println("Release Date: " + game.getReleaseDate());
        System.out.println("Rating: " + game.getRating());
        System.out.println("Cost: " + game.getCost());
        System.out.println("Description: " + game.getDescription());
        System.out.println("Creation Date: " + game.getCreationDate());
        System.out.println();
    }

    public void printGameList(List<Game> games) {
        if (games == null || games.isEmpty()) {
            System.out.println(Messages.NO_GAMES_FOUND.getMessage());
        } else {
            for (Game game : games) {
                printGameDetails(game);
            }
        }
    }
}
