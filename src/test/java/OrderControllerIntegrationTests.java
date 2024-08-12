import com.example.DemoApplication;
import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class OrderControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Product 1");
        product.setPrice(10.0);
        product = productRepository.save(product);

        order = new Order();
        order.setCustomerName("Customer 1");
        order.getProducts().add(product);
        order = orderRepository.save(order);


        if (order.getId() != 1) {
            orderRepository.deleteById((long) order.getId());
            order.setId(1);
            order = orderRepository.save(order);
        }
    }

    @Test
    public void testCreateOrder() {
        Order newOrder = new Order();
        newOrder.setProducts(newArrayList(product));

        ResponseEntity<Order> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/orders/", newOrder, Order.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetOrderById() {
        ResponseEntity<Order> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/orders/1", Order.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());
    }

    @Test
    public void testUpdateOrder() {
        order.getProducts().add(product);
        orderRepository.save(order);
        restTemplate.put("http://localhost:" + port + "/orders/1", order);
        ResponseEntity<Order> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/orders/1", Order.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }



    @Test
    public void testDeleteOrder() {
        Order order = orderRepository.findById(1L).orElseThrow(() -> new RuntimeException("Order not found"));
        restTemplate.delete("http://localhost:" + port + "/orders/1");
        ResponseEntity<Order> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/orders/1", Order.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Optional<Order> deletedOrder = orderRepository.findById(1L);

    }

    @Test
    public void testAddProductToOrder() {
        HttpEntity<Product> requestEntity = new HttpEntity<>(product);
        ResponseEntity<Order> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + order.getId() + "/products",
                HttpMethod.PATCH,
                requestEntity,
                Order.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().getProducts().contains(product));
    }
    @Test
    public void testRemoveProductFromOrder() {
        order.getProducts().add(product);
        orderRepository.save(order);

        restTemplate.delete("http://localhost:" + port + "/orders/1/products/" + product.getId());
        ResponseEntity<Order> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/orders/1", Order.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().getProducts().contains(product));
    }
}
