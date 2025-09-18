package com.example.ecommerce;

import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(1200.0));
        product.setStock(10);
        product.setCategory("Electronics"); // ⚡ обязательно, т.к. NOT NULL
        product.setActive(true);          // если поле тоже NOT NULL
        product.setCreatedAt(LocalDateTime.now()); // если createdAt NOT NULL

        Product saved = productRepository.save(product);
        assertNotNull(saved.getId());

        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
        assertEquals("Electronics", found.get().getCategory());
    }

}

