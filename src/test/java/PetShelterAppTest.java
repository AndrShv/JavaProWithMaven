import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetShelterAppTest {

    private PetShelterApp petShelterApp;
    private ObjectMapper objectMapper;
    private File file;


    @Test
    void testLoadPetsFileDoesNotExist() { //0
        when(file.exists()).thenReturn(false);
        List<Pet> pets = petShelterApp.loadPets();
        assertTrue(pets.isEmpty());
    }



    @Test
    void testAddPetInvalidInput() { //3
        petShelterApp.addPet(null, "Golden Retriever", 3);
        assertTrue(petShelterApp.getPets().isEmpty());

        petShelterApp.addPet("Buddy", "", 3);
        assertTrue(petShelterApp.getPets().isEmpty());

        petShelterApp.addPet("Buddy", "Golden Retriever", -1);
        assertTrue(petShelterApp.getPets().isEmpty());
    }

    @Test
    void testAddPet() { //3
        petShelterApp.addPet("Buddy", "Golden Retriever", 3);
        assertEquals(1, petShelterApp.getPets().size());
        assertEquals("Buddy", petShelterApp.getPets().get(0).getName());
    }




    @Test
    void testListPetsEmpty() {//1
        petShelterApp.listPets();
        assertTrue(petShelterApp.getPets().isEmpty());
    }
    @Test
    void testRemovePetInvalidInput() {
        petShelterApp.addPet("Buddy", "Golden Retriever", 3);
        petShelterApp.removePet(null);
        assertEquals(2, petShelterApp.getPets().size());

        petShelterApp.removePet("");
        assertEquals(2, petShelterApp.getPets().size());

        petShelterApp.removePet("NonExistentPet");
        assertEquals(2, petShelterApp.getPets().size());
    }

    @Test
    void testRemovePet() {
        petShelterApp.addPet("Buddy", "Golden Retriever", 3);
        petShelterApp.removePet("Buddy");
    }



    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        petShelterApp = new PetShelterApp() {
            @Override
            public ObjectMapper getObjectMapper() {
                return objectMapper;
            }

            @Override
            public String getFilePath() {
                return "pets.json";
            }
        };

        file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(10L);
    }


}
