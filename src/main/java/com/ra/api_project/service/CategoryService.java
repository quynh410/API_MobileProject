package com.ra.api_project.service;

import com.ra.api_project.dto.request.CategoryRequest;
import com.ra.api_project.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    Page<CategoryResponse> getAllCategories(Pageable pageable);

    List<CategoryResponse> getAllCategoriesNoPaging();

    CategoryResponse getCategoryById(Integer id);

    CategoryResponse createCategory(CategoryRequest request, MultipartFile image, boolean removeBackground);

    CategoryResponse updateCategory(Integer id, CategoryRequest request, MultipartFile image, boolean removeBackground);

    void deleteCategory(Integer id);
}