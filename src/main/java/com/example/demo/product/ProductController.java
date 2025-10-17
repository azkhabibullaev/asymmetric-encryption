package com.example.demo.product;

import com.example.demo.product.request.ProductRequest;
import com.example.demo.product.response.ProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product API")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(final @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(final @PathVariable String id, final @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(final @PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> getAll(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAll(name, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(final @PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
