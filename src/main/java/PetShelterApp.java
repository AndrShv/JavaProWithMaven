import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public abstract class PetShelterApp {

    private static final String FILE_PATH = "pets.json";
    private List<Pet> pets;
    public ObjectMapper objectMapper;

    public PetShelterApp() {
        objectMapper = new ObjectMapper();
        pets = loadPets();
        System.out.println("Loaded pets: " + pets);
    }

    public List<Pet> loadPets() {
        File file = new File(getFilePath());
        if (file.exists() && file.length() > 0) {
            try {
                List<Pet> pets = objectMapper.readValue(file, new TypeReference<List<Pet>>() {});
                System.out.println("Successfully loaded pets from file: " + pets);
                return pets;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist or is empty.");
        }
        return new ArrayList<>();
    }

    public void savePets() {
        try {
            objectMapper.writeValue(new File(getFilePath()), pets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPet(String name, String breed, int age) {
        if (name == null || name.isEmpty() || breed == null || breed.isEmpty() || age < 0) {
            System.out.println("Invalid input. Pet not added.");
            return;
        }
        pets.add(new Pet(name, breed, age));
        savePets();
        System.out.println("Pet added successfully");
    }

    public void listPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets in shelter");
        } else {
            for (Pet pet : pets) {
                System.out.println(pet.toString());
            }
        }
    }
    public void removePet(String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid input. No pet removed.");
            return;
        }
        Iterator<Pet> iterator = pets.iterator();
        boolean petRemoved = false;
        while (iterator.hasNext()) {
            Pet pet = iterator.next();
            if (pet.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                petRemoved = true;
                System.out.println("Pet removed successfully");
                break;
            }
        }
        if (!petRemoved) {
            System.out.println("Pet with the specified name not found");
        }
        savePets();
    }



    public void exit() {
        savePets();
        System.out.println("Goodbye");
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
                    addPet(name, breed, age);
                    break;
                case "list":
                    listPets();
                    break;
                case "remove":
                    System.out.println("Enter pet name to remove: ");
                    String petName = scanner.nextLine();
                    removePet(petName);
                    break;
                case "exit":
                    exit();
                    return;
                default:
                    System.out.println("Invalid command");
            }
        }
    }


    public String getFilePath() {
        return FILE_PATH;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public static void main(String[] args) {
        PetShelterApp app = new PetShelterApp() {
            @Override
            public ObjectMapper getObjectMapper() {
                return null;
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(app::exit));
        app.run();
    }

    public abstract ObjectMapper getObjectMapper();
}
