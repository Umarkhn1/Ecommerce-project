package com.example.ecommerce.service;

import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        logger.info("Получение всех продуктов из БД");
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        logger.info("Поиск продукта по id: {}", id);
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        logger.info("Сохранение продукта: {}", product.getName());
        return productRepository.save(product);
    }

    public Product saveById(Long id, Product product) {
        product.setId(id);
        logger.info("Обновление продукта с id {}: {}", id, product.getName());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        logger.info("Удаление продукта с id: {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> findByNameAndCategory(String name, String category) {
        logger.info("Поиск продуктов с name='{}' и category='{}'", name, category);
        return productRepository.findByNameAndCategory(name, category);
    }
}
