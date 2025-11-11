package com.ra.api_project.controller;

import com.ra.api_project.dto.request.CategoryRequest;
import com.ra.api_project.dto.response.CategoryResponse;
import com.ra.api_project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @GetMapping("/no-paging")
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesNoPaging() {
        return ResponseEntity.ok(categoryService.getAllCategoriesNoPaging());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new category with optional image and background removal")
    public ResponseEntity<CategoryResponse> createCategory(
            @Parameter(description = "Category name", required = true)
            @RequestParam("categoryName") String categoryName,

            @Parameter(description = "Category description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Category image file",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "image", required = false) MultipartFile image,

            @Parameter(description = "Remove background from image")
            @RequestParam(value = "removeBackground", defaultValue = "false") boolean removeBackground
    ) {
        try {
            CategoryRequest request = CategoryRequest.builder()
                    .categoryName(categoryName)
                    .description(description)
                    .build();

            CategoryResponse response = categoryService.createCategory(request, image, removeBackground);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update category with optional image and background removal")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id,

            @Parameter(description = "Category name", required = true)
            @RequestParam("categoryName") String categoryName,

            @Parameter(description = "Category description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Category image file",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "image", required = false) MultipartFile image,

            @Parameter(description = "Remove background from image")
            @RequestParam(value = "removeBackground", defaultValue = "false") boolean removeBackground
    ) {
        try {
            CategoryRequest request = CategoryRequest.builder()
                    .categoryName(categoryName)
                    .description(description)
                    .build();

            CategoryResponse response = categoryService.updateCategory(id, request, image, removeBackground);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Error updating category: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
