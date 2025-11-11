package com.ra.api_project.service;

import com.ra.api_project.dto.request.ProductRequest;
import com.ra.api_project.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Integer id);

    Page<ProductResponse> getProductsByCategory(Integer categoryId, Pageable pageable);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Integer id, ProductRequest request);

    void deleteProduct(Integer id);
}