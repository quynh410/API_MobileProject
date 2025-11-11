package com.ra.api_project.service.Impl;

import com.cloudinary.Cloudinary;
import com.ra.api_project.dto.request.CategoryRequest;
import com.ra.api_project.dto.response.CategoryResponse;
import com.ra.api_project.entity.Category;
import com.ra.api_project.repository.CategoryRepository;
import com.ra.api_project.service.CategoryService;
import com.ra.api_project.service.RemoveBackgroundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;
    private final RemoveBackgroundService removeBackgroundService;

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public List<CategoryResponse> getAllCategoriesNoPaging() {
        return categoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request, MultipartFile image, boolean removeBackground) {
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new RuntimeException("Category name already exists: " + request.getCategoryName());
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadImageToCloudinary(image, removeBackground);
        } else if (request.getImageUrl() != null) {
            imageUrl = request.getImageUrl();
        }

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryRequest request, MultipartFile image, boolean removeBackground) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getCategoryName().equals(request.getCategoryName()) &&
                categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new RuntimeException("Category name already exists: " + request.getCategoryName());
        }

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        // Chỉ update ảnh nếu có file mới
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToCloudinary(image, removeBackground);
            category.setImageUrl(imageUrl);
        } else if (request.getImageUrl() != null) {
            category.setImageUrl(request.getImageUrl());
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing products");
        }

        categoryRepository.deleteById(id);
    }

    // Method riêng để upload ảnh lên Cloudinary
    private String uploadImageToCloudinary(MultipartFile file, boolean removeBackground) {
        try {
            byte[] imageBytes;
            String fileName;

            if (removeBackground) {
                log.info("Removing background for file: {}", file.getOriginalFilename());
                imageBytes = removeBackgroundService.removeBackground(file);
                fileName = "removed_bg_" + UUID.randomUUID();
            } else {
                imageBytes = file.getBytes();
                fileName = "category_" + UUID.randomUUID();
            }

            // Upload lên Cloudinary - CÁCH MỚI
            Map uploadResult = cloudinary.uploader().upload(
                    imageBytes,  // Truyền byte[] trực tiếp thay vì ByteArrayInputStream
                    Map.of(
                            "folder", "categories",
                            "public_id", fileName,
                            "resource_type", "image"
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            log.info("Image uploaded successfully: {}", imageUrl);
            return imageUrl;

        } catch (Exception e) {
            log.error("Upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi upload ảnh: " + e.getMessage());
        }
    }
    private CategoryResponse convertToResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}