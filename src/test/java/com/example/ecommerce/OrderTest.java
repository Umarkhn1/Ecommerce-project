package com.example.ecommerce;

import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = EcommerceApplication.class)
class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    void createOrder_ProductNotFound_ShouldThrowException() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        Product product = new Product();
        product.setId(99L);
        item.setProduct(product);
        item.setQuantity(1);
        order.setOrderItems(java.util.List.of(item));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(order));
    }

    @Test
    void createOrder_ShouldCalculateTotalAmount() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        Order order = new Order();
        order.setOrderItems(java.util.List.of(item));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order savedOrder = orderService.createOrder(order);

        assertEquals(BigDecimal.valueOf(200), savedOrder.getTotalAmount());
    }
}
