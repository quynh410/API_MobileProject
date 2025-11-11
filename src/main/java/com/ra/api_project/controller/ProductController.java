package com.ra.api_project.controller;

import com.ra.api_project.dto.request.ProductRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.ProductResponse;
import com.ra.api_project.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<BaseResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(BaseResponse.<Page<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all products successfully")
                .data(products)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(BaseResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product successfully")
                .data(product)
                .build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(BaseResponse.<Page<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get products by category successfully")
                .data(products)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(BaseResponse.<Page<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Search products successfully")
                .data(products)
                .build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<ProductResponse>> createProduct(
            @Valid @ModelAttribute ProductRequest request) {

        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Product created successfully")
                        .data(product)
                        .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @Valid @ModelAttribute ProductRequest request) {

        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(BaseResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Product updated successfully")
                .data(product)
                .build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Product deleted successfully")
                .build());
    }
}