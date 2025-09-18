package com.example.ecommerce.service;

import com.example.ecommerce.domain.Order;
import com.example.ecommerce.domain.OrderItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.status.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> findAll() {
        logger.info("Получение всех заказов из DB");
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        logger.info("Поиск заказа с id {} из DB", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Заказ с id {} не найден", id);
                    return new OrderNotFoundException("Заказ не найден " + id);
                });
    }

    @Transactional
    public Order createOrder(Order order) {
        logger.info("Создание заказа для клиента: {}", order.getCustomerEmail());
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> {
                        logger.error("Продукт с id {} не найден", item.getProduct().getId());
                        return new ProductNotFoundException("Не найден такой продукт " + item.getProduct().getId());
                    });

            if (product.getStock() < item.getQuantity()) {
                logger.warn("Недостаточно товара {}. Доступно: {}, Запрошено: {}",
                        product.getName(), product.getStock(), item.getQuantity());
                throw new RuntimeException("Недостаточно стока для продукта " + product.getName());
            }

            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrder(order);

            totalAmount = totalAmount.add(item.getTotalPrice());
            logger.debug("Добавлен товар {} x{} в заказ. Сумма позиции: {}",
                    product.getName(), item.getQuantity(), item.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        logger.info("Общая сумма заказа: {}", totalAmount);

        Order savedOrder = orderRepository.save(order);
        logger.info("Заказ сохранён с id {}", savedOrder.getId());

        return savedOrder;
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        logger.info("Обновление статуса заказа {} на {}", id, status);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Заказ с id {} не найден", id);
                    return new OrderNotFoundException("Не существует такого заказа " + id);
                });

        if (order.getStatus() != OrderStatus.PENDING) {
            logger.warn("Статус заказа {} нельзя изменить, текущий: {}", id, order.getStatus());
            throw new RuntimeException("Только PENDING можно изменить");
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        logger.info("Статус заказа {} обновлён на {}", id, updatedOrder.getStatus());
        return updatedOrder;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Удаление заказа с id {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Заказ с id {} не найден", id);
                    return new OrderNotFoundException("Такого заказа не существует " + id);
                });

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            logger.debug("Возврат {} шт товара {} на склад", item.getQuantity(), product.getName());
        }

        orderRepository.delete(order);
        logger.info("Заказ {} успешно удалён", id);
    }

    public List<Order> findByCustomerEmail(String email) {
        logger.info("Поиск заказов для клиента с email {} из DB", email);
        return orderRepository.findByCustomerEmail(email);
    }
}
