
public enum Messages {

    MENU_HEADER("Game Store Management System"),
    MENU_OPTION_1("1. Add Game"),
    MENU_OPTION_2("2. Delete Game"),
    MENU_OPTION_3("3. Search Game by Name"),
    MENU_OPTION_4("4. Filter Games by Cost"),
    MENU_OPTION_5("5. Filter Games by Rating"),
    MENU_OPTION_6("6. Get All Games Sorted by Creation Date"),
    MENU_OPTION_7("7. View All Games"),
    MENU_OPTION_8("8. Exit"),
    MENU_PROMPT("Enter your choice: "),
    ADD_GAME_NAME("Enter game name: "),
    ADD_GAME_DATE("Enter release date (YYYY-MM-DD): "),
    ADD_GAME_RATING("Enter rating: "),
    ADD_GAME_COST("Enter cost: "),
    ADD_GAME_DESCRIPTION("Enter description: "),
    GAME_ADDED("Game added successfully!"),
    DELETE_GAME_ID("Enter game ID to delete: "),
    GAME_DELETED("Game deleted successfully!"),
    SEARCH_GAME_NAME("Enter game name to search: "),
    GAME_NOT_FOUND("Game not found."),
    FILTER_MIN_COST("Enter minimum cost: "),
    FILTER_MAX_COST("Enter maximum cost: "),
    FILTER_MIN_RATING("Enter minimum rating: "),
    NO_GAMES_FOUND("No games found."),
    INVALID_CHOICE("Invalid choice. Please try again."),
    GAME_NAME_EXISTS("A game with the same name already exists. try adding another game."),
    INVALID_INPUT("Invalid Syntax!"),
    EXITING("Exiting...");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}