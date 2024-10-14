
import java.util.Optional;
import com.example.service.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.model.Food;
import com.example.repository.FoodRepository;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFoodById_FoodExists() {
        Food food = new Food(1, "Pizza", "Main course", "Dough, Tomato, Cheese", 1200, 12.5, "Delicious pizza", true);
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        Food result = foodService.getFoodById(1);
        assertNotNull(result);
        assertEquals("Pizza", result.getName());
        verify(foodRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFoodById_FoodDoesNotExist() {
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());
        Food result = foodService.getFoodById(1);
        assertNull(result);
        verify(foodRepository, times(1)).findById(1L);
    }
}