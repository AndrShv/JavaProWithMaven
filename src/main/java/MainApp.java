public class MainApp {
    public static void main(String[] args) {
        PetShelterApp app = new PetShelterApp() {};
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.run();
        }));
        app.run();
    }
}
