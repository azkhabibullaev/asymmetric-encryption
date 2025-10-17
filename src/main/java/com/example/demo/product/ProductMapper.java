package com.example.demo.product;

import com.example.demo.product.request.ProductRequest;
import com.example.demo.product.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .imageUrl(request.getImageUrl())
                .active(request.isActive())
                .build();
    }
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getLastModifiedDate())
                .createdBy(product.getCreatedBy() != null ? product.getCreatedBy() : null)
                .build();
    }

}
