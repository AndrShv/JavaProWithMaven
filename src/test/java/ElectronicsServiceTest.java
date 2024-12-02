

import com.example.model.Electronics;
import com.example.repository.ComponentRepository;
import com.example.repository.ElectronicsRepository;
import com.example.service.ElectronicsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ElectronicsServiceTest {

    @Mock
    private ElectronicsRepository electronicsRepository;

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ElectronicsService electronicsService;

    @BeforeEach
    public void setUp() {
        // Инициализация моков перед каждым тестом
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetElectronicsById_Success() {
        // Данные для теста
        int id = 1;
        Electronics electronics = new Electronics();
        electronics.setId(id);

        // Мокаем поведение electronicsRepository
        when(electronicsRepository.findById((long) id)).thenReturn(Optional.of(electronics));

        // Выполнение тестируемого метода
        Electronics result = electronicsService.getElectronicsById(id);

        // Проверки
        assertNotNull(result, "Electronics should not be null");
        assertEquals(id, result.getId(), "ID should match");
        verify(electronicsRepository, times(1)).findById((long) id); // Проверяем, что findById был вызван
    }

    @Test
    public void testGetElectronicsById_NotFound() {
        // Данные для теста
        int id = 1;

        // Мокаем поведение electronicsRepository на случай, если объект не найден
        when(electronicsRepository.findById((long) id)).thenReturn(Optional.empty());

        // Выполнение тестируемого метода
        Electronics result = electronicsService.getElectronicsById(id);

        // Проверки
        assertNull(result, "Electronics should be null if not found");
        verify(electronicsRepository, times(1)).findById((long) id); // Проверяем, что findById был вызван
    }

    @Test
    public void testCreateElectronics() {
        // Данные для теста
        Electronics electronics = new Electronics();
        electronics.setBrand("Sony");
        electronics.setModel("Xperia");

        // Мокаем поведение electronicsRepository для сохранения объекта
        when(electronicsRepository.save(electronics)).thenReturn(electronics);

        // Выполнение тестируемого метода
        Electronics result = electronicsService.createElectronics(electronics);

        // Проверки
        assertNotNull(result, "Created electronics should not be null");
        assertEquals("Sony", result.getBrand(), "Brand should be Sony");
        verify(electronicsRepository, times(1)).save(electronics); // Проверяем, что save был вызван
    }

    @Test
    public void testDeleteElectronics_Success() {
        // Данные для теста
        int id = 1;
        Electronics electronics = new Electronics();
        electronics.setId(id);

        // Мокаем поведение electronicsRepository, когда объект найден
        when(electronicsRepository.findById((long) id)).thenReturn(Optional.of(electronics));

        // Выполнение тестируемого метода
        boolean result = electronicsService.deleteElectronics(id);

        // Проверки
        assertTrue(result, "Delete should return true when successful");
        verify(electronicsRepository, times(1)).findById((long) id); // Проверяем, что findById был вызван
        verify(electronicsRepository, times(1)).delete(electronics); // Проверяем, что delete был вызван
    }

    @Test
    public void testDeleteElectronics_NotFound() {
        // Данные для теста
        int id = 1;

        // Мокаем поведение electronicsRepository, когда объект не найден
        when(electronicsRepository.findById((long) id)).thenReturn(Optional.empty());

        // Выполнение тестируемого метода
        boolean result = electronicsService.deleteElectronics(id);

        // Проверки
        assertFalse(result, "Delete should return false when electronics not found");
        verify(electronicsRepository, times(1)).findById((long) id); // Проверяем, что findById был вызван
        verify(electronicsRepository, never()).delete(any(Electronics.class)); // Проверяем, что delete не был вызван
    }
}