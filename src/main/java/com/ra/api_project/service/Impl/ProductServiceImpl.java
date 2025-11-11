package com.ra.api_project.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.api_project.dto.request.ProductRequest;
import com.ra.api_project.dto.response.ProductResponse;
import com.ra.api_project.entity.Category;
import com.ra.api_project.entity.Product;
import com.ra.api_project.exception.GlobalExceptionHandler;
import com.ra.api_project.repository.CategoryRepository;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Integer categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryCategoryId(categoryId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByProductName(request.getProductName())) {
            throw new RuntimeException("Product name already exists: " + request.getProductName());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        // Upload ảnh lên Cloudinary
        String imageUrl = null;
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "product_images"));
                imageUrl = uploadResult.get("secure_url").toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }

        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(imageUrl) // Sử dụng URL từ Cloudinary thay vì từ request
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (!product.getProductName().equals(request.getProductName()) &&
                productRepository.existsByProductName(request.getProductName())) {
            throw new RuntimeException("Product name already exists: " + request.getProductName());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        // ✅ SỬA: Upload ảnh mới nếu có
        String imageUrl = product.getImageUrl(); // Giữ URL cũ
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                // Xóa ảnh cũ trên Cloudinary nếu có
                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    String publicId = extractPublicIdFromUrl(product.getImageUrl());
                    if (publicId != null) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    }
                }

                // Upload ảnh mới
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "product_images",
                                "resource_type", "auto"
                        ));
                imageUrl = uploadResult.get("secure_url").toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(imageUrl); // ✅ SỬA: Set URL mới hoặc giữ URL cũ
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }
    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // ✅ THÊM: Xóa ảnh trên Cloudinary trước khi xóa product
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                String publicId = extractPublicIdFromUrl(product.getImageUrl());
                if (publicId != null) {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
            }
        }

        productRepository.deleteById(id);
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{folder}/{public_id}.{format}
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String pathAfterUpload = parts[1];
                // Bỏ version (vXXXXXXXXXX/)
                String[] pathParts = pathAfterUpload.split("/", 2);
                if (pathParts.length > 1) {
                    String publicIdWithExtension = pathParts[1];
                    // Bỏ extension
                    int lastDotIndex = publicIdWithExtension.lastIndexOf('.');
                    if (lastDotIndex > 0) {
                        return publicIdWithExtension.substring(0, lastDotIndex);
                    }
                    return publicIdWithExtension;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to extract public_id from URL: " + e.getMessage());
        }
        return null;
    }
}