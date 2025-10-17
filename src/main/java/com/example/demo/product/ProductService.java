package com.example.demo.product;

import com.example.demo.product.request.ProductRequest;
import com.example.demo.product.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest product);

    ProductResponse update(String id, ProductRequest product);

    void delete(String id);

    ProductResponse getById(String id);

    PagedResponse<ProductResponse> getAll(String name, Pageable pageable);

}
