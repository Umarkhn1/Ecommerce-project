package com.example.ecommerce.Controller;

import com.example.ecommerce.DTO.ProductResponse;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API для управления продуктами")
public class ProductController {

    public final ProductService productService;

    @GetMapping
    @Operation(summary = "Получить все продукты")
    public List<ProductResponse> getAllProducts() {
        return productService.getProducts()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продукт по ID")
    public ProductResponse getProduct(@PathVariable Long id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product не найден"));
        return convertToResponse(product);
    }

    @PostMapping
    @Operation(summary = "Создать новый продукт")
    public ProductResponse createProduct(@RequestBody @Valid Product product) {
        Product saved = productService.save(product);
        return convertToResponse(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить продукт по ID")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        Product update = productService.saveById(id, product);
        return convertToResponse(update);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить продукт по ID")
    public ResponseEntity<List<ProductResponse>> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        List<ProductResponse> remaining = productService.getProducts().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(remaining);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск продуктов по имени и категории")
    public List<ProductResponse> searchProducts(@RequestParam String name,
                                                @RequestParam String category) {
        return productService.findByNameAndCategory(name, category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());
        dto.setIsActive(product.isActive());
        return dto;
    }
}
