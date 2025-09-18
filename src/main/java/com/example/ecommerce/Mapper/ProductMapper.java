package com.example.ecommerce.Mapper;

import com.example.ecommerce.DTO.ProductResponse;
import com.example.ecommerce.domain.Product;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
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
