package com.example.ecommerce.Controller;

import com.example.ecommerce.DTO.CreateOrderRequest;
import com.example.ecommerce.DTO.OrderResponse;
import com.example.ecommerce.Mapper.OrderMapper;
import com.example.ecommerce.status.OrderStatus;
import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "API для управления заказами")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    @Operation(summary = "Получить все заказы")
    public List<OrderResponse> getAllOrders() {
        return orderService.findAll().stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderMapper.toDTO(orderService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Создать новый заказ")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());

        List<OrderItem> items = request.getOrderItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            Product product = new Product();
            product.setId(i.getProductId());
            item.setProduct(product);
            item.setQuantity(i.getQuantity());
            return item;
        }).toList();
        order.setOrderItems(items);

        return ResponseEntity.ok(orderMapper.toDTO(orderService.createOrder(order)));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус заказа")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderMapper.toDTO(orderService.updateStatus(id, OrderStatus.valueOf(status))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отменить заказ")
    public List<OrderResponse> cancelOrder(@PathVariable Long id) {
        orderService.delete(id);
        return getAllOrders();
    }

    @GetMapping("/customer/{email}")
    @Operation(summary = "Получить заказы по email клиента")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.findByCustomerEmail(email).stream()
                .map(orderMapper::toDTO)
                .toList());
    }
}
