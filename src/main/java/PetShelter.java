import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PetShelter {
    private String filePath;
    private List<Pet> pets;
    private ObjectMapper objectMapper;

    public PetShelter() {
        this("pets.json");
    }

    public PetShelter(String filePath) {
        this.filePath = filePath;
        objectMapper = new ObjectMapper();
        pets = loadPets();
    }

    public List<Pet> loadPets() {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try {
                return objectMapper.readValue(file, new TypeReference<List<Pet>>() {});
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
            objectMapper.writeValue(new File(filePath), pets);
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

    public List<Pet> getPets() {
        return pets;
    }
}
