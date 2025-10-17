package com.example.demo.product.impl;

import com.example.demo.product.*;
import com.example.demo.product.request.ProductRequest;
import com.example.demo.product.response.ProductResponse;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            product.setCreatedBy(String.valueOf(user));
        }
        Product saved = productRepository.save(product);
        log.info("Created new product: {}", saved.getName());
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(String id, ProductRequest request) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setQuantity(request.getQuantity());
        existing.setImageUrl(request.getImageUrl());
        existing.setActive(request.isActive());
        Product updated = productRepository.save(existing);
        log.info("Updating product: {}", updated.getName());
        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
        log.info("Deleted product with id {}", id);
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        return productMapper.toResponse(product);
    }

    public PagedResponse<ProductResponse> getAll(String name, Pageable pageable) {
        Page<Product> page = (name != null && !name.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(name, pageable)
                : productRepository.findAll(pageable);
        Page<ProductResponse> mapped = page.map(productMapper::toResponse);
        return PagedResponse.fromPage(mapped);
    }
}
