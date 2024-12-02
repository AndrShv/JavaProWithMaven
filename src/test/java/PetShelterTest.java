import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetShelterTest {

    @TempDir
    File tempDir;

    private PetShelter petShelter;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = new File(tempDir, "pets.json");
        petShelter = new PetShelter(tempFile.getAbsolutePath());
    }

    @Test
    void testAddPet() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        List<Pet> pets = petShelter.getPets();
        assertEquals(1, pets.size());
        assertEquals("Buddy", pets.get(0).getName());
    }

    @Test
    void testAddPetInvalidInput() {
        petShelter.addPet("", "Golden Retriever", 3);
        assertTrue(petShelter.getPets().isEmpty());
    }

    @Test
    void testListPets() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        petShelter.addPet("Charlie", "Poodle", 2);
        petShelter.listPets();
    }

    @Test
    void testRemovePet() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        petShelter.removePet("Buddy");
        assertTrue(petShelter.getPets().isEmpty());
    }

    @Test
    void testRemovePetNotFound() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        petShelter.removePet("Charlie");
        assertEquals(1, petShelter.getPets().size());
    }

    @Test
    void testSavePets() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        petShelter.savePets();
        assertTrue(tempFile.exists());
    }

    @Test
    void testLoadPets() throws IOException {
        Pet pet = new Pet("Buddy", "Golden Retriever", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(tempFile, List.of(pet));

        PetShelter newPetShelter = new PetShelter(tempFile.getAbsolutePath());
        List<Pet> pets = newPetShelter.getPets();
        assertEquals(1, pets.size());
        assertEquals("Buddy", pets.get(0).getName());
    }

    @Test
    void testExit() {
        petShelter.addPet("Buddy", "Golden Retriever", 3);
        petShelter.exit();
        assertTrue(tempFile.exists());
    }
}
