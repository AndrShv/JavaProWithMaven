import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceJUnitTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1);
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrder(1, order);
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    public void testUpdateOrder_NotExists() {
        Order order = new Order();
        when(orderRepository.existsById(1L)).thenReturn(false);

        Order result = orderService.updateOrder(1, order);
        assertNull(result);
    }

    @Test
    public void testDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        boolean result = orderService.deleteOrder(1);
        assertTrue(result);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteOrder_NotExists() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        boolean result = orderService.deleteOrder(1);
        assertFalse(result);
        verify(orderRepository, never()).deleteById(1L);
    }

    @Test
    public void testAddProductToOrder() {
        Order order = new Order();
        Product product = new Product();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(productRepository.save(product)).thenReturn(product);

        Order result = orderService.addProductToOrder(1, product);
        assertNotNull(result);
        assertTrue(order.getProducts().contains(product));
    }

    @Test
    public void testAddProductToOrder_NotExists() {
        Product product = new Product();
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order result = orderService.addProductToOrder(1, product);
        assertNull(result);
        verify(productRepository, never()).save(product);
    }

    @Test
    public void testRemoveProductFromOrder() {
        Order order = new Order();
        Product product = new Product();
        product.setId(1);
        order.getProducts().add(product);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.removeProductFromOrder(1, 1);
        assertNotNull(result);
        assertFalse(order.getProducts().contains(product));
    }

    @Test
    public void testRemoveProductFromOrder_NotExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order result = orderService.removeProductFromOrder(1, 1);
        assertNull(result);
    }
}
