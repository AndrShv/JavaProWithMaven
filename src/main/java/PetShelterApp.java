import java.util.Scanner;

public class PetShelterApp {
    private PetShelter petShelter;

    public PetShelterApp() {
        petShelter = new PetShelter();
        System.out.println("Loaded pets: " + petShelter.getPets());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter a command (add, list, remove, exit): ");
            String command = scanner.nextLine();
            switch (command) {
                case "add":
                    System.out.println("Please enter pet name: ");
                    String name = scanner.nextLine();
                    System.out.println("Please enter breed of pet: ");
                    String breed = scanner.nextLine();
                    System.out.println("Please enter age of pet: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    petShelter.addPet(name, breed, age);
                    break;
                case "list":
                    petShelter.listPets();
                    break;
                case "remove":
                    System.out.println("Enter pet name to remove: ");
                    String petName = scanner.nextLine();
                    petShelter.removePet(petName);
                    break;
                case "exit":
                    petShelter.exit();
                    return;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}
